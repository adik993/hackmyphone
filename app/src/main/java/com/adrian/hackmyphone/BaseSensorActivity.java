package com.adrian.hackmyphone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 3/11/16.
 */
public abstract class BaseSensorActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    int[] sensorTypes;
    List<Sensor> sensors;

    TextView mTextView;

    private int SENSOR_DELAY=SensorManager.SENSOR_DELAY_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        SENSOR_DELAY=setupSensorDelay();
        sensorTypes=getSensorTypes();
        sensors=new ArrayList<>(sensorTypes.length);
        for (int type : sensorTypes) {
            sensors.add(sensorManager.getDefaultSensor(type));
        }
    }

    protected int setupSensorDelay() {
        return SensorManager.SENSOR_DELAY_NORMAL;
    }

    public int getSensorDelay() {
        int delay = -1;
        switch (SENSOR_DELAY) {
            case SensorManager.SENSOR_DELAY_FASTEST:
                delay = 0;
                break;
            case SensorManager.SENSOR_DELAY_GAME:
                delay = 20000;
                break;
            case SensorManager.SENSOR_DELAY_UI:
                delay = 66667;
                break;
            case SensorManager.SENSOR_DELAY_NORMAL:
                delay = 200000;
                break;
            default:
                delay = SENSOR_DELAY;
                break;
        }
        return delay/1000;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mTextView= (TextView) findViewById(R.id.text);
    }

    protected abstract int[] getSensorTypes();

    @Override
    protected void onStart() {
        super.onStart();
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mTextView!=null) {
            float[] values = event.values;
            StringBuilder builder=new StringBuilder();
            for(int i=0; i<values.length; i++) {
                if(i!=values.length-1) builder.append(String.format("%.4f",values[i])+", ");
                else builder.append(values[i]);
            }
            mTextView.setText("Values: " + builder.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
