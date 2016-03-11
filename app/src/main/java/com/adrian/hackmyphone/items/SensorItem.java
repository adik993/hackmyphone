package com.adrian.hackmyphone.items;

import android.hardware.Sensor;

/**
 * Created by adrian on 3/10/16.
 */
public class SensorItem {
    private Sensor mSensor;
    private boolean mRegistered;
    private float[] values=new float[0];

    public SensorItem(Sensor sensor) {
        mSensor = sensor;
    }

    public Sensor getSensor() {
        return mSensor;
    }

    public void setSensor(Sensor sensor) {
        mSensor = sensor;
    }

    public boolean isRegistered() {
        return mRegistered;
    }

    public void setRegistered(boolean registered) {
        mRegistered = registered;
    }

    public void toggleRegistered() {
        setRegistered(!isRegistered());
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
