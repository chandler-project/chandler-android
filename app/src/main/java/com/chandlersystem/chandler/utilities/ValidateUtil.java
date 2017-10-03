package com.chandlersystem.chandler.utilities;

import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateUtil {
    private static final String PATTERN_BIRTH_DATE =
            "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    private static final String PATTERN_NO_SPECIFIC_CHARACTER = "^[\\p{L} .'-]+$";

    private ValidateUtil() {
    }


    public static boolean checkList(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean checkString(String s) {
        return s != null && !s.isEmpty();
    }

    public static boolean checkNull(Object object) {
        return object != null;
    }

    public static void checkString(String s, ValidateListener listener) {
        if (s != null && !s.isEmpty()) {
            listener.onValidated(s);
        } else {
            listener.onInvalid();
        }
    }

    public static boolean checkValidBirthday(final String date) {
        final Matcher matcher = Pattern.compile(PATTERN_BIRTH_DATE).matcher(date);

        if (matcher.matches()) {

            matcher.reset();

            if (matcher.find()) {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean checkBirthdayBeforeToday(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate;
        try {
            strDate = sdf.parse(birthday);
            if (System.currentTimeMillis() > strDate.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkSpecialCharacter(String content) {
        final Matcher matcher = Pattern
                .compile(PATTERN_NO_SPECIFIC_CHARACTER)
                .matcher(content);
        return matcher.matches();
    }

    public static boolean checkValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkLenght(String name, int maxLenght) {
        return checkString(name) && name.length() <= maxLenght;
    }

    public interface ValidateListener {
        void onValidated(String s);

        void onInvalid();
    }
}
