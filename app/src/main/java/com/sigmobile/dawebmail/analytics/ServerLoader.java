package com.sigmobile.dawebmail.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.gson.Gson;
import com.sigmobile.tools.ConnectionManager;
import com.sigmobile.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerLoader {

    private final static String ACTION_PREF_KEY = "ACTION";

    private ArrayList<ActionDetails> ActionQueue;

    private Context context;

    public ServerLoader(Context context) {
        this.context = context;
        ActionQueue = getActionPrefs();
    }

    private ArrayList<ActionDetails> getActionPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(ACTION_PREF_KEY, Context.MODE_PRIVATE);
        ArrayList<ActionDetails> ActionQueue = new ArrayList<>();

        if (prefs.contains(ACTION_PREF_KEY)) {
            String jsonFavorites = prefs.getString(ACTION_PREF_KEY, null);
            try {
                JSONArray jsonArray = new JSONArray(jsonFavorites);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ActionDetails actionDetails = new ActionDetails(jsonObject.getString("action_StudentID"), jsonObject.getString("action_Action"), jsonObject.getString("action_Connection"), jsonObject.getString("action_ConnectionDetails"), jsonObject.getString("action_TimeStamp"), jsonObject.getString("action_TimeTaken"), jsonObject.getString("action_Success"));
                    ActionQueue.add(actionDetails);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ActionQueue;
    }

    private void setActionPrefs(ArrayList<ActionDetails> actionPrefs) {
        SharedPreferences prefs = context.getSharedPreferences(ACTION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(actionPrefs);

        edit.putString(ACTION_PREF_KEY, json);
        edit.commit();
    }

    public void clearActionPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(ACTION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        ArrayList<ActionDetails> empty = new ArrayList<>();
        Gson gson = new Gson();
        String json = gson.toJson(empty);

        edit.putString(ACTION_PREF_KEY, json);
        edit.commit();
    }

    public void addActionDetails(String username, String action, String timeTaken, String success) {
        ActionDetails actionDetails = null;
        String timedate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        if (ConnectionManager.isConnectedByMobileData(context)) {
            actionDetails = new ActionDetails(username, action, Constants.MOBILE_DATA, ConnectionManager.getNetworkClass(context), timedate, timeTaken, success);
        } else if (ConnectionManager.isConnectedByWifi(context)) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String wifiname = wifiInfo.getSSID();
            actionDetails = new ActionDetails(username, action, Constants.WIFI, wifiname, timedate, timeTaken, success);
        } else {
            actionDetails = new ActionDetails(username, action, "NOT CONNECTED", "-", timedate, timeTaken, "FAIL");
        }

        System.out.println("Adding action detail - " + actionDetails.action_Action);
        ActionQueue = getActionPrefs();
        ActionQueue.add(actionDetails);
        setActionPrefs(ActionQueue);
        setPrefs(Constants.prefPENDINGBIT_ACTION, true);
    }

    public void sendToServer() {
        ActionQueue = getActionPrefs();
        VolleyCommands volleyCommands = new VolleyCommands(context);
        if (getPrefs(Constants.prefPENDINGBIT_ACTION))
            volleyCommands.POSTAction(ActionQueue);
    }

    private void setPrefs(String prefWhich, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean(prefWhich, value);
        edit.commit();
    }

    private boolean getPrefs(String prefWhich) {
        SharedPreferences prefs = context.getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        return prefs.getBoolean(prefWhich, true);
    }
}