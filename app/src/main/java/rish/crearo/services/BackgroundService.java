package rish.crearo.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;

import rish.crearo.R;
import rish.crearo.dawebmail.EmailMessage;
import rish.crearo.dawebmail.ScrappingMachine;
import rish.crearo.utils.Constants;

public class BackgroundService extends Service {
    private static final String TAG = "MyService";
    public static Boolean isLoggedin = false;
    String username, pwd;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        stopSelf(Constants.currentServiceID);
        System.out.println("onDestry called");
    }

    @Override
    public void onStart(Intent intent, int startid) {
        username = intent.getExtras().getString(Constants.bundle_username);
        pwd = intent.getExtras().getString(Constants.bundle_pwd);
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();

        Log.d(TAG, "onStart");
    }

    public void stopSelfService() {
        stopSelf(Constants.currentServiceID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("-----the half an hour service called-----");

        refreshInbox_BroadcastFunction();

        return Service.START_STICKY;
    }

    public void refreshInbox_BroadcastFunction() {
        System.out.println("-------------------------\nStarting the refresh.");

        System.out.println("has wifi connection " + isConnectedByWifi());
        System.out.println("has data conn " + isConnectedByMobileData());

        // get shared prefs of toggle switches in frag
        SharedPreferences prefs = getSharedPreferences(
                Constants.USER_PREFERENCES, MODE_PRIVATE);

        Boolean wifi_enabled = prefs.getBoolean("toggle_wifi", true);
        Boolean mobiledata_enabled = prefs.getBoolean("toggle_mobiledata",
                false);

        System.out.println("wifienabled toggle = " + wifi_enabled);
        System.out.println("mobiledataenabled toggle = " + mobiledata_enabled);

        System.out.println("flag variable - " + Constants.isconnected_internet);

        if (Constants.isconnected_internet == false
                && ((wifi_enabled && isConnectedByWifi()) || (mobiledata_enabled && isConnectedByMobileData()))) {
            Constants.isconnected_internet = true;
            new async_refreshInbox().execute("");
            System.out.println("Checking for mail once");

        } else if (Constants.isconnected_internet == true
                && (isConnectedByWifi() == false && isConnectedByMobileData() == false)) {
            Constants.isconnected_internet = false;
            System.out.println("No need to check for mail");

        } else if ((Constants.isconnected_internet == true && ((wifi_enabled && isConnectedByWifi()) || (mobiledata_enabled && isConnectedByMobileData())))) {
            System.out.println("ON GOING PROCESS. BOTH TRUE. DOING NOTHING.");
        }

    }

    public class async_refreshInbox extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scraper = new ScrappingMachine(username, pwd,
                    getApplicationContext());
            if (!(scraper.checkifLoggedInLong())) {
                System.out.println("Not logged in.");
                SharedPreferences prefs = getSharedPreferences(
                        Constants.USER_PREFERENCES, MODE_PRIVATE);

                scraper.logIn(prefs.getString("Username", "none"),
                        prefs.getString("Password", "none"));
                System.out.println("Logged in");
            }
            scraper.scrapeAllMessagesfromInbox(false);
            System.out.println("Scraped all emails");

            return "Executed";
        }

        @Override
        public void onPostExecute(String result) {
            System.out.println("Service completed called");
            afterServiceCompleted();
        }
    }

    public void afterServiceCompleted() {
        Collections.reverse(ScrappingMachine.allemails);

        for (EmailMessage m : ScrappingMachine.allemails)
            m.save();// now all e-mails are in the database

        if (ScrappingMachine.totalnew == 0) {
            System.out.println("0 new emails");
        } else if (ScrappingMachine.totalnew == 1) {
            showNotification("One New Webmail!", ScrappingMachine.allemails
                    .get(0).getFromName(), ScrappingMachine.allemails.get(0)
                    .getSubject());
        } else
            // showNotification(ScrappingMachine.totalnew + " new Webmails!",
            // ScrappingMachine.allemails.get(0).getFromName(),
            // ScrappingMachine.allemails.get(0).getSubject());
            showNotification(ScrappingMachine.totalnew + " new Webmails!",
                    ScrappingMachine.totalnew + " new Webmails!",
                    "Open Webmail to View.");

        ScrappingMachine.clear_AllEmailsAL();
    }

    public Boolean isConnectedByWifi() {
        if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            // Constants.connectedby = Constants.WIFI;
            System.out.println("is connected by wifi");
            return true;
        } else {
            return false;
        }
    }

    public Boolean isConnectedByMobileData() {
        if (((ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnected()) {
            // Constants.connectedby = Constants.MOBILE_DATA;
            System.out.println("is connected by mobile data");
            return true;
        } else {
            return false;
        }
    }

    public void showNotification(String msgnumber, String sendername, String subject) {
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.final_notification, msgnumber, System.currentTimeMillis());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_ALL;

        Intent notificationintent = new Intent(this, rish.crearo.dawebmail.Main_Nav.class);
        // this can later be changed to open new email
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationintent, 0);
        notification.setLatestEventInfo(this, sendername, subject, pi);
        notificationmanager.notify(new Random().nextInt(10000), notification);
    }

    public static void cancelNotification(Context context) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancelAll();
    }

}