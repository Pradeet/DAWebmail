package com.sigmobile.dawebmail.analytics;

/**
 * Created by rish on 29/7/15.
 */
public class Feedback {

    public String studentID = "";
    public String feedback = "";
    public String timeToLoad = "";
    public String crashreport = "";
    public String suggestion = "";

    public Feedback(String studentID, String feedback, String timeToLoad, String crashreport, String suggestion) {
        this.studentID = studentID;
        this.feedback = feedback;
        this.timeToLoad = timeToLoad;
        this.crashreport = crashreport;
        this.suggestion = suggestion;
    }
}
