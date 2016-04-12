package com.adrian.hackmyphone.fragments;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.AccelerometerActivity;
import com.adrian.hackmyphone.CompassActivity;
import com.adrian.hackmyphone.GpsActivity;
import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.ItemVisibilityScrollListener;
import com.adrian.hackmyphone.adapter.MyAdapter;
import com.adrian.hackmyphone.adapter.OnClickListener;
import com.adrian.hackmyphone.items.IntentItem;
import com.adrian.hackmyphone.items.Section;
import com.adrian.hackmyphone.items.SensorItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class SensorsFragment extends Fragment implements OnClickListener, SensorEventListener, ItemVisibilityScrollListener.ItemVisibilityChange {

    private final String TAG=getClass().getName();

    private int RATE=1000000;

    SensorManager sensorManager;

    @Bind(R.id.list)
    RecyclerView list;
    MyAdapter adapter;

    public static SensorsFragment newInstance() {
        SensorsFragment fragment = new SensorsFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        sensorManager= (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(new Section(getString(R.string.combined_section)));
        tmp.add(IntentItem.builder()
                .context(getActivity())
                .activityClass(GpsActivity.class)
                .title(getString(R.string.gps))
                .build());
        tmp.add(IntentItem.builder()
                .context(getActivity())
                .activityClass(CompassActivity.class)
                .title(getString(R.string.compass))
                .build());
        tmp.add(IntentItem.builder()
                .context(getActivity())
                .activityClass(AccelerometerActivity.class)
                .title(getString(R.string.accelerometer))
                .build());
        tmp.add(new Section(getString(R.string.raw_sensor_data)));
        Observable.from(sensorList)
                .distinct(Sensor::getName)
                .map(SensorItem::new)
                .toList()
                .toBlocking()
                .subscribe(tmp::addAll);
        adapter=new MyAdapter(tmp);
        list.setAdapter(adapter);
        adapter.setListener(this);
        list.addOnScrollListener(new ItemVisibilityScrollListener(layoutManager, this));

        return view;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onItemClick(int i, RecyclerView.ViewHolder vh) {
        Object o = adapter.elems.get(i);
        if(o instanceof SensorItem) onItemClick(i, vh, (SensorItem)o);
        else if(o instanceof IntentItem) onItemClick(i, vh, (IntentItem)o);
    }

    private void onItemClick(int i, RecyclerView.ViewHolder vh, IntentItem item) {
        startActivity(item.getIntent());
    }

    private void onItemClick(int i, RecyclerView.ViewHolder vh, SensorItem item) {
        Observable.from(adapter.elems)
                .filter(o1 -> o1 instanceof SensorItem)
                .map(o2 -> (SensorItem)o2)
                .filter(sensorItem -> sensorItem.isRegistered() && !sensorItem.equals(item))
                .toBlocking()
                .subscribe(sensorItem1 -> {
                    sensorManager.unregisterListener(this, sensorItem1.getSensor());
                    Log.d(TAG, "Unregistered: " + sensorItem1.getSensor().getName());
                    sensorItem1.setRegistered(false);
                    int index = adapter.elems.indexOf(sensorItem1);
                    adapter.notifyItemChanged(index);
                });
        item.toggleRegistered();
        if(item.isRegistered()) {
            sensorManager.registerListener(this, item.getSensor(), RATE);
            Log.d(TAG, "Registered: " + item.getSensor().getName());
        }
        else {
            sensorManager.unregisterListener(this, item.getSensor());
            Log.d(TAG, "Unregistered: " + item.getSensor().getName());
        }
        adapter.notifyItemChanged(i);
    }

    @Override
    public boolean onItemLongClick(int i, RecyclerView.ViewHolder vh) {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int i=-1;
        for (Object o : adapter.elems) {
            i++;
            if(o instanceof SensorItem) {
                SensorItem item= (SensorItem) o;
                if(!event.sensor.getName().equals(item.getSensor().getName())) continue;
                item.setValues(event.values);
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onItemHidden(int position) {
        Log.d(TAG, "Item hidden: "+position);
        Object o = adapter.elems.get(position);
        if(o instanceof SensorItem) {
            SensorItem sensorItem= (SensorItem) o;
            sensorManager.unregisterListener(this, sensorItem.getSensor());
            Log.d(TAG, "Unregistered: "+sensorItem.getSensor().getName());
        }
    }

    @Override
    public void onItemShown(int position) {
        Log.d(TAG, "Item shown: "+position);
        Object o = adapter.elems.get(position);
        if(o instanceof SensorItem) {
            SensorItem sensorItem= (SensorItem) o;
            if(sensorItem.isRegistered()) {
                sensorManager.registerListener(this, sensorItem.getSensor(), RATE);
                Log.d(TAG, "Registered: " + sensorItem.getSensor().getName());
            }
        }
    }
}
