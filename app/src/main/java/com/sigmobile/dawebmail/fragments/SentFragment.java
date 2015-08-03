package com.sigmobile.dawebmail.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigmobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SentFragment extends Fragment {


    public SentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sent, container, false);
    }


}
