package rish.crearo.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Date;

import rish.crearo.R;
import rish.crearo.dawebmail.LoginActivity;

/**
 * Created by rish on 16/7/15.
 */
public class BackgroundTest extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("Time = " + new Date().getSeconds());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

//public void createNotification(){
//        NotificationManager mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
//        Intent intent1 = new Intent(this.getApplicationContext(), LoginActivity.class);
//
//        Notification notification = new Notification(R.drawable.ic_launcher, "This is a test message!" + new Date().getTime(), System.currentTimeMillis());
//        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);
//        mManager.notify(0, notification);
//}

