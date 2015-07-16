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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rish.crearo.R;
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

        String saved_uname = settings.getString(Constants.bundle_username,
                "none");
        String saved_pwd = settings.getString(Constants.bundle_pwd, "none");

        // so here Ive decided, if youve logged in once already, the app
        // will take you directly to your inbox. Once there, you can choose to
        // login

        // logging in after signing out.
        if (saved_uname.equals("noneagain")) {
            usernametf.requestFocus();
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    username = usernametf.getText().toString().trim();
                    pwd = pwdtf.getText().toString();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(loginbtn.getWindowToken(), 0);
                    String txt_forAkshar = "ACCESS DENIED : "
                            + "Akshar!\nYeah. Now the app works well. And its cool. Meh. You still dont have access.";
                    if (username.trim().equals("201301442")) {
                        Toast.makeText(getApplicationContext(),
                                "" + txt_forAkshar, Toast.LENGTH_LONG).show();
                    } else {
                        new asyncLogin_LoginActivity(username, pwd).execute("");
                    }
                }
            });
        }

        else if (!saved_uname.equals("none")) {
            System.out.println("signing in again.");
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

            System.out.println("signing in for the first time.");
            System.out.println(username + pwd);
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    username = usernametf.getText().toString().trim();
                    pwd = pwdtf.getText().toString();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(loginbtn.getWindowToken(), 0);
                    String txt_forAkshar = "ACCESS DENIED : "
                            + "Akshar Alert!\n This application cannot be used by you."
                            + "";
                    if (username.trim().equals("201301442")) {
                        Toast.makeText(getApplicationContext(),
                                "" + txt_forAkshar, Toast.LENGTH_LONG).show();
                    } else {
                        new asyncLogin_LoginActivity(username, pwd).execute("");
                    }
                }
            });
        }
    }

    public class asyncLogin_LoginActivity extends
            AsyncTask<String, Void, String> {
        String checkifloggedin = "";
        String uname, pwd;

        public asyncLogin_LoginActivity(String uname, String pwd) {
            this.uname = uname;
            this.pwd = pwd;
        }

        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scrapper = new ScrappingMachine(username, pwd,
                    getApplicationContext());
            checkifloggedin = scrapper.logIn(this.uname, this.pwd);
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            showDialog(progress_bar_login);

        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        public void onPostExecute(String result) {
            System.out.println("printing what it got - " + checkifloggedin);
            if (checkifloggedin.equals("login successful")) {
                Toast.makeText(getApplicationContext(), "Logged in!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, Main_Nav.class);
                intent.putExtra(Constants.bundle_username, username);
                intent.putExtra(Constants.bundle_pwd, pwd);

                startActivity(intent);
                finish();
                usernametf.setText("");
                pwdtf.setText("");
                SharedPreferences settings = getSharedPreferences(
                        Constants.USER_PREFERENCES, MODE_PRIVATE);
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
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_login:
                prgDialog = new ProgressDialog(this);
                // Logging in.\n
                // keep calm and use webmail
                prgDialog.setMessage("Logging in.");
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }
}
