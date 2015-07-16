package rish.crearo.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rish.crearo.R;
import rish.crearo.dawebmail.Main_Nav;
import rish.crearo.utils.Constants;

public class SplashDialog1 extends Dialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public int splashnumber = 1;
    public static SplashDialog1 cdd;

    public SplashDialog1(Activity a, int splashnum) {
        super(a);
        this.splashnumber = splashnum;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (splashnumber) {
            case 1:
                setContentView(R.layout.splash_dialog1);
                fonts1();
                findViewById(R.id.splash1_btnnext).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 6);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });

                break;

            case 6:
                setContentView(R.layout.splash_dialog6);
                fonts6();
                findViewById(R.id.splash6_btnback).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 1);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });

                findViewById(R.id.splash6_btnnext).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 2);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });
                break;

            case 2:
                setContentView(R.layout.splash_dialog2);
                fonts2();
                findViewById(R.id.splash2_btnnext).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 3);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });
                findViewById(R.id.splash2_btnback).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 1);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });

                break;
            case 3:
                setContentView(R.layout.splash_dialog3);
                fonts3();
                findViewById(R.id.splash3_btnnext).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 4);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });
                findViewById(R.id.splash3_btnback).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 2);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });

                break;

            case 4:
                setContentView(R.layout.splash_dialog4);
                fonts4();
                findViewById(R.id.splash4_btnnext).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 5);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });
                findViewById(R.id.splash4_btnback).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 3);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });

                findViewById(R.id.splash4_txt_dia2).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setType("plain/text");
                                sendIntent.setData(Uri
                                        .parse("bhardwaj.rish@gmail.com"));
                                sendIntent
                                        .setClassName("com.google.android.gm",
                                                "com.google.android.gm.ComposeActivityGmail");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "bhardwaj.rish@gmail.com" });
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                                        "Suggestions Complaints");
                                sendIntent
                                        .putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Hi Rish!\n\nHere are a few suggestions/complaints about the webmail app : \n\n");
                                c.startActivity(sendIntent);

                            }
                        });
                findViewById(R.id.splash4_img_newmail).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setType("plain/text");
                                sendIntent.setData(Uri
                                        .parse("bhardwaj.rish@gmail.com"));
                                sendIntent
                                        .setClassName("com.google.android.gm",
                                                "com.google.android.gm.ComposeActivityGmail");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "bhardwaj.rish@gmail.com" });
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                                        "Suggestions Complaints");
                                sendIntent
                                        .putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Hi Rish!\n\nHere are a few suggestions/complaints about the webmail app : \n\n");
                                c.startActivity(sendIntent);

                            }
                        });
                findViewById(R.id.splash4_txt_dia4).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setType("plain/text");
                                sendIntent.setData(Uri
                                        .parse("bhardwaj.rish@gmail.com"));
                                sendIntent
                                        .setClassName("com.google.android.gm",
                                                "com.google.android.gm.ComposeActivityGmail");
                                sendIntent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { "bhardwaj.rish@gmail.com" });
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                                        "Suggestions Complaints");
                                sendIntent
                                        .putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Hi Rish!\n\nHere are a few suggestions/complaints about the webmail app : \n\n");
                                c.startActivity(sendIntent);

                            }
                        });

                break;

            case 5:
                setContentView(R.layout.splash_dialog5);
                fonts5();
                findViewById(R.id.splash5_btnnext).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                Toast.makeText(
                                        c,
                                        "Please wait if the content hasnt loaded yet",
                                        Toast.LENGTH_LONG).show();
                                cdd.dismiss();
                            }
                        });
                findViewById(R.id.splash5_btnback).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                                cdd = new SplashDialog1(Main_Nav.activity, 4);
                                cdd.setCancelable(false);
                                cdd.show();
                            }
                        });
                SharedPreferences prefs = c.getSharedPreferences(
                        Constants.USER_APP_TUTORIAL, c.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("app_tutorial", false).commit();
                break;

            case 7:
                setContentView(R.layout.splash_dialog7);
                fonts7();
                findViewById(R.id.splash7_btnback).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                cdd.dismiss();
                            }
                        });
                break;

            default:
                setContentView(R.layout.splash_dialog1);
                break;
        }
    }

    public void fonts1() {
        ((TextView) findViewById(R.id.splash1_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash1_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash1_txt_dia2)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash1_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash1_txt_dia4)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts2() {
        ((TextView) findViewById(R.id.splash2_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash2_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash2_txt_dia2)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash2_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts3() {
        ((TextView) findViewById(R.id.splash3_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash3_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash3_txt_dia4)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts4() {
        ((TextView) findViewById(R.id.splash4_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash4_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash4_txt_dia2)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash4_txt_dia4)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts5() {
        ((TextView) findViewById(R.id.splash5_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash5_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash5_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts6() {
        ((TextView) findViewById(R.id.splash6_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash6_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash6_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void fonts7() {
        ((TextView) findViewById(R.id.splash7_txt_dia0)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash7_txt_dia1)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash7_txt_dia3)).setTypeface(Typeface
                .createFromAsset(c.getAssets(), "fonts/Alex_Bold.ttf"));
    }

}
