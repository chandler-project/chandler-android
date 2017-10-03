package com.chandlersystem.chandler.utilities;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.chandlersystem.chandler.BuildConfig;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class DeviceUtil {
    private DeviceUtil() {

    }

    public static String getUUID(Context context) {
        String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        LogUtil.logI("Android uuid", uuid);
        return uuid;
    }

    public static String getVersion() {
        return BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE;
    }

    public static String getOSVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public static String getModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getOS() {
        return "ANDROID - " + getOSVersion();
    }

    public static String getCarrier(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = null;
        if (manager != null) {
            carrierName = manager.getNetworkOperatorName();
        }
        if (ValidateUtil.checkString(carrierName)) {
            return carrierName;
        }

        return "";
    }

    public static String getPlatform() {
        return "android";
    }
}
