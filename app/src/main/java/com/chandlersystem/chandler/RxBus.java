package com.chandlersystem.chandler;

import com.chandlersystem.chandler.utilities.RxUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    //     Volatile means you notify to CPU to use the newest version of this variable
    private static volatile RxBus sInstance;

    //    PublishSubject: Only listen to the newest events and skip the past events
    private final PublishSubject<Object> mPublicSubject;

    /**
     * SINGLETON PATTERN
     * Double check
     *
     * @return RxBus instance
     */
    public static RxBus getInstance() {
        RxBus result = sInstance;
        if (result == null) {
            // Handle case: another thread create new instance before current thread go out of scope
            // Ex: Two thread coming into this: thread A, thread B
            // When thread A completely created a instance , thread B come and doesn't see result != null
            // Then B will see check null again one more time, then A ! null
            // Then B will return as @result
            synchronized (RxBus.class) {
                result = sInstance;
                if (result == null) {
                    sInstance = result = new RxBus();
                }
            }
        }

        return result;
    }

    public RxBus() {
        mPublicSubject = PublishSubject.create();
    }

    /**
     * post subscribe event to PublishSubject
     *
     * @param event
     */
    public void post(Object event) {
        mPublicSubject.onNext(event);
    }

    /**
     * Register to send event to the subscription (Disposable)
     *
     * @param eventClass Class name of the subscriber
     * @param onNext     Interface to handle onNext event
     * @param onError    Handle throwable
     * @param <T>        Class name
     * @return
     */
    public <T> Disposable register(final Class<T> eventClass,
                                   Consumer<T> onNext,
                                   Consumer<? super Throwable> onError) {
        return mPublicSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(o -> (T) o)
                .compose(RxUtil.withSchedulers())
                .subscribe(onNext, onError);
    }
}


