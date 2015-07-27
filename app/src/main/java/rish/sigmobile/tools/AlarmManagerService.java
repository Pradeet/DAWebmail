package rish.sigmobile.tools;

import rish.sigmobile.services.BackgroundService;
import rish.sigmobile.utils.Constants;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerService {
    static Activity mContext;

    public AlarmManagerService(Activity context) {
        AlarmManagerService.mContext = context;
    }

    static public void setBackgrndService(Activity mContext) {

        int checkInterval = Constants.checkInterval_noconnection;
        Constants.isconnected_internet = false;
        AlarmManager am = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = PendingIntent.getService(mContext, 0, new Intent(mContext, BackgroundService.class), 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1000 * 5), 1000 * checkInterval, intent);

        Printer.println("called service with checkInterval = " + checkInterval);
    }
}