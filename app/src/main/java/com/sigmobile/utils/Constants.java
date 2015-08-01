package com.sigmobile.utils;

public class Constants {

    //    public static final String BASEURL = "http://pradeet.pythonanywhere.com/";
    public static final String BASEURL = "http://192.168.150.1:8080/";

    public final static String USER_PREFERENCES = "UserPrefs";
    public final static String USER_APP_TUTORIAL = "app_tutorial";

    public static Boolean isconnected_internet = false;

    public static String WIFI = "wifi";
    public static String MOBILE_DATA = "mobile data";

    public static int checkInterval_wifi = (int) (30 * 60);
    public static int checkInterval_mobiledata = (int) (30 * 60);
    public static int checkInterval_noconnection = 30 * 60;

    public static int currentServiceID = 100;

    public static String RandomStringText = "keep calm and use webmail";

    public static String calledby = "none";
    public static String calledbywifi = "";
    public static String calledbydata = "";

    public static Boolean isLoggedin = false;

    public static String bundle_username = "__username";
    public static String bundle_pwd = "__pwd";

    public static String prefPENDINGBIT_PHONE = "PENDINGBIT_PHONE";
    public static String prefPENDINGBIT_REGISTER = "PENDINGBIT_REGISTER";
    public static String prefPENDINGBIT_ACTION = "PENDINGBIT_REGISTER";

    public static final String API_USERNAME = "dawebmail";
    public static final String API_PASSWD = "machoman";
    public static final String API_VERSION = "v1";

    public static final String ENCRYPTER_KEY = "mAcHoMaN";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_VIEWEMAIL = "view email";
    public static final String ACTION_REFRESH = "refresh";
    public static final String ACTION_MASTERREFRESH = "master refresh";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_DOWNLOADATT = "download attachment";
}
