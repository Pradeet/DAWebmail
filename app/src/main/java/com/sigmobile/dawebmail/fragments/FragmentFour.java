package com.sigmobile.dawebmail.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sigmobile.R;
import com.sigmobile.dawebmail.analytics.Feedback;
import com.sigmobile.dawebmail.analytics.VolleyCommands;
import com.sigmobile.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FragmentFour extends Fragment {

    EditText editText_1, editText_2;
    Spinner spinner1, spinner2;

    TextView title, text1, text2, text3, text4, text5;

    Button buttonsend;

    public FragmentFour() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);

        editText_1 = (EditText) rootView.findViewById(R.id.feedback_etbox);
        editText_2 = (EditText) rootView.findViewById(R.id.feedback_etbox2);

        spinner1 = (Spinner) rootView.findViewById(R.id.feedback_spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.feedback_spinner2);

        title = (TextView) rootView.findViewById(R.id.feedback_title);
        text1 = (TextView) rootView.findViewById(R.id.feeback_text1);
        text2 = (TextView) rootView.findViewById(R.id.feeback_text2);
        text3 = (TextView) rootView.findViewById(R.id.feeback_text3);
        text4 = (TextView) rootView.findViewById(R.id.feeback_text4);
        text5 = (TextView) rootView.findViewById(R.id.feeback_text5);

        buttonsend = (Button) rootView.findViewById(R.id.feedback_send);

        setSpinner1();
        setSpinner2();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Comix Loud.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Alex_Bold.ttf");

        title.setTypeface(font);
        text1.setTypeface(font2);
        text2.setTypeface(font2);
        text3.setTypeface(font2);
        text4.setTypeface(font2);
        text5.setTypeface(font2);

        editText_1.setTypeface(font2);
        editText_2.setTypeface(font2);

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

        editText_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.feedback_etbox2) {
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
                String et2 = editText_2.getText().toString();

                String crashreport = spinner1.getSelectedItem().toString();
                String timereport = spinner2.getSelectedItem().toString();

                SharedPreferences settings = getActivity().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);


                Feedback feedback = new Feedback(settings.getString(Constants.bundle_username, "none"), et1, timereport, crashreport, et2);
                new VolleyCommands(getActivity()).POSTFeedback(getActivity(), feedback);
            }
        });


        return rootView;
    }

    public void showFeedbackResponse(Context context, boolean error) {
        if (!error)
            Toast.makeText(context, "Hallelujah", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "No Success man", Toast.LENGTH_LONG).show();
    }

    public void setSpinner1() {
        List<String> list = new ArrayList<String>();
        list.add("The same speed on both - decently fast");
        list.add("Not so long on wifi, 3G, forever.");
        list.add("The same speed on both - very slow");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.element_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.element_spinner);
        spinner1.setAdapter(dataAdapter);
    }

    public void setSpinner2() {
        List<String> list = new ArrayList<String>();
        list.add("Barely ever");
        list.add("Frequent");
        list.add("All the time");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.element_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.element_spinner);
        spinner2.setAdapter(dataAdapter);
    }
}
