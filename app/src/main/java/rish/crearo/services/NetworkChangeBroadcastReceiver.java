package rish.crearo.services;

import java.util.Collections;
import java.util.Random;

import rish.crearo.R;
import rish.crearo.dawebmail.EmailMessage;
import rish.crearo.dawebmail.ScrappingMachine;
import rish.crearo.tools.Printer;
import rish.crearo.utils.Constants;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    Context context;
    String username, pwd;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.username = intent.getExtras().getString(Constants.bundle_username);
        this.pwd = intent.getExtras().getString(Constants.bundle_pwd);
        this.context = context;

        refreshInbox_BroadcastFunction();
    }

    public void refreshInbox_BroadcastFunction() {

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.USER_PREFERENCES, context.MODE_PRIVATE);

        Boolean wifi_enabled = prefs.getBoolean("toggle_wifi", true);
        Boolean mobiledata_enabled = prefs.getBoolean("toggle_mobiledata",
                false);

        if (Constants.isconnected_internet == false
                && ((wifi_enabled && isConnectedByWifi()) || (mobiledata_enabled && isConnectedByMobileData()))) {
            Constants.isconnected_internet = true;
            new async_refreshInbox().execute("");
            Printer.println("Checking for mail once");
        } else if (Constants.isconnected_internet == true
                && (isConnectedByWifi() == false && isConnectedByMobileData() == false)) {
            Constants.isconnected_internet = false;
            Printer.println("No need to check for mail");

        } else if ((Constants.isconnected_internet == true && ((wifi_enabled && isConnectedByWifi()) || (mobiledata_enabled && isConnectedByMobileData())))) {
            new async_refreshInbox().execute("");
            Printer.println("ON GOING PROCESS. BOTH TRUE. DOING NOTHING.");
        }
    }

    public class async_refreshInbox extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scraper = new ScrappingMachine(username, pwd,
                    context);
            if (!(scraper.checkifLoggedInLong())) {
                Printer.println("Not logged in.");
                SharedPreferences prefs = context.getSharedPreferences(
                        Constants.USER_PREFERENCES, context.MODE_PRIVATE);

                String uname = prefs.getString(Constants.bundle_username,
                        "none");
                String pwd = prefs.getString(Constants.bundle_pwd, "none");
                scraper.logIn(uname, pwd);
            }
            scraper.scrapeAllMessagesfromInbox(false);

            return "Executed";
        }

        @Override
        public void onPostExecute(String result) {
            afterServiceCompleted();
        }
    }

    public void afterServiceCompleted() {
        Collections.reverse(ScrappingMachine.allemails);

        for (EmailMessage m : ScrappingMachine.allemails)
            m.save();// now all e-mails are in the database

        if (ScrappingMachine.totalnew == 0) {
            Printer.println("0 new emails");
        } else if (ScrappingMachine.totalnew == 1) {
            showNotification("One New Webmail!", ScrappingMachine.allemails
                    .get(0).getFromName(), ScrappingMachine.allemails.get(0)
                    .getSubject());
        } else {
            showNotification(ScrappingMachine.totalnew + " new Webmails!",
                    ScrappingMachine.totalnew + " new Webmails!",
                    "Open Webmail to View.");
        }
        ScrappingMachine.clear_AllEmailsAL();
    }

    public Boolean isConnectedByWifi() {
        if (((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            // Constants.connectedby = Constants.WIFI;
            Printer.println("is connected by wifi");
            return true;
        } else {
            return false;
        }
    }

    public Boolean isConnectedByMobileData() {
        if (((ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
            // Constants.connectedby = Constants.MOBILE_DATA;
            Printer.println("is connected by mobile data");
            return true;
        } else {
            return false;
        }
    }

    public void showNotification(String msgnumber, String sendername,
                                 String subject) {
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(
                R.drawable.final_notification, msgnumber,
                System.currentTimeMillis());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_ALL;

        Intent notificationintent = new Intent(context,
                rish.crearo.dawebmail.LoginActivity.class);

        // this can later be changed to open new email
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                notificationintent, 0);
        notification.setLatestEventInfo(context, sendername, subject, pi);
        notificationmanager.notify(new Random().nextInt(10000), notification);
    }

    public static void cancelNotification(Context context) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context
                .getSystemService(ns);
        nMgr.cancelAll();
    }
}
