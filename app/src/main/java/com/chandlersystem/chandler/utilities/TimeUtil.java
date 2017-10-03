package com.chandlersystem.chandler.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    public static final String FORMAT_WORLD_WIDE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_FULL_DATE_TIME = "HH:mm:ss a dd/MM/yyyy";
    public static final String FORMAT_FULL_DATE_TIME_WITH_AM_PM = "dd/MM/yyyy hh:mm a";
    public static final String FORMAT_DATE_WEST = "yyyy-MM-dd";
    public static final String FORMAT_DATE_VN = "dd/MM/yyyy";

    private TimeUtil() {
    }

    /**
     * Convert from time as string to timestamp (long)
     *
     * @param time
     * @return
     */
    public static long convertStringToTimestamp(String time) {
        try {
            DateFormat isoUtcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            isoUtcFormat.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
            return isoUtcFormat.parse(time).getTime() / 1000;
        } catch (Exception e) {//this generic but you can control another types of exception
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Convert from @timestamp to String with new @format
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String convertTimestampToString(long timestamp, String format) {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
            cal.setTimeInMillis(timestamp * 1000);
            String date = android.text.format.DateFormat.format(format, cal).toString();
            return date;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get current timestamp in second
     *
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Convert from old format string to new format string
     *
     * @param oldFormat
     * @param newFormat
     * @param time
     * @return
     */
    public static String convert(String oldFormat, String newFormat, String time) {
        if (!ValidateUtil.checkString(time))
            return "";

        String formattedDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat, Locale.getDefault());
        Date myDate;
        try {
            myDate = dateFormat.parse(time);
            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
            formattedDate = timeFormat.format(myDate);
            return formattedDate;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
