package rish.crearo.tools;

import java.util.ArrayList;
import java.util.Collections;

import rish.crearo.dawebmail.EmailMessage;
import rish.crearo.dawebmail.Main_Nav;
import rish.crearo.dawebmail.fragments.FragmentOne;
import android.text.Editable;
import android.text.TextWatcher;

public class SearchWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence c, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Main_Nav.mSearchQuery = Main_Nav.mSearchEt.getText().toString();
        System.out.println(Main_Nav.mSearchQuery);
        // Do search task here

        if (Main_Nav.mSearchQuery.length() >= 2) {
            FragmentOne.allemails_main = (ArrayList<EmailMessage>) EmailMessage
                    .listAll(EmailMessage.class);
            Collections.reverse(FragmentOne.allemails_main);

            for (int i = 0; i < FragmentOne.allemails_main.size(); i++) {
                EmailMessage email = FragmentOne.allemails_main.get(i);
                if (email.getFromName().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())
                        || email.getFromaddress().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())
                        || email.getSubject().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())
                        || email.getDate().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())
                        || email.getDateentire().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())
                        || email.getContent().toLowerCase()
                        .contains(Main_Nav.mSearchQuery.toLowerCase())) {
                } else {
                    FragmentOne.allemails_main.remove(email);
                    i--;
                }
            }
            FragmentOne.mAdapter.notifyDataSetChanged();
        } else {
            FragmentOne.allemails_main = (ArrayList<EmailMessage>) EmailMessage
                    .listAll(EmailMessage.class);
            Collections.reverse(FragmentOne.allemails_main);
            FragmentOne.mAdapter.notifyDataSetChanged();
        }
    }

    public void updateInboxList() {

    }

}