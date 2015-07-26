package rish.crearo.dawebmail.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collections;

import rish.crearo.R;
import rish.crearo.dawebmail.EmailMessage;
import rish.crearo.dawebmail.ScrappingMachine;
import rish.crearo.dawebmail.ViewEmail;
import rish.crearo.dawebmail.analytics.LocationDetails;
import rish.crearo.dawebmail.analytics.LoginDetails;
import rish.crearo.dawebmail.analytics.ServerLoader;
import rish.crearo.dawebmail.commands.LoginListener;
import rish.crearo.dawebmail.commands.LoginManager;
import rish.crearo.tools.AlarmManagerService;
import rish.crearo.tools.Printer;
import rish.crearo.tools.RandomStrings;
import rish.crearo.utils.ColorScheme;
import rish.crearo.utils.Constants;

public class FragmentOne extends Fragment {

    String username, pwd;

    ProgressDialog progdialog;
    public static ArrayList<EmailMessage> allemails_main;
    public ArrayList<EmailMessage> emails_tobedeleted_pub;

    public static ArrayList<hash_email_checked> allemails_main_ischecked;
    boolean firstrun = true;
    private SwipeRefreshLayout swipeContainer;
    AsyncTask<String, Void, String> mAsyncLogin;
    TextView webmail_tv, pull_tv, invis_msg;
    public static AppAdapter mAdapter;
    private ListView mListView;
    public Button floatingDelete;
    int totalSelected_emails = 0;
    Animation anim_slideout;

    LoginListener loginListener;
    LoginManager loginManager;

    public FragmentOne() {
    }

