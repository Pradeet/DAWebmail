package com.sigmobile.dawebmail.commands;

import android.content.Context;
import android.os.AsyncTask;

import com.sigmobile.dawebmail.ScrappingMachine;
import com.sigmobile.dawebmail.analytics.LocationDetails;
import com.sigmobile.dawebmail.analytics.ServerLoader;

public class LoginManager extends AsyncTask<Void, Void, Void> {

    LoginListener loginListener;
    String username, pwd;
    Context context;
    String checkifloggedin = "NULL";
    long initTime, finalTime = 0;


    public LoginManager(Context context, LoginListener loginListener, String username, String pwd) {
        this.context = context;
        this.loginListener = loginListener;
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginListener.onPreLogin();
        initTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ScrappingMachine scrapper = new ScrappingMachine(username, pwd, context);
        checkifloggedin = scrapper.logIn(username, pwd);
        sendLocationDetails();
        return null;
    }

    private void sendLocationDetails() {
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setValue(context);
        ServerLoader loader = new ServerLoader(context);
        loader.addLocationDetails(locationDetails);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        finalTime = System.currentTimeMillis();
        loginListener.onPostLogin(checkifloggedin, "" + (finalTime - initTime));
    }
}