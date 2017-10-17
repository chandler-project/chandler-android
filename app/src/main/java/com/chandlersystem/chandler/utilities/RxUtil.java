package com.chandlersystem.chandler.utilities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;

import static com.chandlersystem.chandler.configs.RxConstant.DEFAULT_DEBOUNCE_DURATION_LONG;
import static com.chandlersystem.chandler.configs.RxConstant.DEFAULT_DEBOUNCE_DURATION_SHORT;

public class RxUtil {
    private static final String TAG = RxUtil.class.getCanonicalName();

    /**
     * @param <R> the generic type of Observable
     * @return an Observable which has the same parameter but subscribe on IO and observe on Main Thread
     */
    public static <R> ObservableTransformer<R, R> withSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <R> ObservableTransformer<R, R> withSchedulersIO() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static <R> ObservableTransformer<R, R> withProgressTerminal(View progressView, @Nullable View mainView) {
        return upstream -> upstream.doOnSubscribe(disposable -> {
            ViewUtil.toggleView(progressView, true);
            if (mainView != null) {
                ViewUtil.toggleView(mainView, false);
            }
        }).doOnTerminate(() -> {
            ViewUtil.toggleView(progressView, false);
            if (mainView != null) {
                ViewUtil.toggleView(mainView, true);
            }
        });
    }

    public static <R> ObservableTransformer<R, R> withProgressError(View progressView, View mainView) {
        return upstream -> upstream.doOnSubscribe(disposable -> {
            ViewUtil.toggleView(progressView, true);
            ViewUtil.toggleView(mainView, false);
        }).doOnError(throwable -> {
            throwable.printStackTrace();
            ViewUtil.toggleView(progressView, false);
            ViewUtil.toggleView(mainView, true);
        });
    }

    public static <R> ObservableTransformer<R, R> withLongThrottleFirst() {
        return upstream -> upstream.throttleFirst(DEFAULT_DEBOUNCE_DURATION_LONG,
                TimeUnit.MILLISECONDS);
    }

    public static <R> ObservableTransformer<R, R> withShortThrottleFirst() {
        return upstream -> upstream.throttleFirst(DEFAULT_DEBOUNCE_DURATION_SHORT,
                TimeUnit.MILLISECONDS);
    }

    public static <R> ObservableTransformer<R, R> withProgressTerminal(SwipeRefreshLayout progressView) {
        return upstream -> upstream
                .doOnSubscribe(disposable -> {
                    LogUtil.logD(TAG, "doOnSubscribe#SwipeRefreshLayout#isRefreshing: " + progressView.isRefreshing());
                    if (!progressView.isRefreshing()) {
                        progressView.setRefreshing(true);
                    }
                })
                .doOnTerminate(() -> {
                    LogUtil.logD(TAG, "doOnTerminal#SwipeRefreshLayout#isRefreshing:" + progressView.isRefreshing());
                    if (progressView.isRefreshing()) {
                        progressView.setRefreshing(false);
                    }
                });
    }

    public static <R> ObservableTransformer<R, R> withProgressBar(View progressView) {
        return upstream -> upstream
                .doOnSubscribe(disposable -> {
                    ViewUtil.toggleView(progressView, true);
                })
                .doOnTerminate(() -> {
                    ViewUtil.toggleView(progressView, false);
                });
    }
}