    public FragmentOne(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;

        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);
    }

    private boolean getPrefs(String prefWhich) {
        SharedPreferences prefs = getActivity().getSharedPreferences(prefWhich, Context.MODE_PRIVATE);
        return prefs.getBoolean(prefWhich, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_fragment_one,
                container, false);

        appInstalled();

        anim_slideout = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);
        anim_slideout.setDuration(500);

        swipeContainer = (SwipeRefreshLayout) rootView
                .findViewById(R.id.swipeContainer);

        webmail_tv = (TextView) rootView.findViewById(R.id.webmailtv);
        webmail_tv.setTypeface(Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/helv_children.otf"));

        invis_msg = (TextView) rootView.findViewById(R.id.inbox_invisiblemsg);
        invis_msg.setTypeface(Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/helv_children.otf"));
        invis_msg.setVisibility(View.INVISIBLE);

        floatingDelete = (Button) rootView
                .findViewById(R.id.floatingbutton_del);

        swipeContainer.bringToFront();
        progdialog = new ProgressDialog(getActivity());
        allemails_main = new ArrayList<>();
        allemails_main_ischecked = new ArrayList<>();
        emails_tobedeleted_pub = new ArrayList<>();

        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        onAppOpened();

        //sending stuff to server
        if (getPrefs(Constants.prefPENDINGBIT_LOCATION) || getPrefs(Constants.prefPENDINGBIT_LOGIN)) {
            Printer.println("ServerLoading, sending jsons to server");
            ServerLoader sender = new ServerLoader(getActivity());
            sender.sendToServer();
        }

        if (((int) SugarRecord.count(EmailMessage.class, null, null) == 0)) {
            new async_refreshInbox().execute("");
        }

        loginListener = new LoginListener() {
            @Override
            public void onPreLogin() {
                progdialog = ProgressDialog.show(getActivity(), "", "Logging in.", true);
                progdialog.setCancelable(false);
            }

            @Override
            public void onPostLogin(String loginSuccess, String timeTaken) {
                if (loginSuccess.equals("login successful")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
                    Constants.isLoggedin = true;

                    getActivity().invalidateOptionsMenu();
                    swipeContainer.setRefreshing(false);
                    progdialog.dismiss();
                    new async_refreshInbox().execute("");

                    sendLoginDetails(Constants.MANUAL, Constants.TRUE, timeTaken);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    Constants.isLoggedin = false;
                    sendLoginDetails(Constants.MANUAL, Constants.FALSE, timeTaken);
                }

                getActivity().invalidateOptionsMenu();
                swipeContainer.setRefreshing(false);
                progdialog.dismiss();
            }
        };

        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if ((int) SugarRecord.count(EmailMessage.class, null, null) != 0)
                    firstrun = false;
                if (!Constants.isLoggedin) {
                    loginManager = new LoginManager(getActivity(), loginListener, username, pwd);
                    loginManager.execute();
                } else {
                    // refresh
                    new async_refreshInbox().execute("");
                    Printer.println("-----------------refreshing------------------");
                }
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light, android.R.color.darker_gray,
                android.R.color.holo_blue_dark);

        floatingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (totalSelected_emails > 0) {
                    // perform delete
                    floatingDelete.setVisibility(View.INVISIBLE);
                    totalSelected_emails = 0;
                    for (hash_email_checked item : allemails_main_ischecked) {
                        item.setIschecked(false);
                    }

                    try {
                        Toast.makeText(getActivity(), "Deleting.. The actual delete may take a minute",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Printer.println("Error in toast");
                        e.printStackTrace();
                    }

                    for (EmailMessage email : emails_tobedeleted_pub) {
                        try {
                            mListView.getChildAt(allemails_main.indexOf(email))
                                    .startAnimation(anim_slideout);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        allemails_main.remove(email);
                    }

                    mAdapter.notifyDataSetChanged();

                    new async_delete(emails_tobedeleted_pub).execute("");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        return rootView;
    }

    private void sendLoginDetails(String loginType, String success, String time) {
        LoginDetails details = new LoginDetails();
        details.setValues(getActivity(), loginType, success, time);
        ServerLoader loader = new ServerLoader(getActivity().getApplicationContext());
        loader.addLoginDetails(details);
    }

    private void appInstalled() {// to run only once when app first installed
        // sharedprefs

        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.USER_APP_TUTORIAL, getActivity().MODE_PRIVATE);

        Boolean app_tutorial = prefs.getBoolean("app_tutorial", false);
        if (app_tutorial) {
            ColorScheme colorscheme = new ColorScheme(getActivity());
            colorscheme.classicColorPreset();
            colorscheme.changeColorScheme();
        }
    }

    public void onAppOpened() {
        getActivity().getActionBar().setTitle("Inbox");
        // fetch all emails in the database and display them
        allemails_main = (ArrayList<EmailMessage>) SugarRecord.listAll(EmailMessage.class);
        Collections.reverse(allemails_main);

        for (int i = 0; i < allemails_main.size(); i++)
            allemails_main_ischecked.add(new hash_email_checked(i, false));

        mAdapter.notifyDataSetChanged();
    }

    public class async_refreshInbox extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scrapper = new ScrappingMachine(username, pwd, getActivity());
            scrapper.scrapeAllMessagesfromInbox(firstrun);
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            // showDialog(progress_bar_type);
            // "Please wait while we load your content.
            progdialog = ProgressDialog.show(getActivity(), "",
                    "Please wait while we load your content.", true);
            progdialog.setCancelable(false);
            if (((int) SugarRecord.count(EmailMessage.class, null, null) == 0)) {
                progdialog.setCancelable(true);
                invis_msg.setVisibility(View.VISIBLE);
                invis_msg.bringToFront();
            }
        }

        @Override
        public void onPostExecute(String result) {
            // get all e-mails from the temp array list made in Scraping Machine
            // and save the data here, in the main thread.

            invis_msg.setVisibility(View.INVISIBLE);

            RandomStrings.setRandomText();
            webmail_tv.setText(Constants.RandomStringText);

            Collections.reverse(ScrappingMachine.allemails);

            for (EmailMessage m : ScrappingMachine.allemails)
                m.save();// now all e-mails are in the database

            // allemails_main creates a temporary arraylist of all emails in

            allemails_main = (ArrayList<EmailMessage>) SugarRecord
                    .listAll(EmailMessage.class);// fetch all emails from the
            // database
            Collections.reverse(allemails_main);

            ScrappingMachine.clear_AllEmailsAL();

            for (int i = 0; i < allemails_main.size(); i++)
                allemails_main_ischecked.add(new hash_email_checked(i, false));

            // inboxadapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
            if (ScrappingMachine.totalnew == 0)
                Toast.makeText(getActivity().getApplicationContext(),
                        "No new webmail.", Toast.LENGTH_SHORT).show();
            else if (ScrappingMachine.totalnew == 1)
                Toast.makeText(getActivity().getApplicationContext(),
                        ScrappingMachine.totalnew + " new webmail!",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(),
                        ScrappingMachine.totalnew + " new webmails!",
                        Toast.LENGTH_SHORT).show();

            progdialog.dismiss();
            swipeContainer.setRefreshing(false);

            if (firstrun == true) {
                AlarmManagerService.setBackgrndService(getActivity());
            }
        }
    }


    public class async_delete extends AsyncTask<String, Void, String> {
        ArrayList<EmailMessage> emails_tobedeleted = new ArrayList<>();

        public async_delete(ArrayList<EmailMessage> emails_tobedeleted) {
            this.emails_tobedeleted = emails_tobedeleted;
        }

        @Override
        protected void onPreExecute() {
            Printer.println("Starting delete");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            Printer.println("Allemailsmain size " + FragmentOne.allemails_main.size());
            sendLocationDetails();
            try {
                new ScrappingMachine(username, pwd, getActivity())
                        .getValues_forDelete(emails_tobedeleted);
            } catch (Exception e) {
                Printer.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Printer.println("Stopping delete");
            try {
                Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Printer.println("error in deleting");
                e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    private void sendLocationDetails() {
        LocationDetails details = new LocationDetails();
        details.setValue(getActivity().getApplicationContext());
        ServerLoader loader = new ServerLoader(getActivity());
        loader.addLocationDetails(details);
    }

    // for class adapter of the list.
    public class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allemails_main.size();
        }

        @Override
        public EmailMessage getItem(int position) {
            return allemails_main.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity()
                        .getApplicationContext(), R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            EmailMessage item = getItem(position);

            if (totalSelected_emails == 0) {
                floatingDelete.setVisibility(View.INVISIBLE);
            } else if (totalSelected_emails == 1) {

            } else {
                floatingDelete.bringToFront();
                floatingDelete.setVisibility(View.VISIBLE);
            }

            holder.tv_name.setTextSize(15);

            // holder.tv_date.setTextColor(Color
            // .parseColor(ColorScheme.color_unreadtextsubject));

            if (item.readunread.equals("Unread Message")) {
                Printer.println("has att - " + item.attlink1);
                if (item.attlink1.equals("isempty"))
                    holder.iv_icon
                            .setBackgroundResource(R.drawable.final_unread);
                else
                    holder.iv_icon
                            .setBackgroundResource(R.drawable.final_unread_a);
                if (allemails_main_ischecked.get(position).getIschecked()) {
                    holder.iv_icon.setBackgroundResource(R.drawable.ic_tick);
                }

                holder.tv_relaLayout.setBackgroundColor(Color
                        .parseColor(ColorScheme.color_unreadmessage));
                holder.tv_name.setTextColor(Color
                        .parseColor(ColorScheme.color_unreadtextsender));
                holder.tv_subject.setTextColor(Color
                        .parseColor(ColorScheme.color_unreadtextsubject));
                holder.tv_date.setTextColor(Color
                        .parseColor(ColorScheme.color_unreadtextsubject));
                holder.tv_name.setTypeface(null, Typeface.BOLD);
            } else {
                if (item.attlink1.equals("isempty"))
                    holder.iv_icon.setBackgroundResource(R.drawable.final_read);
                else
                    holder.iv_icon
                            .setBackgroundResource(R.drawable.final_read_a);
                if (allemails_main_ischecked.get(position).getIschecked()) {
                    holder.iv_icon.setBackgroundResource(R.drawable.ic_tick);
                }
                holder.tv_relaLayout.setBackgroundColor(Color
                        .parseColor(ColorScheme.color_readmessage));
                holder.tv_name.setTextColor(Color
                        .parseColor(ColorScheme.color_readtextsender));
                holder.tv_subject.setTextColor(Color
                        .parseColor(ColorScheme.color_readtextsubject));
                holder.tv_date.setTextColor(Color
                        .parseColor(ColorScheme.color_readtextsubject));
                holder.tv_name.setTypeface(null, Typeface.NORMAL);
            }
            holder.tv_name.setText(item.fromname);
            holder.tv_date.setText(item.date);
            holder.tv_subject.setText(item.subject);

            // set all the onclick listeners to the holder!

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View arg0) {
                    Printer.println("Long click");
                    allemails_main_ischecked.get(position).setIschecked(true);
                    mAdapter.notifyDataSetChanged();
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(
                            Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);

                    if (totalSelected_emails++ == 0) {
                        floatingDelete.bringToFront();
                        floatingDelete.setVisibility(View.VISIBLE);
                        AnimationSet set = new AnimationSet(true);
                        Animation translate = new TranslateAnimation(100, 0, 0,
                                0);
                        translate.setDuration(300);
                        set.addAnimation(translate);
                        floatingDelete.startAnimation(set);

                        emails_tobedeleted_pub.add(allemails_main.get(position));
                    }

                    return true;
                }
            });

            holder.iv_icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Printer.println("Clicked ! " + position);

                    if (allemails_main_ischecked.get(position).getIschecked()) {
                        allemails_main_ischecked.get(position).setIschecked(
                                false);

                        emails_tobedeleted_pub.remove(allemails_main
                                .get(position));
                        if (--totalSelected_emails == 0) {
                            floatingDelete.bringToFront();
                            AnimationSet set = new AnimationSet(true);
                            Animation translate = new TranslateAnimation(0,
                                    100, 0, 0);
                            translate.setDuration(300);
                            set.addAnimation(translate);
                            floatingDelete.startAnimation(set);
                            floatingDelete.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        allemails_main_ischecked.get(position).setIschecked(
                                true);

                        if (totalSelected_emails++ == 0) {
                            floatingDelete.bringToFront();
                            floatingDelete.setVisibility(View.VISIBLE);
                            AnimationSet set = new AnimationSet(true);
                            Animation translate = new TranslateAnimation(100,
                                    0, 0, 0);
                            translate.setDuration(300);
                            set.addAnimation(translate);
                            floatingDelete.startAnimation(set);

                        }
                        emails_tobedeleted_pub.add(allemails_main.get(position));
                    }
                    Printer.println(allemails_main_ischecked.get(position).ischecked
                            + ", totalselected =  " + totalSelected_emails);

                    mAdapter.notifyDataSetChanged();

                }
            });

            holder.tv_relaLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewEmail nextFrag = new ViewEmail(username, pwd, allemails_main.get(position));
                    getFragmentManager().beginTransaction().add(R.id.content_frame, nextFrag)
                            .addToBackStack(null).commit();
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name, tv_subject, tv_date;
            LinearLayout tv_relaLayout;

            public ViewHolder(View view) {
                tv_relaLayout = (LinearLayout) view
                        .findViewById(R.id.tv_relativelayout);
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
                tv_subject = (TextView) view.findViewById(R.id.tv_subject);

                Typeface font = Typeface.createFromAsset(getActivity()
                        .getAssets(), "fonts/GeosansLight.ttf");

                tv_date.setTypeface(font);
                tv_name.setTypeface(font);
                tv_subject.setTypeface(font);

                view.setTag(this);
            }
        }
    }

    private class hash_email_checked {
        Boolean ischecked;
        int position;

        public hash_email_checked(int pos, Boolean isch) {
            this.position = pos;
            this.ischecked = isch;

        }

        public Boolean getIschecked() {
            return ischecked;
        }

        public int getPosition() {
            return position;
        }

        public void setIschecked(Boolean ischecked) {
            this.ischecked = ischecked;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}