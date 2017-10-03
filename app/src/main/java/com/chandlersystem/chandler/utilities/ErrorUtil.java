package com.chandlersystem.chandler.utilities;

import android.content.Context;

import com.chandlersystem.chandler.R;
import com.facebook.FacebookAuthorizationException;
import com.facebook.accountkit.AccountKitException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import retrofit2.HttpException;

public class ErrorUtil {
    private ErrorUtil() {
    }

    /**
     * Get error message from throwable
     *
     * @param context
     * @param throwable represent the error which return by Retrofit or others request api library
     * @return String indicates the message.
     */
    public static String getErrorMessage(Context context, Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            JSONObject jObjError;
            try {
                String jsonError = ((HttpException) throwable)
                        .response().errorBody().string();
                jObjError = new JSONObject(jsonError);
                return jObjError.getString("message");
            } catch (JSONException | IOException | NullPointerException e) {
                e.printStackTrace();
                return context.getString(R.string.error_common);
            }
        } else if (throwable instanceof FacebookAuthorizationException || throwable instanceof IOException) {
            return context.getString(R.string.content_top_bar_no_internet_connection);
        } else if (throwable instanceof NoSuchElementException) {
            return context.getString(R.string.error_no_data);
        } else if (throwable instanceof UnknownHostException) {
            return context.getString(R.string.error_unknown_host);
        } else if (throwable instanceof SocketTimeoutException) {
            return context.getString(R.string.error_socket_timeout);
        } else if (throwable instanceof AccountKitException) {
            return context.getString(R.string.error_common);
        }

        return context.getString(R.string.error_common);
    }

    /**
     * Handle error response code. Ex: 401 -> Logout
     *
     * @param context
     * @param throwable
     */
    /*public static Disposable handleCode401(@NonNull Context context, @NonNull Throwable throwable) {
        return Observable.just(throwable)
                .filter(throwable12 -> throwable12 instanceof HttpException)
                .map(throwable1 -> (HttpException) throwable1)
                .filter(e -> e.code() == 401)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {

                }, throwable13 -> DialogUtil.showMessageDialog(context, throwable13));
    }*/
}
