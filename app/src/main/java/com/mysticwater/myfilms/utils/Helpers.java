package com.mysticwater.myfilms.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class Helpers {

    public static String getRegionCode(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        return locale.getCountry();
    }

}
