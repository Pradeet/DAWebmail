package rish.crearo.dawebmail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import rish.crearo.R;
import rish.crearo.dawebmail.commands.LoginListener;
import rish.crearo.dawebmail.commands.LoginManager;
import rish.crearo.utils.Constants;

public class ViewEmail extends Fragment {

    EmailMessage currentemail;
    TextView tvsender, tvsubject, tvcontent, tvsenderbottom, tvdatebottom;
    ImageView att1, att2, att3;
    LinearLayout attll1, attll2, attll3;
    TextView tvatt;
    String username, pwd;

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
        tvcontent = (TextView) rootView.findViewById(R.id.viewmail_content);
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

        attll1.setBackgroundColor(Color.parseColor("#EEEEEE"));
        attll2.setBackgroundColor(Color.parseColor("#EEEEEE"));
        attll3.setBackgroundColor(Color.parseColor("#EEEEEE"));

        attll1.setVisibility(View.INVISIBLE);
        attll2.setVisibility(View.INVISIBLE);
        attll3.setVisibility(View.INVISIBLE);

        showEmailAttachments();

        attll1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogFragment newFrag = new MyAlertDialogFragment_download(1);
                newFrag.show(getFragmentManager(), "att1");

                System.out.println("File Downloading.");
            }
        });
        attll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFrag = new MyAlertDialogFragment_download(2);
                newFrag.show(getFragmentManager(), "att2");
            }
        });
        attll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFrag = new MyAlertDialogFragment_download(3);
                newFrag.show(getFragmentManager(), "att3");
            }
        });

        // ActionBar actionBar = getActivity().getActionBar();
        // actionBar.setTitle("" + currentemail.fromname);

        progdialog = new ProgressDialog(getActivity());

        loginListener = new LoginListener() {
            @Override
            public void onPreLogin() {
                progdialog = ProgressDialog.show(getActivity(), "", "Logging in.", true);
                progdialog.setCancelable(false);
            }

            @Override
            public void onPostLogin(String loginSuccess) {

                if (loginSuccess.equals("login successful")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
                    progdialog.dismiss();
                    Constants.isLoggedin = true;
                    new async_ViewEmail().execute("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    Constants.isLoggedin = false;
                    progdialog.dismiss();
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (currentemail.content.equals("isempty")) {
            // load content only if logged in.
            tvcontent.setText("\n\nConnect to the Internet to download content.");
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
            System.out.println("fromname - " + currentemail.fromname);
            System.out.println("fromadd - " + currentemail.fromaddress);

            if (currentemail.fromname.contains("...") && (!currentemail.fromaddress.equals("isempty")))
                tvsender.setText(currentemail.fromaddress);
            else
                tvsender.setText(currentemail.fromaddress);

            if (!(currentemail.subjectfull.equals("isempty")))
                tvsubject.setText(currentemail.subjectfull);
            else
                tvsubject.setText(currentemail.subject);

            if (currentemail.content.equals("isempty"))
                tvcontent.setText("Connect to internet to download content.");
            else {
                // -- ATTEMPTED TO SET LINK --
                // String contentinHTML = currentemail.content;
                // int index = contentinHTML.indexOf("</style>");
                // Log.d("testing", index + "");
                // if (index != -1)
                // contentinHTML = contentinHTML.substring(0, index)
                // + contentinHTML.substring(index + 8);
                // Log.d("testing", index + "");
                // Log.d("testing", "This is final HTML text\n" +
                // contentinHTML);
                // tvcontent.setText(Html.fromHtml(contentinHTML));
                // -- FAILED ATTEMPT --
                tvcontent.setText(currentemail.content);
            }
            tvsenderbottom.setText(currentemail.fromname);
            tvdatebottom.setText(currentemail.dateentire);

            tvcontent.setClickable(true);
            tvcontent.setMovementMethod(LinkMovementMethod.getInstance());
            // String content = tvcontent.getText().toString();
            // int indexoflink = content.indexOf("http");
            // // while (indexoflink != -1) {
            // if (indexoflink != -1) {
            // int stopindex = content.substring(indexoflink).indexOf(" ")
            // + indexoflink;
            // if (stopindex == -1)
            // stopindex = content.substring(indexoflink).indexOf("\n")
            // + indexoflink;
            // if (stopindex == -1)
            // stopindex = content.substring(indexoflink).length() - 1;
            // System.out.println("\n\n+" + indexoflink + " " + stopindex);
            // String httpLink = "<br>Click on this <a href=\""
            // + content.substring(indexoflink, stopindex)
            // + "\"> link </a><br>";
            // System.out.println("httplink - " + httpLink);
            //
            // content = content.substring(0, indexoflink)
            // + httpLink
            // + content
            // .substring(stopindex + 1, content.length() - 1);
            //
            // // System.out.println("Content - " + content);
            // // indexoflink = content.substring(content.indexOf("</a>"))
            // // .indexOf("http");
            // // System.out.println("Now content - " + content);
            // tvcontent.setText(Html.fromHtml(content));
            // }
        }

        return rootView;
    }

    public class async_ViewEmail extends AsyncTask<String, Void, String> {
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
        }

        @Override
        public void onPostExecute(String result) {

            tvsender.setText(currentemail.fromaddress);
            tvcontent.setText(currentemail.content);
            tvsubject.setText(currentemail.subject);
            tvsenderbottom.setText(currentemail.fromname);
            tvdatebottom.setText(currentemail.dateentire);
            showEmailAttachments();

            progdialog.dismiss();
        }
    }

    public void downloadAttachment(String link, int attnumber) {

        System.out.println("link = " + link);

        if (!(link.equals("isempty") || link.equals("notempty"))) {
            link = link.substring(0, link.indexOf("&auth"));
            System.out.println(link);
            String webpage = link;

            String authString = username + ":" + pwd;
            System.out.println("auth string: " + authString);
            String authStringEnc = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);

            System.out.println("Base64 encoded auth string: " + authStringEnc);
            DownloadManager downloadManager = (DownloadManager) getActivity().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(webpage));
            request.addRequestHeader("Authorization", "Basic " + authStringEnc);
            request.setTitle(currentemail.subject + "_Attachment_" + attnumber);
            request.setDestinationInExternalFilesDir(getActivity().getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,
                    currentemail.subject + "_Attachment_" + attnumber);
            downloadManager.enqueue(request);

            // open downloads direct.
            startActivity(new Intent().setAction(DownloadManager.ACTION_VIEW_DOWNLOADS));

        }
    }

    public class MyAlertDialogFragment_download extends DialogFragment {
        int whichattatchment;

        public MyAlertDialogFragment_download(int whichattatchment) {
            this.whichattatchment = whichattatchment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());

            myDialog.setTitle("Download the attachment?");
            if (isConnectedByMobileData())
                myDialog.setMessage("You are conected over mobile network.");
            if (isConnectedByWifi())
                myDialog.setMessage("You are conected over wifi network.");

            myDialog.setIcon(getResources().getDrawable(R.drawable.ic_action_locate));
            myDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            myDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            if (whichattatchment == 1)
                                downloadAttachment(currentemail.attlink1, 1);
                            if (whichattatchment == 2)
                                downloadAttachment(currentemail.attlink2, 2);
                            if (whichattatchment == 3)
                                downloadAttachment(currentemail.attlink3, 3);

                        }
                    });
            return myDialog.create();
        }
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
            attll1.setVisibility(View.INVISIBLE);
            attll2.setVisibility(View.INVISIBLE);
            attll3.setVisibility(View.INVISIBLE);
        } else if (currentemail.attlink2.equals("isempty")) {
            attll1.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setVisibility(View.INVISIBLE);
            attll3.setVisibility(View.INVISIBLE);
        } else if (currentemail.attlink3.equals("isempty")) {
            attll1.setVisibility(View.VISIBLE);
            attll2.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll3.setVisibility(View.INVISIBLE);
        } else {
            attll1.setVisibility(View.VISIBLE);
            attll2.setVisibility(View.VISIBLE);
            attll3.setVisibility(View.VISIBLE);
            attll1.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll2.setBackgroundColor(Color.parseColor("#E7E7E7"));
            attll3.setBackgroundColor(Color.parseColor("#E7E7E7"));
        }
    }
}