package com.sigmobile.dawebmail.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.sigmobile.R;
import com.sigmobile.dawebmail.Adapters.SentListAdapter;
import com.sigmobile.dawebmail.EmailMessage;
import com.sigmobile.dawebmail.ScrappingMachine;
import com.sigmobile.dawebmail.SentEmailMessage;
import com.sigmobile.dawebmail.analytics.ServerLoader;
import com.sigmobile.dawebmail.commands.LoginListener;
import com.sigmobile.dawebmail.commands.LoginManager;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.Constants;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class SentFragment extends Fragment {

    String username, pwd;
    int totalSelected_emails = 0;

    public ProgressDialog progdialog;
    ActionButton deleteButton;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView sentList;

    LoginListener loginListener;
    LoginManager loginManager;

    ArrayList<SentEmailMessage> sentEmailMessages;

    public SentFragment() {
        // Required empty public constructor
    }

    public SentFragment(String pwd, String username) {
        this.pwd = pwd;
        this.username = username;
//        if (mAdapter != null)
//            mAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sent, container, false);

        deleteButton = (ActionButton) rootView.findViewById(R.id.delete_button);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.DeleteSwipeContainer);
        sentList = (ListView) rootView.findViewById(R.id.DeleteListView);
        progdialog = new ProgressDialog(getActivity());

        sentEmailMessages = new ArrayList<>();

        SentListAdapter adapter = new SentListAdapter();
        sentList.setAdapter(adapter);
        sentList.setBackgroundColor(Color.parseColor("#FFFFFF"));
        adapter.notifyDataSetChanged();

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
                    swipeRefreshLayout.setRefreshing(false);
                    progdialog.dismiss();
                    new async_refreshSent().execute("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    Constants.isLoggedin = false;
                }

                getActivity().invalidateOptionsMenu();
                swipeRefreshLayout.setRefreshing(false);
                progdialog.dismiss();
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Constants.isLoggedin) {
                    loginManager = new LoginManager(getActivity(), loginListener, username, pwd);
                    loginManager.execute();
                } else {
                    // refresh
                    new async_refreshSent().execute("");
                    Printer.println("-----------------refreshing------------------");
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light, android.R.color.darker_gray,
                android.R.color.holo_blue_dark);

        return rootView;
    }

    public class async_refreshSent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scrapper = new ScrappingMachine(username, pwd, getActivity());
            scrapper.scrapeAllMessagesfromSentbox();
            return "Excecuted";
        }

        @Override
        protected void onPreExecute() {
            progdialog = ProgressDialog.show(getActivity(), "", "Please wait while we load your content.", true);
            progdialog.setCancelable(false);
            if (SugarRecord.count(SentEmailMessage.class, null, null) == 0){
                progdialog.setCancelable(true);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Collections.reverse(ScrappingMachine.allemails);

            for (EmailMessage m : ScrappingMachine.allemails)
                m.save();

//            sentEmailMessages = SentEmailMessage.listAll(SentEmailMessage.class);
        }
    }

}
