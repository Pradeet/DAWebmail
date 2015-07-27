package rish.sigmobile.dawebmail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rish.sigmobile.R;
import rish.sigmobile.utils.ColorScheme;

public class AboutFrag extends Activity {

    Button b1, b3;
    Boolean b1c = false;
    int b3c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_frag);

        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        abTitle.setTextColor(Color
                .parseColor(ColorScheme.color_actionbartextcolor));
        ColorDrawable actionbarcolor = new ColorDrawable(
                Color.parseColor(ColorScheme.color_actionbarcolor));
        getActionBar().setBackgroundDrawable(actionbarcolor);
        invalidateOptionsMenu();

        b1 = (Button) findViewById(R.id.aboutbutton1);
        b3 = (Button) findViewById(R.id.aboutbutton3);

        b1.setTextColor(Color.parseColor("#555555"));
        b3.setTextColor(Color.parseColor("#555555"));

        b1.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        b3.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));

        b1.setText("the Application");
        b3.setText("the Developers");

        final String theapp = "DAWebmail.\n\nThis application arose from the need for a hand-held application for our college's webmail.\n"
                + "Currently the app is pull-based, and receives new data only once it makes a request to the webmail server.\nYour phone makes a request to the webmail server every time you connect to the internet. "
                + "\n\nA tool called Jaunt-API has been used to scrape your emails, using a virtual browser. The ID and password you use to login are encrypted and stored locally. The developer does not have access to it."
                + "\n\nThe Current Version.\nThe current version supports viewing emails in your inbox, and receiving notifications for every new webmail.";

        final String thedev = "the Developers\n\nThe first version was developed by a group of sophomores, during and after their rural internship period.\n\nSeveral prototypes were first made by Rishabh, before Parth Ganatra and Pradeet Swamy joined in to work on the backend of the app. Vadhir Bhupathi worked on the look and feel of the app including the graphics and the logo."
                +

                "They look forward to working with anyone interested for further development.\nYou can contact them at -\n\n"
                +

                "Rishabh Bhardwaj\nbhardwaj.rish@gmail.com\n\n" +

                "Parth Ganatra\nganatra.parth1995@gmail.com\n\n" +

                "Pradeet Swamy\npradeet1995@gmail.com\n\n" +

                "Vadhir Bhupathi\nvadhirbhupathi@gmail.com\n";

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (b1c == false) {
                    b1.setText(theapp);
                    b1.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/GeosansLight.ttf"));
                    b1c = true;
                } else if (b1c == true) {
                    b1.setText("the Application");
                    b1.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/helv_children.otf"));

                    b1c = false;
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (b3c == 0) {
                    b3.setText(thedev);
                    b3c = 1;
                    b3.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/GeosansLight.ttf"));

                } else if (b3c == 1) {
                    startActivity(new Intent(AboutFrag.this, Me.class));
                    b3.setText("the Developers");
                    b3.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/helv_children.otf"));
                    b3c = 0;
                }
            }
        });
    }
}
