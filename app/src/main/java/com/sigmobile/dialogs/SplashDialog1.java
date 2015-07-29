package com.sigmobile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sigmobile.R;
import com.sigmobile.utils.Constants;

public class SplashDialog1 extends Dialog {

    public Activity activity;

    TextView tv_title, tv_content, tv_bottom;
    ImageView btn_back, btn_next;

    int TYPE = 0;
    int currentPage = 0;

    public SplashDialog1(Activity activity, int TYPE) {
        super(activity);
        this.activity = activity;
        this.TYPE = TYPE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_dialog1);
        setFonts();

        tv_title = (TextView) findViewById(R.id.splash_title);
        tv_bottom = (TextView) findViewById(R.id.splash_bottom);
        tv_content = (TextView) findViewById(R.id.splash_content);

        btn_back = (ImageView) findViewById(R.id.splash_btnback);
        btn_next = (ImageView) findViewById(R.id.splash_btnnext);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                dismiss();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                dismiss();
            }
        });

        setTexts();
    }

    private void setTexts() {
        SharedPreferences settings = getContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        String username = settings.getString(Constants.bundle_username, "none");
    }

    public void setFonts() {
        ((TextView) findViewById(R.id.splash_title)).setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) findViewById(R.id.splash_content)).setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) findViewById(R.id.splash_bottom)).setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Alex_Bold.ttf"));
    }

    public void sendGmail() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse("bhardwaj.rish@gmail.com"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bhardwaj.rish@gmail.com"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions Complaints");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi Rish!\n\nHere are a few suggestions/complaints about the webmail app : \n\n");
        activity.startActivity(sendIntent);
    }
}
