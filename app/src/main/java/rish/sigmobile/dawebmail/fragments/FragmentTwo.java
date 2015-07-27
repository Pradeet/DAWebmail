package rish.sigmobile.dawebmail.fragments;

import rish.sigmobile.R;
import rish.sigmobile.services.SavedStatistics;
import rish.sigmobile.utils.Statistics;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentTwo extends Fragment {

    public FragmentTwo() {
    }

    private View rootView;
    private ImageView sendEmailbtn;
    private TextView mostsender1, mostsender2, mostsender3, lastref, subtitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_fragment_two, container,
                false);

        fonts();
        sendEmailbtn = (ImageView) rootView
                .findViewById(R.id.smartbox_sendbutton);
        mostsender1 = (TextView) rootView
                .findViewById(R.id.smartbox_mostsender1);
        mostsender2 = (TextView) rootView
                .findViewById(R.id.smartbox_mostsender2);
        mostsender3 = (TextView) rootView
                .findViewById(R.id.smartbox_mostsender3);
        lastref = (TextView) rootView.findViewById(R.id.smartbox_lastreftv);
        subtitle = (TextView) rootView.findViewById(R.id.smartbox_subtitel1);

        Statistics statistics = new Statistics();

        mostsender1.setText("" + statistics.map_.get(0).number + "  "
                + statistics.map_.get(0).sender);
        mostsender2.setText("" + statistics.map_.get(1).number + "  "
                + statistics.map_.get(1).sender);
        mostsender3.setText("" + statistics.map_.get(2).number + "  "
                + statistics.map_.get(2).sender);

        sendEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendEmail();
            }
        });

        if (SavedStatistics.count(SavedStatistics.class, null, null) != 0)
            lastref.setText("Last refreshed - "
                    + SavedStatistics.findById(SavedStatistics.class,
                    SavedStatistics.count(SavedStatistics.class, null, null))
                    .getLastRefreshed());
        else
            lastref.setText("Last refreshed - never");

        return rootView;
    }

    public void fonts() {
        ((TextView) rootView.findViewById(R.id.smartbox_title))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Comix Loud.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_mostsender1))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_mostsender2))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_mostsender3))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_text1))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_text2))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_lastreftv))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));
        ((TextView) rootView.findViewById(R.id.smartbox_subtitel1))
                .setTypeface(Typeface.createFromAsset(
                        getActivity().getAssets(), "fonts/Alex_Bold.ttf"));

    }

    public void sendEmail() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse("bhardwaj.rish@gmail.com"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "bhardwaj.rish@gmail.com" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions Complaints");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi Rish!\n\nHere are a few suggestions/complaints about the webmail app : \n\n");
        getActivity().startActivity(sendIntent);
    }

}