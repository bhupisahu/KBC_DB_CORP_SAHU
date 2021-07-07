package com.example.kbc_db_corp_sahu.Sharepref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharePref
{
    private static SharedPreferences mSharedPref;
    public static final String MARKS = "MARKS";

    private SharePref()
    {

    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }
    public static void clearAll() {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }
}