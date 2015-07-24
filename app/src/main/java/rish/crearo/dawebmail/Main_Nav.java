package rish.crearo.dawebmail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import rish.crearo.R;
import rish.crearo.dawebmail.fragments.FragmentOne;
import rish.crearo.dawebmail.fragments.FragmentThree;
import rish.crearo.dawebmail.fragments.FragmentTwo;
import rish.crearo.dialogs.SplashDialog1;
import rish.crearo.services.NetworkChangeBroadcastReceiver;
import rish.crearo.tools.SearchWatcher;
import rish.crearo.utils.ColorScheme;
import rish.crearo.utils.Constants;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

public class Main_Nav extends FragmentActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    ActionBarDrawerToggle mDrawerToggle;
    ProgressDialog prgDialog;
    public static final int progress_bar_login = 0;
    static public Activity activity;
    static int exithere = 0;
    private boolean doubleBackToExitPressedOnce = false;
    public ProgressDialog progdialog;
    private boolean mSearchOpened;
    public static String mSearchQuery;
    public static EditText mSearchEt;
    private MenuItem mSearchAction;
    private Drawable mIconCloseSearch;
    private Drawable mIconOpenSearch;

    private String username, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__nav);

        jauntValidity();

        username = getIntent().getExtras().getString(Constants.bundle_username);
        pwd = getIntent().getExtras().getString(Constants.bundle_pwd);

        mSearchOpened = false;
        mSearchQuery = "";
        mIconOpenSearch = getResources().getDrawable(
                R.drawable.ic_action_search);
        mIconCloseSearch = getResources().getDrawable(
                R.drawable.ic_action_close);
        // changing the color of the text in actionbar
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        progdialog = new ProgressDialog(getApplicationContext());

        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/helv_children.otf"));
        abTitle.setTextColor(Color
                .parseColor(ColorScheme.color_actionbartextcolor));
        invalidateOptionsMenu();

        activity = this;
        // action bar color
        ColorDrawable actionbarcolor = new ColorDrawable(
                Color.parseColor(ColorScheme.color_actionbarcolor));
        getActionBar().setBackgroundDrawable(actionbarcolor);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(
                        android.R.color.transparent)));

        mNavigationDrawerItemTitles = getResources().getStringArray(
                R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // mDrawerList.setBackgroundColor(Color.parseColor("#CC0F0F0F"));
        Drawable navdrawerbackground = getResources().getDrawable(
                R.drawable.bg_login);
        navdrawerbackground.setAlpha(220);
        mDrawerList.setBackgroundDrawable(navdrawerbackground);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mDrawerTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("DAWebmail");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_user_edited,
                "" + username);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_mail, "Inbox");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_send,
                "SMARTbox");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.settings_new,
                "Settings");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
                R.layout.navdraw_listitem, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerTitle = getTitle();

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            selectItem(1);
        }
    }

    private void jauntValidity() {
        // if Jaunt expires, =>
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date expiryDate = dateFormat.parse("31/07/2015");
            Date todaysDate = Calendar.getInstance().getTime();
            String todaysDate_string = dateFormat.format(todaysDate);

            System.out.println("today - " + todaysDate_string);
            System.out.println("dead - " + expiryDate);

            todaysDate = dateFormat.parse(todaysDate_string);
            if (todaysDate.after(expiryDate)) {
                System.out.println("after");
                SplashDialog1.cdd = new SplashDialog1(Main_Nav.this, 7);
                SplashDialog1.cdd.setCancelable(false);
                SplashDialog1.cdd.show();
            }
        } catch (Exception e) {
            System.out.println("e!");
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        FragmentOne.mAdapter.notifyDataSetChanged();
        invalidateOptionsMenu();

        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                System.out.println("Popping backstack");
                getFragmentManager().popBackStack();
            } else {
                mDrawerList.setItemChecked(1, true);
                mDrawerList.setSelection(1);
                mDrawerLayout.closeDrawer(mDrawerList);

                FragmentManager fm = getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                FragmentOne x = new FragmentOne(username, pwd);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, x).commit();

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit.",
                        Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

        Context mContext;
        int layoutResourceId;
        ObjectDrawerItem data[] = null;

        public DrawerItemCustomAdapter(Context mContext, int layoutResourceId,
                                       ObjectDrawerItem[] data) {

            super(mContext, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            listItem = inflater.inflate(layoutResourceId, parent, false);

            ImageView imageViewIcon = (ImageView) listItem
                    .findViewById(R.id.navdraw_image);
            TextView textViewName = (TextView) listItem
                    .findViewById(R.id.navdraw_textView);

            ObjectDrawerItem folder = data[position];

            imageViewIcon.setImageResource(folder.icon);
            textViewName.setText(folder.name);

            return listItem;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        ColorDrawable actionbarcolor = new ColorDrawable(
                Color.parseColor(ColorScheme.color_actionbarcolor));
        getActionBar().setBackgroundDrawable(actionbarcolor);
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(Color
                .parseColor(ColorScheme.color_actionbartextcolor));
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                showDialog();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, new FragmentOne(username, pwd))
                        .commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 1:
                fragment = new FragmentOne(username, pwd);
                break;
            case 2:
                fragment = new FragmentTwo();
                break;
            case 3:
                fragment = new FragmentThree(username, pwd);
                break;

            default:
                break;
        }
        if (fragment != null) {
            // here fragment is being changed. So every time it is changed i
            // will update the user icon.
            // updateUserStatus(false);
            invalidateOptionsMenu();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
            fragmentManager.popBackStack();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerTitle = mNavigationDrawerItemTitles[position];
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        // SearchManager searchManager = (SearchManager)
        // getSystemService(Context.SEARCH_SERVICE);
        // SearchView searchView = (SearchView)
        // menu.findItem(R.id.action_search)
        // .getActionView();
        // searchView.setSearchableInfo(searchManager
        // .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.action_composemail:
                // new Statistics();
                SplashDialog1.cdd = new SplashDialog1(Main_Nav.this, 1);
                SplashDialog1.cdd.setCancelable(false);
                SplashDialog1.cdd.show();

                invalidateOptionsMenu();
                return true;
            case R.id.action_settings:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                new FragmentThree(username, pwd)).commit();
                return true;
            case R.id.action_aboutme:
                startActivity(new Intent(Main_Nav.this, AboutFrag.class));
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_masterrefresh:
                new MyAlertDialogFragment_MasterRefresh().show(
                        getFragmentManager(), "masterrefreshdialog");
                return true;
            case R.id.action_search:
                if (mSearchOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar(mSearchQuery);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        // Search edit text field setup.
        mSearchEt = (EditText) actionBar.getCustomView().findViewById(
                R.id.etSearch);
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setText(queryText);
        mSearchEt.requestFocus();
        // open the soft keyboard
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Change search icon accordingly.
        mSearchAction.setIcon(mIconCloseSearch);
        mSearchOpened = true;

    }

    private void closeSearchBar() {

        // Remove custom view.
        getActionBar().setDisplayShowCustomEnabled(false);

        // close the soft keyboard
        mSearchEt.clearFocus();
        mSearchAction.collapseActionView();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
        mSearchEt.setText("");

        // Change search icon accordingly.
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened = false;

    }

    void showDialog() {
        DialogFragment newFrag = new MyAlertDialogFragment();
        newFrag.show(getFragmentManager(), "dialog");
    }

    static public class MyAlertDialogFragment extends DialogFragment {

        public MyAlertDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(
                    getActivity());

            if (ScrappingMachine.checkIfLoggedIn()) {
                myDialog.setMessage("You are currently online :)");
            } else {
                myDialog.setMessage("You are currently offline.");
            }

            myDialog.setNegativeButton("Okay!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                        }
                    });
            return myDialog.create();
        }
    }

    public class MyAlertDialogFragment_MasterRefresh extends DialogFragment {
        public MyAlertDialogFragment_MasterRefresh() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(
                    getActivity());

            myDialog.setTitle("Master Refresh");
            myDialog.setMessage("Is the app faulty somewhere? Go ahead and master refresh.");
            myDialog.setIcon(getResources().getDrawable(
                    R.drawable.ic_action_overflow));
            myDialog.setNegativeButton("No!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                        }
                    });
            myDialog.setPositiveButton("Refresh",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // have to check if connected to data or not and
                            // only then run the process.

                            if (((ConnectivityManager) getApplicationContext()
                                    .getSystemService(
                                            Context.CONNECTIVITY_SERVICE))
                                    .getNetworkInfo(
                                            ConnectivityManager.TYPE_MOBILE)
                                    .isConnectedOrConnecting()
                                    || ((ConnectivityManager) getApplicationContext()
                                    .getSystemService(
                                            Context.CONNECTIVITY_SERVICE))
                                    .getNetworkInfo(
                                            ConnectivityManager.TYPE_WIFI)
                                    .isConnectedOrConnecting()) {
                                new async_MasterRefresh().execute("");
                            } else
                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        "Connect to the net for master refresh",
                                        Toast.LENGTH_SHORT).show();
                        }
                    });
            return myDialog.create();
        }
    }

    public class async_MasterRefresh extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progdialog = ProgressDialog.show(Main_Nav.this, "",
                    "Master Refresh In Progress.", true);
            progdialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            ScrappingMachine scrapper = new ScrappingMachine(username, pwd,
                    getApplicationContext());
            scrapper.masterRefresh();
            if (ScrappingMachine.checkifLoggedInLong()) {
                scrapper.scrapeAllMessagesfromInbox(true);
            } else {
                scrapper.logIn(username, pwd);
                scrapper.scrapeAllMessagesfromInbox(true);
            }
            return "Executed";
        }

        protected void onProgressUpdate(String... progress) {
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        public void onPostExecute(String result) {
            invalidateOptionsMenu();
            Collections.reverse(ScrappingMachine.allemails);
            for (EmailMessage m : ScrappingMachine.allemails)
                m.save();// now all e-mails are in the database

            // allemails_main creates a temporary arraylist of all emails in
            FragmentOne.allemails_main = (ArrayList<EmailMessage>) SugarRecord
                    .listAll(EmailMessage.class); // fetch all emails from the
            // database

            Collections.reverse(FragmentOne.allemails_main);
            ScrappingMachine.clear_AllEmailsAL();
            FragmentOne.mAdapter.notifyDataSetChanged();

            Toast.makeText(Main_Nav.this, "Successfully Refreshed",
                    Toast.LENGTH_SHORT).show();
            progdialog.dismiss();
        }
    }

    public class MyAlertDialogFragment_SignOut extends DialogFragment {
        public MyAlertDialogFragment_SignOut() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(
                    getActivity());

            myDialog.setTitle("Log Out?");
            myDialog.setMessage("Saying bye bye?");
            myDialog.setIcon(getResources().getDrawable(
                    R.drawable.ic_action_locate));
            myDialog.setNegativeButton("No!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            myDialog.setPositiveButton("Log Out",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // do the following to logout. as of now, everything
                            // gets deleted from the local database.

                            SharedPreferences prefs = getSharedPreferences(
                                    Constants.USER_PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Logging Out.", Toast.LENGTH_SHORT).show();
                            editor.putString(Constants.bundle_username,
                                    "none");
                            editor.putString(Constants.bundle_pwd, "none");
                            editor.commit();
                            EmailMessage.deleteAll(EmailMessage.class);

                            editor.putBoolean("toggle_wifi", false);
                            editor.putBoolean("toggle_mobiledata", false);

                            NetworkChangeBroadcastReceiver
                                    .cancelNotification(getApplicationContext());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                    finish();
                                }
                            }, 2000);
                        }
                    });
            return myDialog.create();
        }
    }

    public void logout() {
        // if logged in, then make logout available.
        DialogFragment newFrag = new MyAlertDialogFragment_SignOut();
        newFrag.show(getFragmentManager(), "signoutdialog");
    }
}
