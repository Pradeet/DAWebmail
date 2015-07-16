package rish.crearo.dawebmail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import rish.crearo.R;

public class ComposeEmail extends Activity {

    EditText contenttf, toaddresstf, subjectaddresstf;
    ScrollView scrollview;
    static Boolean exitcompose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        getActionBar().setTitle("Compose Email");
        toaddresstf = (EditText) findViewById(R.id.compose_toaddresstf);
        subjectaddresstf = (EditText) findViewById(R.id.compose_subjecttf);
        contenttf = (EditText) findViewById(R.id.compose_contenttf);
        scrollview = (ScrollView) findViewById(R.id.compose_scrollview);
        toaddresstf.setHintTextColor(Color.WHITE);
        subjectaddresstf.setHintTextColor(Color.WHITE);
        contenttf.setHintTextColor(Color.WHITE);
        scrollview.setFillViewport(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        switch (item.getItemId()) {

            case R.id.action_composemail:
                DialogFragment newFrag = new MyAlertDialogFragment_Send();
                newFrag.show(getFragmentManager(), "senddialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DialogFragment newFrag = new MyAlertDialogFragment_Exit();
        newFrag.show(getFragmentManager(), "exitdialog");
    }

    public void sendWebmail() {
        Toast.makeText(getApplicationContext(), "Sending Webmail",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ComposeEmail.this, Main_Nav.class));

    }

    public class MyAlertDialogFragment_Send extends DialogFragment {
        public MyAlertDialogFragment_Send() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(
                    getActivity());

            myDialog.setTitle("Continue sending webmail?");
            myDialog.setIcon(getResources().getDrawable(
                    R.drawable.ic_action_send));
            myDialog.setNegativeButton("No!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            myDialog.setPositiveButton("Send.",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            sendWebmail();
                        }
                    });

            return myDialog.create();
        }
    }

    public class MyAlertDialogFragment_Exit extends DialogFragment {
        public MyAlertDialogFragment_Exit() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(
                    getActivity());

            myDialog.setTitle("Back to Inbox? This webmail will not be saved as a draft.");
            myDialog.setIcon(getResources().getDrawable(
                    R.drawable.ic_action_delete));
            myDialog.setNegativeButton("No!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            myDialog.setPositiveButton("Back to Inbox.",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            startActivity(new Intent(ComposeEmail.this,
                                    Main_Nav.class));
                        }
                    });
            return myDialog.create();
        }
    }
}
