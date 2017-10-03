package com.chandlersystem.chandler.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * detect if the network is available
 */

public class NetworkUtil {
    private NetworkUtil() {
    }

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0,
            NETWORK_STAUS_WIFI = 1,
            NETWORK_STATUS_MOBILE = 2;

    /**
     * @param context Context
     * @return a integer represent for the status of internet connection which defines above
     */
    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }

    /**
     * @param context Context
     * @return a boolean value to detect if the devide is connected(Wifi or Mobile data) or not
     */
    public static boolean isNetworkAvailable(Context context) {
        return getConnectivityStatus(context) != NETWORK_STATUS_NOT_CONNECTED;
    }
}
