package rish.sigmobile.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rish.sigmobile.utils.Constants;

public class ServerLoader {

    private final static String LOGIN_PREF_KEY = "LOGIN";
    private final static String LOCATION_PREF_KEY = "LOCATION";
    private final static String PHONE_PREF_KEY = "PHONE";

    private ArrayList<LocationDetails> LocationQueue;
    private ArrayList<LoginDetails> LoginQueue;
    private ArrayList<PhoneDetails> PhoneQueue;

    private Context context;

    public ServerLoader(Context context) {
        this.context = context;
        LocationQueue = getLocationPrefs();
        LoginQueue = getLoginPrefs();
        PhoneQueue = getPhonePrefs();
    }

    private ArrayList<PhoneDetails> getPhonePrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PHONE_PREF_KEY, Context.MODE_PRIVATE);
        ArrayList<PhoneDetails> PhoneQueue = new ArrayList<>();

        if (prefs.contains(PHONE_PREF_KEY)) {
            String jsonFavorites = prefs.getString(PHONE_PREF_KEY, null);

            try {
                JSONArray jsonArray = new JSONArray(jsonFavorites);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PhoneDetails phoneDetails = new PhoneDetails();
                    phoneDetails.Phone_AppList = jsonObject.getString("");
                    phoneDetails.Phone_Model = jsonObject.getString("");
                    phoneDetails.Phone_AndroidVersion = jsonObject.getString("");
                    phoneDetails.Phone_Brand = jsonObject.getString("");
                    phoneDetails.Phone_ScreenSize = jsonObject.getString("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<PhoneDetails>>() {
//            }.getType();
//            PhoneQueue = (ArrayList<PhoneDetails>) gson.fromJson(jsonFavorites, listType);
        }

        return PhoneQueue;
    }

    private ArrayList<LoginDetails> getLoginPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREF_KEY, Context.MODE_PRIVATE);
        ArrayList<LoginDetails> LoginQueue = new ArrayList<>();

        if (prefs.contains(LOGIN_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOGIN_PREF_KEY, null);

            try {
                JSONArray jsonArray = new JSONArray(jsonFavorites);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    LoginDetails loginDetails = new LoginDetails();
                    loginDetails.Login_TimeStamp = object.getString("Login_TimeStamp");
                    loginDetails.Login_Connection = object.getString("Login_Connection");
                    loginDetails.Login_loginType = object.getString("Login_loginType");
                    loginDetails.Login_connectionDetails = object.getString("Login_connectionDetails");
                    loginDetails.Login_studentID = object.getString("Login_studentID");
                    loginDetails.Login_Success = object.getString("Login_Success");
                    loginDetails.Login_TimeTaken = object.getString("Login_TimeTaken");
                    LoginQueue.add(loginDetails);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<LoginDetails>>() {
//            }.getType();
//            LoginQueue = (ArrayList<LoginDetails>) gson.fromJson(jsonFavorites, listType);
        }

        return LoginQueue;
    }

    private ArrayList<LocationDetails> getLocationPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOCATION_PREF_KEY, Context.MODE_PRIVATE);
        ArrayList<LocationDetails> LocationQueue = new ArrayList<>();

        if (prefs.contains(LOCATION_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOCATION_PREF_KEY, null);
            try {
                JSONArray jsonArray = new JSONArray(jsonFavorites);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.d("TAG", object.get("Location_IPAddress") + "");
                    LocationDetails details = new LocationDetails();
                    details.Location_IPAddress = object.getString("Location_IPAddress");
                    details.Location_Subnet = object.getString("Location_Subnet");
                    details.Location_studentID = object.getString("Location_studentID");
                    details.Location_WifiName = object.getString("Location_WifiName");
                    details.Location_TimeStamp = object.getString("Location_TimeStamp");
                    LocationQueue.add(details);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<LocationDetails>>() {
//            }.getType();
////            locationArray = gson.fromJson(jsonFavorites, LocationDetails[].class);
////            LocationQueue = new ArrayList<LocationDetails>(Arrays.asList(locationArray));
//            System.out.println("jsonfav : " + jsonFavorites);
//            System.out.println("ListType:    " + listType.toString());
//            System.out.println( "hallelujah : " + gson.fromJson(jsonFavorites, listType));
//            LocationQueue = (ArrayList<LocationDetails>) (gson.fromJson(jsonFavorites, listType));
        }

        return LocationQueue;
    }

    public void addLoginDetails(LoginDetails details) {
        LoginQueue = getLoginPrefs();
        LoginQueue.add(details);
        setLoginPrefs(LoginQueue);
        setPrefs(Constants.prefPENDINGBIT_LOGIN, true);
    }

    private void setPrefs(String prefWhich, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean(prefWhich, value);
        edit.commit();
    }

    private void setLoginPrefs(ArrayList<LoginDetails> loginQueue) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(loginQueue);

        edit.putString(LOGIN_PREF_KEY, json);
        edit.commit();
    }

    public void addLocationDetails(LocationDetails details) {
        LocationQueue = getLocationPrefs();
        LocationQueue.add(details);
        setLocationPrefs(LocationQueue);
        setPrefs(Constants.prefPENDINGBIT_LOCATION, true);
    }

    private void setLocationPrefs(ArrayList<LocationDetails> locationQueue) {
        SharedPreferences prefs = context.getSharedPreferences(LOCATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(locationQueue);

        edit.putString(LOCATION_PREF_KEY, json);
        edit.commit();
    }

    public void addPhoneDetails(PhoneDetails details) {
        PhoneQueue = getPhonePrefs();
        PhoneQueue.add(details);
        setPhonePrefs(PhoneQueue);
    }

    private void setPhonePrefs(ArrayList<PhoneDetails> phoneQueue) {
        SharedPreferences prefs = context.getSharedPreferences(PHONE_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(phoneQueue);

        edit.putString(PHONE_PREF_KEY, json);
        edit.commit();
    }

    public void sendToServer() {
        VolleyCommands volleyCommands = new VolleyCommands(context);
        if (getPrefs(Constants.prefPENDINGBIT_LOCATION))
            volleyCommands.POSTLocation(LocationQueue);
        if (getPrefs(Constants.prefPENDINGBIT_LOGIN))
            volleyCommands.POSTLogin(LoginQueue);
//        if (getPrefs(Constants.prefPENDINGBIT_PHONE))
//            volleyCommands.POSTPhone(PhoneQueue);
//        if (getPrefs(Constants.prefPENDINGBIT_REGISTER))
//            volleyCommands.POSTStudent();
    }

    private boolean getPrefs(String prefWhich) {
        SharedPreferences prefs = context.getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        return prefs.getBoolean(prefWhich, true);
    }
}