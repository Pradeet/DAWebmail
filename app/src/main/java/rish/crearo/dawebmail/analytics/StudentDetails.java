package rish.crearo.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.util.Date;

import rish.crearo.utils.Constants;

/**
 * Created by rish on 26/7/15.
 */
public class StudentDetails {

    public String username = "";
    public String blue = "";
    public String regTime = "";
    Context context;

    public StudentDetails(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        this.username = settings.getString(Constants.bundle_username, "none");
        this.blue = settings.getString(Constants.bundle_pwd, "none");
        this.regTime = DateFormat.getDateTimeInstance().format(new Date());
        this.context = context;
    }

    public void addStudentDetails(StudentDetails details) {
//        new ServerLoader(context).addStudentDetails(details);
    }
}
