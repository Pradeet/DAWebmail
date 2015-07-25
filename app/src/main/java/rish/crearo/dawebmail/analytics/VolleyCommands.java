package rish.crearo.dawebmail.analytics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import AES_v1_0.AES;
import rish.crearo.tools.AppController;
import rish.crearo.utils.Constants;

public class VolleyCommands {
    Context context;

    String username = "", blue = "";

    public VolleyCommands(Context context) {
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFERENCES, context.MODE_PRIVATE);
        username = settings.getString(Constants.bundle_username, "none");
        blue = settings.getString(Constants.bundle_pwd, "none");
        AES.setKey(Constants.ENCRYPTER_KEY);
        blue = AES.encrypt(blue);
    }

    public void POSTStudent() {
        final String URL = Constants.BASEURL + Constants.API_VERSION + "/student";
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
//        pDialog.show();
        pDialog.setCancelable(false);

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        params.put("u_studentid", username);
        params.put("u_blue", blue);
        params.put("u_regTime", "" + currentDateTimeString);

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE = " + response);
                        pDialog.hide();
                        //if successful, set pending bit of register to false
                        setPrefs(Constants.prefPENDINGBIT_REGISTER, false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", Constants.API_USERNAME, Constants.API_PASSWD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        // add the request object to the queue to be executed
        AppController.getInstance().addToRequestQueue(req);
    }

    public void POSTLogin(Queue<LoginDetails> loginQueue) {
        final String URL = Constants.BASEURL + Constants.API_VERSION + "/login";
        final ProgressDialog pDialog = new ProgressDialog(context);
        JSONArray jsonArray = new JSONArray();

        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < loginQueue.size(); i++) {
            LoginDetails details = loginQueue.poll();
            params.put("l_studentID", details.Login_studentID);
            params.put("l_timestamp", "" + details.Login_TimeStamp);
            params.put("l_type", "" + details.Login_loginType);
            params.put("l_connection", "" + details.Login_Connection);
            params.put("l_connectiondetails", details.Login_connectionDetails);
            jsonArray.put(new JSONObject(params));
            params.clear();
        }
        makeRequest(pDialog, URL, jsonArray);
    }

    public void POSTPhone(Queue<PhoneDetails> phoneQueue) {
        final String URL = Constants.BASEURL + Constants.API_VERSION + "/phone";
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONArray jsonArray = new JSONArray();

        HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < phoneQueue.size(); i++) {
            PhoneDetails phoneDetails = phoneQueue.poll();

            params.put("p_studentID", username);
            params.put("p_brand", "" + phoneDetails.Phone_Brand);
            params.put("p_product", "" + phoneDetails.Phone_AndroidVersion);
            params.put("p_model", "" + phoneDetails.Phone_Model);
            params.put("p_applist", "" + phoneDetails.Phone_AppList);
            params.put("p_screensize", phoneDetails.Phone_ScreenSize);
            jsonArray.put(new JSONObject(params));
            params.clear();
        }
        makeRequest(pDialog, URL, jsonArray);
    }

    public void POSTPhone(PhoneDetails phoneDetails) {
        final String URL = Constants.BASEURL + Constants.API_VERSION + "/phone";
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
//        pDialog.show();
        pDialog.setCancelable(false);

        JSONArray jsonArray = new JSONArray();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("p_studentID", username);
        params.put("p_brand", "" + phoneDetails.Phone_Brand);
        params.put("p_product", "" + phoneDetails.Phone_AndroidVersion);
        params.put("p_model", "" + phoneDetails.Phone_Model);
        params.put("p_applist", "" + phoneDetails.Phone_AppList);
        params.put("p_screensize", phoneDetails.Phone_ScreenSize);
        jsonArray.put(new JSONObject(params));

        makeRequest(pDialog, URL, jsonArray);
    }

    public void POSTLocation(Queue<LocationDetails> locationQueue) {
        final String URL = Constants.BASEURL + Constants.API_VERSION + "/location";
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONArray jsonArray = new JSONArray();

        HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < locationQueue.size(); i++) {
            LocationDetails locationDetails = locationQueue.poll();
            params.put("c_studentID", locationDetails.Location_studentID);
            params.put("c_timestamp", "" + locationDetails.Location_TimeStamp);
            params.put("c_wifiname", "" + locationDetails.Location_WifiName);
            params.put("c_ipaddress", "" + locationDetails.Location_IPAddress);
            params.put("c_subnet", "" + locationDetails.Location_Subnet);
            jsonArray.put(new JSONObject(params));
            params.clear();
        }
        makeRequest(pDialog, URL, jsonArray);
    }

    public void makeRequest(final ProgressDialog pDialog, final String URL, JSONArray jsonArray) {
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                pDialog.dismiss();
                //set pending to flase once sent successfully
                if (URL.contains("location")) {
                    setPrefs(Constants.prefPENDINGBIT_LOCATION, false);
                }
                if (URL.contains("login")) {
                    setPrefs(Constants.prefPENDINGBIT_LOGIN, false);
                }
                if (URL.contains("phone")) {
                    setPrefs(Constants.prefPENDINGBIT_PHONE, false);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
            }
        };

        JsonArrayRequest reqarray = new JsonArrayRequest(URL, jsonArray, jsonArrayListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", Constants.API_USERNAME, Constants.API_PASSWD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(reqarray);
    }

    private void setPrefs(String prefWhich, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean(prefWhich, value);
        edit.commit();
    }

    //UNUSED
    /*
    public void GETLogin() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = Constants.BASEURL + Constants.API_VERSION + "/student";

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", Constants.API_USERNAME, Constants.API_PASSWD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    */
}