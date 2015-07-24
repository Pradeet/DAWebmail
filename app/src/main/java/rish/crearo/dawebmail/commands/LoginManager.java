package rish.crearo.dawebmail.commands;

import android.content.Context;
import android.os.AsyncTask;

import rish.crearo.dawebmail.ScrappingMachine;

public class LoginManager extends AsyncTask<Void, Void, Void> {

    LoginListener loginListener;
    String username, pwd;
    Context context;
    String checkifloggedin = "NULL";

    public LoginManager(Context context, LoginListener loginListener, String username, String pwd) {
        this.loginListener = loginListener;
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginListener.onPreLogin();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ScrappingMachine scrapper = new ScrappingMachine(username, pwd,
                context);
        checkifloggedin = scrapper.logIn(username, pwd);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loginListener.onPostLogin(checkifloggedin);
    }
}