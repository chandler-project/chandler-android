package com.chandlersystem.chandler;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class AccountKitException extends Throwable {
    public AccountKitException() {
    }

    public AccountKitException(String message) {
        super(message);
    }

    public AccountKitException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountKitException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AccountKitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
