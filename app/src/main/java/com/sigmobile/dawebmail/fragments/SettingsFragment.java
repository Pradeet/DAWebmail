package com.sigmobile.dawebmail.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.drakeet.materialdialog.MaterialDialog;
import com.sigmobile.R;
import com.sigmobile.dawebmail.ThemeFragment;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.Constants;

public class SettingsFragment extends Fragment {

    String username, pwd;

    public SettingsFragment() {

    }

    @SuppressLint("ValidFragment")
    public SettingsFragment(String un, String pw) {
        username = un;
        pwd = pw;
    }

    TextView title_notifications, title_graphics, title_inbox;
    TextView changetheme, addtosmartbox, editautoread, editautodelete;
    Switch switch_wifi, switch_mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_three,
                container, false);
        getActivity().getActionBar().setTitle("Settings");

        // sharedprefs

        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.USER_PREFERENCES, getActivity().MODE_PRIVATE);

        Boolean toggle_wifi = prefs.getBoolean("toggle_wifi", true);
        Boolean toggle_mobiledata = prefs.getBoolean("toggle_mobiledata", true);

        title_notifications = (TextView) rootView
                .findViewById(R.id.settings_title_notifications);
        title_graphics = (TextView) rootView
                .findViewById(R.id.settings_title_graphics);
        title_inbox = (TextView) rootView
                .findViewById(R.id.settings_title_inbox);

        changetheme = (TextView) rootView
                .findViewById(R.id.settings_changetheme);
        addtosmartbox = (TextView) rootView
                .findViewById(R.id.settings_addtosmartbox);
        editautoread = (TextView) rootView
                .findViewById(R.id.settings_editautoread);
        editautodelete = (TextView) rootView
                .findViewById(R.id.settings_editauodelete);

        switch_mobile = (Switch) rootView
                .findViewById(R.id.settings_networkcell_switch);
        switch_wifi = (Switch) rootView.findViewById(R.id.settings_networkwifi_switch);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/GeosansLight.ttf");

        title_graphics.setTypeface(font, Typeface.BOLD);
        title_inbox.setTypeface(font, Typeface.BOLD);
        title_notifications.setTypeface(font, Typeface.BOLD);

        changetheme.setTypeface(font);
        addtosmartbox.setTypeface(font);
        editautodelete.setTypeface(font);
        editautoread.setTypeface(font);

        addtosmartbox.setTextColor(Color.BLACK);
        addtosmartbox.setTextSize(15);
        editautodelete.setTextColor(Color.BLACK);
        editautodelete.setTextSize(15);
        editautoread.setTextColor(Color.BLACK);
        editautoread.setTextSize(15);
        changetheme.setTextColor(Color.BLACK);
        changetheme.setTextSize(15);

        switch_mobile.setTypeface(font);
        switch_wifi.setTypeface(font);

        switch_mobile.setChecked(toggle_mobiledata);
        switch_wifi.setChecked(toggle_wifi);

        switch_mobile.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        Constants.USER_PREFERENCES, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                Printer.println("wifi " + switch_mobile);
                if (!switch_mobile.isChecked()) {
                    editor.putBoolean("toggle_mobiledata", false).commit();
                    Constants.calledbydata = "no";
                    Printer.println("mobile put to false");

                } else {
                    editor.putBoolean("toggle_mobiledata", true).commit();
                    Constants.calledby = "Mobile";
                    Constants.calledbydata = "yes";
                    Printer.println("mobile put to true");
                    Printer.println("Check interval - "
                            + Constants.checkInterval_mobiledata);
                }
            }
        });

        switch_wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        Constants.USER_PREFERENCES, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                Printer.println("wifi " + switch_wifi);
                if (!switch_wifi.isChecked()) {
                    editor.putBoolean("toggle_wifi", false).commit();
                    Constants.calledbywifi = "no";
                    Printer.println("wifi put to false");
                } else {
                    editor.putBoolean("toggle_wifi", true).commit();
                    Constants.calledby = "wifi";
                    Printer.println("wifi put to true");
                    Constants.calledbywifi = "yes";
                    Printer.println("Check interval - "
                            + Constants.checkInterval_wifi);
                }
            }
        });

        changetheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog(getActivity());
                materialDialog.setMessage("Want to Change DAWebmail's theme?");
                materialDialog.setNegativeButton("From Preset", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager()
                                .beginTransaction()
                                .add(R.id.content_frame,
                                        new ThemeFragment("Preset",
                                                username, pwd))
                                .addToBackStack(null).commit();
                        materialDialog.dismiss();
                    }
                });
                materialDialog.setPositiveButton("Customize", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager()
                                .beginTransaction()
                                .add(R.id.content_frame,
                                        new ThemeFragment("Customise",
                                                username, pwd))
                                .addToBackStack(null).commit();
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
            }
        });

        addtosmartbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "" + "Will be available in next version",
                        Toast.LENGTH_SHORT).show();
            }
        });

        editautodelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "" + "Will be available in next version",
                        Toast.LENGTH_SHORT).show();
            }
        });

        editautoread.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "" + "Will be available in next version",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
