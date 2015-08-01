package com.sigmobile.dawebmail;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.drakeet.materialdialog.MaterialDialog;

import com.sigmobile.R;
import com.sigmobile.dawebmail.analytics.ServerLoader;
import com.sigmobile.dawebmail.commands.LoginListener;
import com.sigmobile.dawebmail.commands.LoginManager;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.Constants;

public class ViewEmail extends Fragment {

    EmailMessage currentemail;
    TextView tvsender, tvsubject, tvsenderbottom, tvdatebottom;
    ImageView att1, att2, att3;
    LinearLayout attll1, attll2, attll3;
    TextView tvatt;
    String username, pwd;
    WebView webView_viewContent;

    public ViewEmail() {

    }

    @SuppressLint("ValidFragment")
    public ViewEmail(String un, String pw, EmailMessage currentEmail) {
        this.currentemail = currentEmail;
        this.username = un;
        this.pwd = pw;
    }

    ProgressDialog progdialog;
    LoginManager loginManager;
    LoginListener loginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_view_email, container, false);

        tvsender = (TextView) rootView.findViewById(R.id.viewmail_sender);
        tvsubject = (TextView) rootView.findViewById(R.id.viewmail_subject);
        tvsenderbottom = (TextView) rootView.findViewById(R.id.viewmail_senderbottom);
        tvdatebottom = (TextView) rootView.findViewById(R.id.viewmail_datebottom);
        att1 = (ImageView) rootView.findViewById(R.id.viewemail_attachment1);
        att2 = (ImageView) rootView.findViewById(R.id.viewemail_attachment2);
        att3 = (ImageView) rootView.findViewById(R.id.viewemail_attachment3);

        attll1 = (LinearLayout) rootView.findViewById(R.id.viewemail_attll1);
        attll2 = (LinearLayout) rootView.findViewById(R.id.viewemail_attll2);
        attll3 = (LinearLayout) rootView.findViewById(R.id.viewemail_attll3);
        tvatt = (TextView) rootView.findViewById(R.id.viewemail_attatchment1_tv);

        webView_viewContent = (WebView) rootView.findViewById(R.id.viewmail_webview);
        webView_viewContent.setBackgroundColor(Color.parseColor("#E7E7E7"));

        attll1.setBackgroundColor(Color.parseColor("#EEEEEE"));
        attll2.setBackgroundColor(Color.parseColor("#EEEEEE"));
        attll3.setBackgroundColor(Color.parseColor("#EEEEEE"));

        attll1.setVisibility(View.GONE);
        attll2.setVisibility(View.GONE);
        attll3.setVisibility(View.GONE);

        showEmailAttachments();

        attll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createDownloadDialog(1);
            }
        });
        attll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createDownloadDialog(2);
            }
        });
        attll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createDownloadDialog(3);
            }
        });

        progdialog = new ProgressDialog(getActivity());

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
                    progdialog.dismiss();
                    Constants.isLoggedin = true;
                    new ServerLoader(getActivity()).addActionDetails(username, Constants.ACTION_VIEWEMAIL, timeTaken, Constants.TRUE);
                    new async_ViewEmail().execute("");

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    Constants.isLoggedin = false;
                    progdialog.dismiss();
                    new ServerLoader(getActivity()).addActionDetails(username, Constants.ACTION_VIEWEMAIL, timeTaken, Constants.FALSE);
                }
                getActivity().invalidateOptionsMenu();
            }
        };

        if (currentemail.content.equals("isempty")) {
            // load content only if logged in.
            setWebviewContent("<html><head></head><body>Connect to the Internet to download content</body></html>");
            tvdatebottom.setText("" + currentemail.date);
            tvsender.setText("" + currentemail.fromname);
            tvsenderbottom.setText("");
            tvsubject.setText("" + currentemail.subject);

            if (ScrappingMachine.homepage_link.equals("")) {
                loginManager = new LoginManager(getActivity(), loginListener, username, pwd);
                loginManager.execute();
            } else {
                new async_ViewEmail().execute("");
            }
        } else {
            Printer.println("fromname - " + currentemail.fromname);
            Printer.println("fromadd - " + currentemail.fromaddress);

            if (currentemail.fromname.contains("...") && (!currentemail.fromaddress.equals("isempty")))
                tvsender.setText(currentemail.fromaddress);
            else
                tvsender.setText(currentemail.fromaddress);

            if (!(currentemail.subjectfull.equals("isempty")))
                tvsubject.setText(currentemail.subjectfull);
            else
                tvsubject.setText(currentemail.subject);

            if (currentemail.content.equals("isempty")) {
                setWebviewContent("<html><head></head><body>Connect to the Internet to download content</body></html>");
            } else {
                setWebviewContent(currentemail.content);
            }
            tvsenderbottom.setText(currentemail.fromname);
            tvdatebottom.setText(currentemail.dateentire);
        }

        return rootView;
    }

    public class async_ViewEmail extends AsyncTask<String, Void, String> {

        long timeStarted = 0, timeFinished = 0;

        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scrapper = new ScrappingMachine(username, pwd, getActivity());
            scrapper.fetchEmailContent(currentemail);
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            progdialog = ProgressDialog.show(getActivity(), "", "Fetching Content.", true);
            progdialog.setCancelable(false);
            timeStarted = System.currentTimeMillis();
        }

        @Override
        public void onPostExecute(String result) {
            timeFinished = System.currentTimeMillis();
            long timeTaken = timeFinished - timeStarted;
            new ServerLoader(getActivity()).addActionDetails(username, Constants.ACTION_VIEWEMAIL, "" + timeTaken, Constants.TRUE);
            tvsender.setText(currentemail.fromaddress);
            setWebviewContent(currentemail.content);
            tvsubject.setText(currentemail.subject);
            tvsenderbottom.setText(currentemail.fromname);
            tvdatebottom.setText(currentemail.dateentire);
            showEmailAttachments();
            progdialog.dismiss();
        }
    }


    public void downloadAttachment(String link, int attnumber) {

        Printer.println("link = " + link);

        if (!(link.equals("isempty") || link.equals("notempty"))) {
            link = link.substring(0, link.indexOf("&auth"));
            Printer.println(link);
            String webpage = link;

            String authString = username + ":" + pwd;
            Printer.println("auth string: " + authString);
            String authStringEnc = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);

            Printer.println("Base64 encoded auth string: " + authStringEnc);
            DownloadManager downloadManager = (DownloadManager) getActivity().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(webpage));
            request.addRequestHeader("Authorization", "Basic " + authStringEnc);
            request.setTitle(currentemail.subject + "_Attachment_" + attnumber);
            request.setDestinationInExternalFilesDir(getActivity().getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,
                    currentemail.subject + "_Attachment_" + attnumber);
            downloadManager.enqueue(request);

            // open downloads direct.
            new ServerLoader(getActivity()).addActionDetails(username, Constants.ACTION_DOWNLOADATT, "0", Constants.TRUE);
            startActivity(new Intent().setAction(DownloadManager.ACTION_VIEW_DOWNLOADS));
        }
    }


    public void createDownloadDialog(final int whichattatchment) {
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog.setTitle("Download the attachment?");
        if (isConnectedByMobileData())
            materialDialog.setMessage("You are conected over mobile network.");
        if (isConnectedByWifi())
            materialDialog.setMessage("You are conected over wifi network.");

        materialDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });

        materialDialog.setPositiveButton("Download", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                if (whichattatchment == 1)
                    downloadAttachment(currentemail.attlink1, 1);
                if (whichattatchment == 2)
                    downloadAttachment(currentemail.attlink2, 2);
                if (whichattatchment == 3)
                    downloadAttachment(currentemail.attlink3, 3);

            }
        });
        materialDialog.show();

    }

    public Boolean isConnectedByWifi() {
        if (((ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isConnectedByMobileData() {
        if (((ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void showEmailAttachments() {
        if (currentemail.attlink1.equals("isempty")) {
            attll1.setVisibility(View.GONE);
            attll2.setVisibility(View.GONE);
            attll3.setVisibility(View.GONE);
        } else if (currentemail.attlink2.equals("isempty")) {
            attll1.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setVisibility(View.GONE);
            attll3.setVisibility(View.GONE);
        } else if (currentemail.attlink3.equals("isempty")) {
            attll1.setVisibility(View.VISIBLE);
            attll2.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll3.setVisibility(View.GONE);
        } else {
            attll1.setVisibility(View.VISIBLE);
            attll2.setVisibility(View.VISIBLE);
            attll3.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll3.setBackgroundColor(Color.parseColor("#E7E7E7"));
        }
    }

    public void setWebviewContent(String html) {
        String mime = "text/html";
        String encoding = "utf-8";

        webView_viewContent.getSettings().setJavaScriptEnabled(true);
        webView_viewContent.loadDataWithBaseURL(null, html, mime, encoding, null);

    }
}