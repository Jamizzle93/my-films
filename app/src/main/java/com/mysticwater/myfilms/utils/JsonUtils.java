package com.mysticwater.myfilms.utils;

import com.google.gson.Gson;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

public class JsonUtils {

    private static final String LOG_TAG = "JsonUtils";

    public static String objectToJson(Object object)
    {
        String result = "";
        try {
            Gson gson = new Gson();
            result = gson.toJson(object);
        } catch (Exception e)
        {
            Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to convert " + object + " to JSON.");
        }
        return result;
    }

}
