package rish.crearo.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import rish.crearo.utils.Constants;

public class ServerLoader {

    private final static String LOGIN_PREF_KEY = "LOGIN";
    private final static String LOCATION_PREF_KEY = "LOCATION";
    private final static String PHONE_PREF_KEY = "PHONE";

    private Queue<LocationDetails> LocationQueue;
    private Queue<LoginDetails> LoginQueue;
    private Queue<PhoneDetails> PhoneQueue;

    private Context context;

    public ServerLoader(Context context) {
        this.context = context;
        LocationQueue = getLocationPrefs();
        LoginQueue = getLoginPrefs();
        PhoneQueue = getPhonePrefs();
    }

    private Queue<PhoneDetails> getPhonePrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PHONE_PREF_KEY, Context.MODE_PRIVATE);
        Queue<PhoneDetails> PhoneQueue;

        if (prefs.contains(PHONE_PREF_KEY)) {
            String jsonFavorites = prefs.getString(PHONE_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<PhoneDetails>>() {
            }.getType();
            PhoneQueue = (LinkedList<PhoneDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            PhoneQueue = new LinkedList<>();
        }

        return PhoneQueue;
    }

    private Queue<LoginDetails> getLoginPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREF_KEY, Context.MODE_PRIVATE);
        Queue<LoginDetails> LoginQueue;

        if (prefs.contains(LOGIN_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOGIN_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LoginDetails>>() {
            }.getType();
            LoginQueue = (LinkedList<LoginDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            LoginQueue = new LinkedList<>();
        }

        return LoginQueue;
    }

    private Queue<LocationDetails> getLocationPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(LOCATION_PREF_KEY, Context.MODE_PRIVATE);
        Queue<LocationDetails> LocationQueue;

        if (prefs.contains(LOCATION_PREF_KEY)) {
            String jsonFavorites = prefs.getString(LOCATION_PREF_KEY, null);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LocationDetails>>() {
            }.getType();
            LocationQueue = (LinkedList<LocationDetails>) gson.fromJson(jsonFavorites, listType);
        } else {
            LocationQueue = new LinkedList<>();
        }

        return LocationQueue;
    }

    public void addLoginDetails(LoginDetails details) {
        LoginQueue = getLoginPrefs();
        LoginQueue.add(details);
        setLoginPrefs(LoginQueue);
    }

    private void setLoginPrefs(Queue<LoginDetails> loginQueue) {
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
    }

    private void setLocationPrefs(Queue<LocationDetails> locationQueue) {
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

    private void setPhonePrefs(Queue<PhoneDetails> phoneQueue) {
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