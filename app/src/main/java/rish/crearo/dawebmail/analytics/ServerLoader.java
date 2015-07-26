package rish.crearo.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import rish.crearo.utils.Constants;

public class ServerLoader {

    private final static String LOGIN_PREF_KEY = "LOGIN";
    private final static String LOCATION_PREF_KEY = "LOCATION";
    private final static String PHONE_PREF_KEY = "PHONE";

    private static Queue<LocationDetails> LocationQueue;
    private static Queue<LoginDetails> LoginQueue;
    private static Queue<PhoneDetails> PhoneQueue;

    private static Context context;

    public ServerLoader(Context context) {
        ServerLoader.context = context;
        LocationQueue = getLocationPrefs();
        LoginQueue = getLoginPrefs();
        PhoneQueue = getPhonePrefs();
        sendToServer();
    }

    private static Queue<PhoneDetails> getPhonePrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PHONE_PREF_KEY, Context.MODE_PRIVATE);
        Queue<PhoneDetails> PhoneQueue;

        if (prefs.contains(PHONE_PREF_KEY)) {
            String jsonFavorites = prefs.getString(PHONE_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<PhoneDetails>>() {}.getType();
            PhoneQueue = (LinkedList<PhoneDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            PhoneQueue = new LinkedList<>();
        }

        return PhoneQueue;
    }

    private static Queue<LoginDetails> getLoginPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREF_KEY, Context.MODE_PRIVATE);
        Queue<LoginDetails> LoginQueue;

        if (prefs.contains(LOGIN_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOGIN_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LoginDetails>>() {}.getType();
            LoginQueue = (LinkedList<LoginDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            LoginQueue = new LinkedList<>();
        }

        return LoginQueue;
    }

    private static Queue<LocationDetails> getLocationPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOCATION_PREF_KEY, Context.MODE_PRIVATE);
        Queue<LocationDetails> LocationQueue;

        if (prefs.contains(LOCATION_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOCATION_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LocationDetails>>() {}.getType();
            LocationQueue = (LinkedList<LocationDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            LocationQueue = new LinkedList<>();
        }

        return LocationQueue;
    }

    public static void addLoginDetails(LoginDetails details) {
        LoginQueue = getLoginPrefs();
        LoginQueue.add(details);
        Constants.pendingBit = true;
        setLoginPrefs(LoginQueue);
    }

    private static void setLoginPrefs(Queue<LoginDetails> loginQueue) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(loginQueue);

        edit.putString(LOGIN_PREF_KEY, json);
        edit.commit();
    }

    public static void addLocationDetails(LocationDetails details) {
        LocationQueue = getLocationPrefs();
        LocationQueue.add(details);
        Constants.pendingBit = true;
        setLocationPrefs(LocationQueue);
    }

    private static void setLocationPrefs(Queue<LocationDetails> locationQueue) {
        SharedPreferences prefs = context.getSharedPreferences(LOCATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(locationQueue);

        edit.putString(LOCATION_PREF_KEY, json);
        edit.commit();
    }

    public static void addPhoneDetails(PhoneDetails details) {
        PhoneQueue = getPhonePrefs();
        PhoneQueue.add(details);
        Constants.pendingBit = true;
        setPhonePrefs(PhoneQueue);
    }

    private static void setPhonePrefs(Queue<PhoneDetails> phoneQueue) {
        SharedPreferences prefs = context.getSharedPreferences(PHONE_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(phoneQueue);

        edit.putString(PHONE_PREF_KEY, json);
        edit.commit();
    }

    public void sendToServer() {
        if (Constants.pendingBit){
            VolleyCommands volleyCommands = new VolleyCommands(context);
//            volleyCommands.POSTLocation();
        }
    }
}
