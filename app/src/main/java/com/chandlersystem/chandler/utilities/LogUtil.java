package com.chandlersystem.chandler.utilities;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chandlersystem.chandler.BuildConfig;

import java.util.List;

public class LogUtil {
    /**
     * Used to indicate critical failure, this is the level printed at when throwing an Exception.
     *
     * @param tag
     * @param mess
     */
    public static void logE(@Nullable String tag, String mess) {
        log(LogType.ERROR, tag, mess);
    }

    /**
     * Used to indicate a warning, mainly for recoverable failures
     *
     * @param tag
     * @param mess
     */
    public static void logW(@Nullable String tag, String mess) {
        log(LogType.WARN, tag, mess);
    }


    /**
     * Used to indicate higher-level information about the state of the application
     *
     * @param tag
     * @param mess
     */
    public static void logI(@Nullable String tag, String mess) {
        log(LogType.INFO, tag, mess);
    }


    /**
     * Used to log information that would be useful to know when debugging the application,
     * but would get in the way when running the application
     *
     * @param tag
     * @param mess
     */
    public static void logD(@Nullable String tag, String mess) {
        log(LogType.DEBUG, tag, mess);
    }

    /**
     * Used to log information that reflects the small details about the state of the application
     *
     * @param tag
     * @param mess
     */
    public static void logV(@Nullable String tag, String mess) {
        log(LogType.VERBOSE, tag, mess);
    }

    /**
     * Used to log information about a condition that should never happen.
     * wtf stands for "What a Terrible Failure".
     *
     * @param tag
     * @param mess
     */
    public static void logWTF(@Nullable String tag, String mess) {
        log(LogType.ASSERT, tag, mess);
    }

    /**
     * Only show log in development environment
     *
     * @param logType
     * @param tag
     * @param mess
     */
    private static void log(LogType logType, @Nullable String tag, String mess) {
        if (tag == null) {
            tag = "Chandler-Application";
        }

        if (mess == null) {
            mess = "Something wrong happen!!";
        } else {
            mess = ": " + mess;
        }

//        WARNING: Please never remove this line of code: BuildConfig.DEBUG
//        Only show log in dev environment
        if (BuildConfig.DEBUG) {
            switch (logType) {
                case ERROR:
                    Log.e(tag, mess);
                    break;
                case WARN:
                    Log.w(tag, mess);
                    break;
                case INFO:
                    Log.i(tag, mess);
                    break;
                case DEBUG:
                    logWithLink(tag, mess);
                    break;
                case VERBOSE:
                    Log.v(tag, mess);
                    break;
                case ASSERT:
                    Log.wtf(tag, mess);
                    break;
            }
        }
    }

    /**
     * Stack level
     * Level1: logWithLink
     * Level2: LogUtil
     * Level3: Name of class which used LogUtil. Ex: MainActivity
     * Level4: Name of super class. Ex: Activity
     * ...
     * WARNING: High level can throw IndexOutOfBoundException
     * <p>
     * TIPS: Very useful when logging with mode DEBUG
     *
     * @param tag
     * @param msg
     */
    private static void logWithLink(String tag, String msg) {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
                .getStackTrace();
        int currentIndex = 5;

        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName
                .lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String
                .valueOf(stackTraceElement[currentIndex].getLineNumber());

        Log.d(tag, msg + "\nat " + fullClassName + "." + methodName + "("
                + className + ".java:" + lineNumber + ")");
    }

    /**
     * Loop through list and log each of them.
     *
     * @param tag
     * @param objects
     */
    public static void logList(String tag, List<?> objects) {
        for (Object object : objects) {
            logD(tag, object.toString() + "\n");
        }
    }

    private enum LogType {
        ERROR,
        WARN,
        INFO,
        DEBUG,
        VERBOSE,
        ASSERT
    }
}
