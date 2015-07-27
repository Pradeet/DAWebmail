package rish.sigmobile.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rish on 16/7/15.
 */
public class BackgroundRunner {

    public void setHourlyRunner(Context context) {
        Intent intent = new Intent(context, BackgroundReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 1000, 5 * 1000, pendingIntent);
    }
}
