package rish.crearo.dawebmail;

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

import rish.crearo.R;
import rish.crearo.dawebmail.commands.LoginListener;
import rish.crearo.dawebmail.commands.LoginManager;
import rish.crearo.utils.ColorScheme;
import rish.crearo.utils.Constants;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");

        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        abTitle.setTextColor(Color.parseColor("#FFFFFF"));
        ColorDrawable actionbarcolor = new ColorDrawable(
                Color.parseColor("#299ABD"));
        getActionBar().setBackgroundDrawable(actionbarcolor);

        usernametf = (EditText) findViewById(R.id.login_username);
        pwdtf = (EditText) findViewById(R.id.login_password);
        loginbtn = (Button) findViewById(R.id.login_loginbtn);
        logintitle = (TextView) findViewById(R.id.login_title);

        usernametf.requestFocus();

        SharedPreferences settings = getSharedPreferences(
                Constants.USER_PREFERENCES, MODE_PRIVATE);

        Typeface type = Typeface.createFromAsset(getAssets(),
                "fonts/GeosansLight.ttf");
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
            public void onPostLogin(String loginSuccess) {
                System.out.println("printing what it got - " + loginSuccess);
                if (loginSuccess.equals("login successful")) {
                    Toast.makeText(getApplicationContext(), "Logged in!",
                            Toast.LENGTH_SHORT).show();
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
                } else {
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful!",
                            Toast.LENGTH_SHORT).show();
                    pwdtf.setText("");
                }
                dismissDialog(progress_bar_login);
            }
        };

        String saved_uname = settings.getString(Constants.bundle_username,
                "none");
        String saved_pwd = settings.getString(Constants.bundle_pwd, "none");

        // already logged in
        if (!saved_uname.equals("none")) {
            System.out.println("Signing In.");
            System.out.println(username);
            username = saved_uname;
            pwd = saved_pwd;
            Intent intent = new Intent(LoginActivity.this, Main_Nav.class);
            intent.putExtra(Constants.bundle_username, username);
            intent.putExtra(Constants.bundle_pwd, pwd);

            startActivity(intent);

            this.finish();
            System.out.println(saved_pwd);
            System.out.println(saved_uname);
        }

        // logging in for the first time.
        else {
            colorScheme.initialColorSchemeSetter();
            colorScheme.changeColorScheme();

            System.out.println("Signing in for the first time.");
            System.out.println(username + pwd);
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
}