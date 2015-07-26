package rish.crearo.utils;

import java.util.Date;

public class Constants {

    public static final String BASEURL = "http://pradeet.pythonanywhere.com/";

    public final static String USER_PREFERENCES = "UserPrefs";
    public final static String USER_APP_TUTORIAL = "app_tutorial";

    public static Boolean isconnected_internet = false;

    public static String WIFI = "wifi";
    public static String MOBILE_DATA = "mobiledata";

    public static int checkInterval_wifi = (int) (30 * 60);
    public static int checkInterval_mobiledata = (int) (30 * 60);
    public static int checkInterval_noconnection = 30 * 60;

    public static int currentServiceID = 100;

    public static String RandomStringText = "keep calm and use webmail";

    public static String calledby = "none";
    public static String calledbywifi = "";
    public static String calledbydata = "";

    public static Boolean isLoggedin = false;

    public static String bundle_username = "_username";
    public static String bundle_pwd = "_pwd";

    public static String SELFDESTRUCT = "July 31";

//    public static boolean PENDINGBIT_PHONE= true;
//    public static boolean PENDINGBIT_LOCATION= true;
//    public static boolean PENDINGBIT_LOGIN= true;
//    public static boolean PENDINGBIT_REGISTER= true;

    public static String prefPENDINGBIT_PHONE = "PENDINGBIT_PHONE";
    public static String prefPENDINGBIT_LOCATION = "PENDINGBIT_LOCATION";
    public static String prefPENDINGBIT_LOGIN = "PENDINGBIT_LOGIN";
    public static String prefPENDINGBIT_REGISTER = "PENDINGBIT_REGISTER";

    public static final String API_USERNAME = "dawebmail";
    public static final String API_PASSWD = "machoman";
    public static final String API_VERSION = "v1";

    public static final String ENCRYPTER_KEY = "mAcHoMaN";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String MANUAL = "manual";
    public static final String AUTOMATIC = "automatic";


}
