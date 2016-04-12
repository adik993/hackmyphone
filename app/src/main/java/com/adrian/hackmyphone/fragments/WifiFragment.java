package com.adrian.hackmyphone.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.MyAdapter;
import com.adrian.hackmyphone.items.WifiItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class WifiFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView mList;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    MyAdapter mAdapter;

    WifiManager mWifiManager;
    WifiReceiver mWifiReceiver;

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        mWifiManager= (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver=new WifiReceiver();
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new MyAdapter(new ArrayList<>());
        mList.setAdapter(mAdapter);

        if(!mWifiManager.isWifiEnabled()) mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
        return view;
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
