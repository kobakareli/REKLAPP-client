package com.example.koba.reklappclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Koba on 25/07/2016.
 */
public class NextDay extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.preferences_key), Context.MODE_PRIVATE);
        prefs.edit().putInt("videoCount", 0).commit();
    }
}
