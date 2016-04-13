package com.adrian.hackmyphone.fragments;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.items.WifiItem;

import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;

public class WifiFragment extends BaseListProgressFragment {

    private static final int ASK_LOC = 1;
    WifiManager mWifiManager;
    WifiReceiver mWifiReceiver;

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        mWifiManager= (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver=new WifiReceiver();

        if(!mWifiManager.isWifiEnabled()) mWifiManager.setWifiEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ASK_LOC);
        } else mWifiManager.startScan();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ASK_LOC: {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    mWifiManager.startScan();
                } else {
                    Snackbar.make(getView(), R.string.allow_location_to_scan, Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mWifiReceiver);
        super.onPause();
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            mAdapter.elems.clear();
            Observable.from(scanResults)
                    .map(WifiItem::new)
                    .toList()
                    .toBlocking()
                    .subscribe(wifiItems -> mAdapter.elems.addAll(wifiItems));
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
