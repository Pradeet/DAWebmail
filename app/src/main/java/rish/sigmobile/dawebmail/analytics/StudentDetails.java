package rish.sigmobile.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import rish.sigmobile.utils.Constants;

/**
 * Created by rish on 26/7/15.
 */
public class StudentDetails implements Serializable{

    public String username = "";
    public String blue = "";
    public String regTime = "";
    Context context;

    public StudentDetails(){
        username = "";
        blue = "";
        regTime = "";
        context = null;
    }

    public void setValues(Context context) {
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
