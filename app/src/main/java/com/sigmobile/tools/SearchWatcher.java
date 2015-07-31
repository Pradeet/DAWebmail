package com.sigmobile.tools;

import java.util.ArrayList;
import java.util.Collections;

import com.sigmobile.dawebmail.EmailMessage;
import com.sigmobile.dawebmail.Main_Nav;
import com.sigmobile.dawebmail.fragments.InboxFragment;
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
        Printer.println(Main_Nav.mSearchQuery);
        // Do search task here

        if (Main_Nav.mSearchQuery.length() >= 2) {
            InboxFragment.allemails_main = (ArrayList<EmailMessage>) EmailMessage
                    .listAll(EmailMessage.class);
            Collections.reverse(InboxFragment.allemails_main);

            for (int i = 0; i < InboxFragment.allemails_main.size(); i++) {
                EmailMessage email = InboxFragment.allemails_main.get(i);
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
                    InboxFragment.allemails_main.remove(email);
                    i--;
                }
            }
            InboxFragment.mAdapter.notifyDataSetChanged();
        } else {
            InboxFragment.allemails_main = (ArrayList<EmailMessage>) EmailMessage
                    .listAll(EmailMessage.class);
            Collections.reverse(InboxFragment.allemails_main);
            InboxFragment.mAdapter.notifyDataSetChanged();
        }
    }

    public void updateInboxList() {

    }

}