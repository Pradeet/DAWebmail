package rish.crearo.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.util.Date;

import rish.crearo.utils.Constants;

public class LoginDetails {

    Context context;

    public String Login_studentID;
    public String Login_Connection;
    public String Login_TimeStamp;
    public String Login_connectionDetails;
    public String Login_loginType;
    public String Login_TimeTaken;
    public String Login_Success;


    public LoginDetails(Context context, String LoginType, String LoginSuccess, String LoginTime) {
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        Login_studentID = settings.getString(Constants.bundle_username, "none");
        Login_Connection = getConnectionType();
        Login_TimeStamp = DateFormat.getDateTimeInstance().format(new Date());
        Login_connectionDetails = getConDetails();
        Login_loginType = LoginType;
        Login_Success = LoginSuccess;
        Login_TimeTaken = LoginTime;
    }

    private String getConDetails() {
        return "None for now";
    }

    private String getConnectionType() {
        String conn = "Not connected";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            conn = "3G";
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            conn = "Wifi";
        }
        return conn;
    }

    public void addLoginDetails(LoginDetails details) {
        new ServerLoader(context).addLoginDetails(details);
    }
}
