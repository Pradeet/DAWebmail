package rish.crearo.tools;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by rish on 16/7/15.
 */
public class ConnectionManager {

    public static Boolean isConnectedByWifi(Context context) {
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

    public static Boolean isConnectedByMobileData(Context context) {
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
}
