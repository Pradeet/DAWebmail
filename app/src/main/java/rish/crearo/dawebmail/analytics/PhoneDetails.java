package rish.crearo.dawebmail.analytics;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rish on 24/7/15.
 */
public class PhoneDetails {

    Context context;
    Activity activity;

    public PhoneDetails(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    public String Phone_Brand = Build.BRAND;
    public String Phone_Model = Build.MODEL;
    public String Phone_AndroidVersion = Build.VERSION.RELEASE;
    public String Phone_AppList = getApplist(context);
    public String Phone_ScreenSize = getScreenSize(activity);

    private String getApplist(Context context) {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        JSONObject jsonApps = new JSONObject();
        String stringapps = "";
        int count = 1;
        for (ApplicationInfo packageInfo : packages) {
            try {
                if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    stringapps += "" + packageInfo.packageName + ",";
                    jsonApps.put("" + count++, "" + packageInfo.packageName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringapps;
    }

    private String getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;

        double diagonal = Math.sqrt(Math.pow(wi, 2) + Math.pow(hi, 2));
        diagonal = Math.round(diagonal * 100.0) / 100.0;

        return "{ 'diagonal': '" + diagonal + "', width : '" + wi + "', 'height' : '" + hi + "' }";
    }
}
