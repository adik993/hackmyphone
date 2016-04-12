package com.adrian.hackmyphone.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.R;

public class TelephonyFragment extends Fragment {

    public static Fragment newInstance() {
        return new TelephonyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_telephony, container, false);

        return view;
    }
}
