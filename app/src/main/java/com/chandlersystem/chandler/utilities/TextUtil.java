package com.chandlersystem.chandler.utilities;

import android.content.Context;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TextUtil {
    private TextUtil() {

    }

    public static String getCurrencyApproximate(int number) {
        if ((number / 1000000) >= 1) {
            return (number % 1000000 == 0 ? "" : "~") + number / 1000000 + "M";
        } else if ((number / 1000) >= 1) {
            return (number % 1000 == 0 ? "" : "~") + number / 1000 + "k";
        } else return number + "";
    }

    public static String formatCurrency(String number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(new BigDecimal(number));
    }
}
