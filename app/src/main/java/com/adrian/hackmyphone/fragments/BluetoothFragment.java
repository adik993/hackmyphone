package com.adrian.hackmyphone.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.MyAdapter;
import com.adrian.hackmyphone.items.BTItem;
import com.adrian.hackmyphone.items.InfoItem;
import com.adrian.hackmyphone.items.ProgressItem;
import com.adrian.hackmyphone.items.Section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class BluetoothFragment extends Fragment {

    private static final int REQ_BT_EN = 1;
    @Bind(R.id.list)
    RecyclerView mList;
    MyAdapter mAdapter;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothReceiver mBluetoothReceiver;

    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        mBluetoothAdapter=getBTAdapter();
        ButterKnife.bind(this, view);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new MyAdapter(new ArrayList<>());
        populateAdapter();
        mList.setAdapter(mAdapter);
        mBluetoothReceiver=new BluetoothReceiver(mAdapter);
        if(mBluetoothAdapter.isEnabled()) {
            startDiscovery();
        } else {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQ_BT_EN);
        }
        return view;
    }

    private void populateAdapter() {
        mAdapter.elems.add(new Section(getString(R.string.bounded_devices)));
        List<BTItem> btItems = mapToBTItem(mBluetoothAdapter.getBondedDevices());
        if(btItems.isEmpty()) {
            mAdapter.elems.add(new InfoItem(getString(R.string.none)));
        } else mAdapter.elems.addAll(btItems);
        mAdapter.elems.add(new Section(getString(R.string.found_devices)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_BT_EN) {
            if(resultCode== Activity.RESULT_OK) {
                startDiscovery();
            } else {
                Snackbar.make(mList, R.string.bt_needed, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void startDiscovery() {
        if(!mBluetoothAdapter.startDiscovery()) {
            Snackbar.make(mList, R.string.unable_to_discover, Snackbar.LENGTH_SHORT).show();
        } else {
            mAdapter.elems.add(new ProgressItem());
            mAdapter.notifyItemInserted(mAdapter.elems.size()-1);
        }
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mBluetoothReceiver, BluetoothReceiver.getIntentFilter());
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mBluetoothReceiver);
        super.onPause();
    }

    private BluetoothAdapter getBTAdapter() {
        BluetoothAdapter adapter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager manager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            adapter=manager.getAdapter();
        } else {
            adapter=BluetoothAdapter.getDefaultAdapter();
        }

        return adapter;
    }

    @SuppressWarnings("unchecked")
    private List<BTItem> mapToBTItem(Collection<BluetoothDevice> collection) {
        final List[] ret = {null};
        Observable.from(collection)
                .map(BTItem::new)
                .toList()
                .toBlocking()
                .subscribe(btItems -> ret[0] =btItems);
        return (List<BTItem>)ret[0];
    }

    private static class BluetoothReceiver extends BroadcastReceiver {

        MyAdapter mAdapter;

        public BluetoothReceiver(MyAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int pos = mAdapter.elems.size() - 1;
                mAdapter.elems.add(pos, new BTItem(device));
                mAdapter.notifyItemInserted(pos);
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mAdapter.elems.remove(mAdapter.elems.size()-1);
                mAdapter.notifyItemRemoved(mAdapter.elems.size());
            }
        }

        public static IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            return filter;
        }
    }

}
