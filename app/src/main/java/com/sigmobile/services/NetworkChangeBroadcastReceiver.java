package com.sigmobile.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.Random;

import com.sigmobile.R;
import com.sigmobile.dawebmail.EmailMessage;
import com.sigmobile.dawebmail.LoginActivity;
import com.sigmobile.dawebmail.ScrappingMachine;
import com.sigmobile.tools.ConnectionManager;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.Constants;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    Context context;
    String username, pwd;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.username = intent.getExtras().getString(Constants.bundle_username);
        this.pwd = intent.getExtras().getString(Constants.bundle_pwd);
        this.context = context;

        refreshInbox_BroadcastFunction();

//        LocationDetails locationDetails = new LocationDetails();
//        locationDetails.setValue(context);
//        ServerLoader loader = new ServerLoader(context);
//        loader.addLocationDetails(locationDetails);
    }

    public void refreshInbox_BroadcastFunction() {

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.USER_PREFERENCES, context.MODE_PRIVATE);

        Boolean wifi_enabled = prefs.getBoolean("toggle_wifi", true);
        Boolean mobiledata_enabled = prefs.getBoolean("toggle_mobiledata",
                false);

        if (Constants.isconnected_internet == false
                && ((wifi_enabled && ConnectionManager.isConnectedByWifi(context)) || (mobiledata_enabled && ConnectionManager.isConnectedByMobileData(context)))) {
            Constants.isconnected_internet = true;
            try {
                new async_refreshInbox().execute("");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Printer.println("Checking for mail once");
        } else if (Constants.isconnected_internet == true
                && (ConnectionManager.isConnectedByWifi(context) == false && ConnectionManager.isConnectedByMobileData(context) == false)) {
            Constants.isconnected_internet = false;
            Printer.println("No need to check for mail");

        } else if ((Constants.isconnected_internet == true && ((wifi_enabled && ConnectionManager.isConnectedByWifi(context)) || (mobiledata_enabled && ConnectionManager.isConnectedByMobileData(context))))) {
            try {
                new async_refreshInbox().execute("");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                String ret = scraper.logIn(uname, pwd);

                if (ret.equals("login successful")) {
//                    LoginDetails loginDetails = new LoginDetails();
//                    loginDetails.setValues(context, Constants.AUTOMATIC, Constants.TRUE, "---");
//                    ServerLoader loader = new ServerLoader(context);
//                    loader.addLoginDetails(loginDetails);
                }
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
            int numberToShow = (ScrappingMachine.totalnew >= 5) ? 5 : ScrappingMachine.totalnew;
            Printer.println(numberToShow + "");
            sendInboxStyleNotification(numberToShow);
        }
        ScrappingMachine.clear_AllEmailsAL();
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
                com.sigmobile.dawebmail.LoginActivity.class);

        // this can later be changed to open new email
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                notificationintent, 0);
        notification.setLatestEventInfo(context, sendername, subject, pi);
        notificationmanager.notify(new Random().nextInt(10000), notification);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nMgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    public void sendInboxStyleNotification(int numberToShow) {
        PendingIntent pi = getPendingIntent();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("DAWebmails")
                .setContentText("Open to view webmails!")
                .setSmallIcon(R.drawable.final_notification_small)
                .addAction(R.drawable.final_notification_small, "Open DAWebmail", pi);

        Notification.InboxStyle notification1 = new Notification.InboxStyle(builder);

        for (int i = 0; i < numberToShow; i++) {
            notification1.addLine(ScrappingMachine.allemails.get(ScrappingMachine.allemails.size() - 1 - i).getFromName());
            notification1.addLine(ScrappingMachine.allemails.get(ScrappingMachine.allemails.size() - 1 - i).getSubject());
        }

        Notification notification = notification1.build();

        // Put the auto cancel notification flag
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public PendingIntent getPendingIntent() {
        cancelNotification(context);
        return PendingIntent.getActivity(context, 0, new Intent(context,
                LoginActivity.class), 0);
    }
}