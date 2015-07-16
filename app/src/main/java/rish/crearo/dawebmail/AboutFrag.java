package rish.crearo.dawebmail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rish.crearo.R;
import rish.crearo.utils.ColorScheme;

public class AboutFrag extends Activity {

    Button b1, b2, b3, b4;
    Boolean b1c = false;
    Boolean b2c = false;
    int b3c = 0;
    Boolean b4c = false;

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
        b2 = (Button) findViewById(R.id.aboutbutton2);
        b3 = (Button) findViewById(R.id.aboutbutton3);
        b4 = (Button) findViewById(R.id.aboutbutton4);

        b1.setTextColor(Color.parseColor("#555555"));
        b2.setTextColor(Color.parseColor("#555555"));
        b3.setTextColor(Color.parseColor("#555555"));
        b4.setTextColor(Color.parseColor("#666666"));

        b1.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        b2.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        b3.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        b4.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));

        b1.setText("the Application");
        b4.setText("known bugs and fixes");
        b2.setText("the Future");
        b3.setText("the Developers");

        final String theapp = "DAWebmail.\n\nThis application arose from the need for a hand-held application for our college's webmail.\n"
                + "Currently the app is pull-based, and receives new data only once it makes a request to the webmail server.\nYour phone makes a request to the webmail server every time you connect to the internet. "
                + "\n\nA tool called Jaunt-API has been used to scrape your emails, using a virtual browser. The ID and password you use to login are encrypted and stored locally. The developer does not have access to it."
                + "\n\nThe Current Version.\nThe current version supports viewing emails in your inbox, and receiving notifications for every new webmail. More features will be added in stepwise upgrades.";

        final String thebugs = "There are a couple of minor bugs-"
                + "\n\nYes. The content is screwed up in some e-mails. We know. We're working hard to fix it!"
                + "\n\n- A process keeps running in the background for you to receive notifications. If the background process is using the internet and you turn the internet off at that very moment (not a highly probable event), the app may show a crash notification."
                + "\n\n! In case you aren't receiving any new notifications, log out and login again. \n\nGo to settings and set the notifications as required."
                + "\n\n- The app may crash while going back from the download page, after downloading an attachment. (Not always).\nSimply restart the app.";

        final String thefuture = "the Future\n\nThis is the very first step in making a full-fledged application, for you to view your webmails.\nThe next versions will have the following features-\n\n"
                + "- Searching within Webmail\n"
                + "- Better notifications system\n"
                + "- Composing webmails\n"
                + "- a SmartInbox to view only those emails that matter\n"
                + "- Auto-Delete\n"
                + "- Auto-Read\n"
                + "- And the various suggestions that you may send to us\n";

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

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (b4c == false) {
                    b4.setText(thebugs);
                    b4.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/GeosansLight.ttf"));
                    b4c = true;
                } else if (b4c == true) {
                    b4.setText("known Bugs and Fixes");
                    b4.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/helv_children.otf"));

                    b4c = false;
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (b2c == false) {
                    b2.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/GeosansLight.ttf"));
                    b2.setText(thefuture);
                    b2c = true;
                } else if (b2c == true) {
                    b2.setText("the Future");
                    b2.setTypeface(Typeface.createFromAsset(getAssets(),
                            "fonts/helv_children.otf"));
                    b2c = false;
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
