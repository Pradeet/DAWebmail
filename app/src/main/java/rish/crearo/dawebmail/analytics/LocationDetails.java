package rish.crearo.dawebmail.analytics;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by rish on 24/7/15.
 */
public class LocationDetails {

    Context context;

    public LocationDetails(Context context) {
        this.context = context;
    }

    public String Location_TimeStamp = DateFormat.getDateTimeInstance().format(new Date());
    public String Location_WifiName = getWifiName(context);
    public String Location_IPAddress = getIPAddress(context);
    public String Location_Subnet = "EMPTY";


    private String getWifiName(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String wifiname = wifiInfo.getSSID();
        return wifiname;
    }

    private String getIPAddress(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ip;
    }
}
