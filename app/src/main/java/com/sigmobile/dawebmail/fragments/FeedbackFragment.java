package com.sigmobile.dawebmail.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sigmobile.R;
import com.sigmobile.dawebmail.analytics.FeedbackDetails;
import com.sigmobile.dawebmail.analytics.VolleyCommands;
import com.sigmobile.utils.Constants;

import java.text.DateFormat;
import java.util.Date;

public class FeedbackFragment extends Fragment {

    EditText editText_1;

    TextView title, text1, text2;

    Button buttonsend, rateus;

    public FeedbackFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);

        editText_1 = (EditText) rootView.findViewById(R.id.feedback_etbox);

        title = (TextView) rootView.findViewById(R.id.feedback_title);
        text1 = (TextView) rootView.findViewById(R.id.feeback_text1);
        text2 = (TextView) rootView.findViewById(R.id.feeback_text2);

        buttonsend = (Button) rootView.findViewById(R.id.feedback_send);
        rateus = (Button) rootView.findViewById(R.id.feedback_ratingbar);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Comix Loud.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Alex_Bold.ttf");

        title.setTypeface(font);
        text1.setTypeface(font2);
        text2.setTypeface(font2);

        editText_1.setTypeface(font2);

        editText_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.feedback_etbox) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        buttonsend.setTypeface(font2);

        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String et1 = editText_1.getText().toString();
                SharedPreferences settings = getActivity().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                FeedbackDetails feedback = new FeedbackDetails(settings.getString(Constants.bundle_username, "none"), et1, currentDateTimeString);
                new VolleyCommands(getActivity()).POSTFeedback(getActivity(), feedback);
            }
        });
        rateus.setTypeface(font);
        rateus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    launchMarket();
                }
                return true;
            }
        });

        return rootView;
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to rate :(", Toast.LENGTH_LONG).show();
        }
    }

    public void showFeedbackResponse(Context context, boolean error) {
        if (!error)
            Toast.makeText(context, "Hallelujah", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "No Success man", Toast.LENGTH_LONG).show();
    }

    // some chanhe made
//    public void setSpinner1() {
//        List<String> list = new ArrayList<String>();
//        list.add("The same speed on both - decently fast");
//        list.add("Not so long on wifi, 3G, forever.");
//        list.add("The same speed on both - very slow");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.element_spinner, list);
//        dataAdapter.setDropDownViewResource(R.layout.element_spinner);
//        spinner1.setAdapter(dataAdapter);
//    }
//
//    public void setSpinner2() {
//        List<String> list = new ArrayList<String>();
//        list.add("Barely ever");
//        list.add("Frequent");
//        list.add("All the time");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.element_spinner, list);
//        dataAdapter.setDropDownViewResource(R.layout.element_spinner);
//        spinner2.setAdapter(dataAdapter);
//    }
}
