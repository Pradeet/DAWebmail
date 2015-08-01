package com.sigmobile.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.sigmobile.utils.Constants;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by rish on 1/8/15.
 */
public class ActionDetails {

    private Context context;
    public String action_StudentID = "STUDENT ID";
    public String action_Action = "ACTION";
    public String action_Connection = "No Connection";
    public String action_ConnectionDetails = "No Connection";
    public String action_TimeStamp = "Time";
    public String action_TimeTaken = "TimeTaken";
    public String action_Success = "false";

    public ActionDetails(Context context, String action_Action, String action_TimeStamp, String action_TimeTaken, String action_Success) {
        this.context = context;
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        this.action_StudentID = settings.getString(Constants.bundle_username, "none");
        this.action_Action = action_Action;
        this.action_Connection = getConnectionType();
        this.action_ConnectionDetails = getWifiName(context);
        this.action_TimeStamp = currentDateTimeString;
        this.action_TimeTaken = action_TimeTaken;
        this.action_Success = action_Success;
    }

    private String getConnectionType() {
        String conn = "Not connected";
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                conn = "3G";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                conn = "Wifi";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private String getWifiName(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String wifiname = wifiInfo.getSSID();
        return wifiname;
    }


}