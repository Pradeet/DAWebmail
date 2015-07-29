package com.sigmobile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rish on 16/7/15.
 */
public class BackgroundReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, BackgroundTest.class);
        context.startService(service);
    }
}
