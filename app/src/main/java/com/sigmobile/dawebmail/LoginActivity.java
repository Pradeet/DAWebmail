package com.sigmobile.dawebmail;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sigmobile.R;
import com.sigmobile.dawebmail.analytics.PhoneDetails;
import com.sigmobile.dawebmail.analytics.VolleyCommands;
import com.sigmobile.dawebmail.commands.LoginListener;
import com.sigmobile.dawebmail.commands.LoginManager;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.ColorScheme;
import com.sigmobile.utils.Constants;

public class LoginActivity extends Activity {

    private EditText usernametf, pwdtf;
    private Button loginbtn;
    private String username = "", pwd = "";
    ProgressDialog prgDialog;
    public final int progress_bar_login = 0;
    public TextView logintitle;
    ColorScheme colorScheme;

    LoginListener loginListener;
    LoginManager loginManager;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");

        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/helv_children.otf"));
        abTitle.setTextColor(Color.parseColor("#FFFFFF"));
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#299ABD")));

        usernametf = (EditText) findViewById(R.id.login_username);
        pwdtf = (EditText) findViewById(R.id.login_password);
        loginbtn = (Button) findViewById(R.id.login_loginbtn);
        logintitle = (TextView) findViewById(R.id.login_title);

        usernametf.requestFocus();

        SharedPreferences settings = getSharedPreferences(Constants.USER_PREFERENCES, MODE_PRIVATE);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");
        logintitle.setTypeface(type);
        loginbtn.setTypeface(type);

        colorScheme = new ColorScheme(getApplicationContext());

        colorScheme.changeColorScheme();
        colorScheme.printColorScheme();

        loginListener = new LoginListener() {
            @Override
            public void onPreLogin() {
                showDialog(progress_bar_login);
            }

            @Override
            public void onPostLogin(String loginSuccess, String timeTaken) {
                Printer.println("printing what it got - " + loginSuccess);
                if (loginSuccess.equals("login successful")) {
                    Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, Main_Nav.class);
                    intent.putExtra(Constants.bundle_username, username);
                    intent.putExtra(Constants.bundle_pwd, pwd);

                    startActivity(intent);
                    finish();
                    usernametf.setText("");
                    pwdtf.setText("");
                    SharedPreferences settings = getSharedPreferences(Constants.USER_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = settings.edit();

                    prefEditor.putString(Constants.bundle_username, username);
                    prefEditor.putString(Constants.bundle_pwd, pwd);
                    prefEditor.commit();

                    sendPhoneDetails();
                    sendRegisterDetails();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                    pwdtf.setText("");
                }
                dismissDialog(progress_bar_login);
            }
        };

        String saved_uname = settings.getString(Constants.bundle_username, "none");
        String saved_pwd = settings.getString(Constants.bundle_pwd, "none");

        // already logged in
        if (!saved_uname.equals("none")) {
            Printer.println("Signing In.");
            Printer.println(username);
            username = saved_uname;
            pwd = saved_pwd;
            Intent intent = new Intent(LoginActivity.this, Main_Nav.class);
            intent.putExtra(Constants.bundle_username, username);
            intent.putExtra(Constants.bundle_pwd, pwd);

            startActivity(intent);

            this.finish();
            Printer.println(saved_pwd);
            Printer.println(saved_uname);

            if (getPrefs(Constants.prefPENDINGBIT_REGISTER)) {
                sendRegisterDetails();
            }
            if (getPrefs(Constants.prefPENDINGBIT_PHONE)) {
                sendPhoneDetails();
            }
        }

        // logging in for the first time.
        else {
            colorScheme.initialColorSchemeSetter();
            colorScheme.changeColorScheme();

            Printer.println("Signing in for the first time.");
            Printer.println(username + pwd);
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    username = usernametf.getText().toString().trim();
                    pwd = pwdtf.getText().toString();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(loginbtn.getWindowToken(), 0);
                    loginManager = new LoginManager(getApplicationContext(), loginListener, username, pwd);
                    loginManager.execute();
//                    new BackgroundRunner().setHourlyRunner(getApplicationContext());
                }
            });
        }
    }

    private void sendPhoneDetails() {

        if (getPrefs(Constants.prefPENDINGBIT_PHONE)) {
            PhoneDetails phoneDetails = new PhoneDetails();
            phoneDetails.setVlaues(activity, getApplicationContext());
            VolleyCommands volleyCommands = new VolleyCommands(getApplicationContext());
            volleyCommands.POSTPhone(phoneDetails);
        }
    }

    private void sendRegisterDetails() {
        if (getPrefs(Constants.prefPENDINGBIT_REGISTER)) {
            VolleyCommands volleyCommands = new VolleyCommands(getApplicationContext());
            volleyCommands.POSTStudent();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_login:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Logging in.");
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    private boolean getPrefs(String prefWhich) {
        SharedPreferences prefs = getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        return prefs.getBoolean(prefWhich, true);
    }
}