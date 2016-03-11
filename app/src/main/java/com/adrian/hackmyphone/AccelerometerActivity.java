package com.adrian.hackmyphone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccelerometerActivity extends BaseSensorActivity {

    private final String TAG=getClass().getName();

    private float[] gravity=new float[3];
    private float[] linear_acceleration=new float[3];
    private float[] gyroscope=new float[3];

    @Bind(R.id.accelerometer)
    TextView mAccelerometerValues;

    @Bind(R.id.gyroscope)
    TextView mGyroscopeValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        ButterKnife.bind(this);
    }

    @Override
    protected int[] getSensorTypes() {
        return new int[]{Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 200f/(200f+getSensorDelay());
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            String str = String.format("Akcelerometr(low-pass):\nX: %.2f\nY: %.2f\nZ: %.2f",
                    linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
            mAccelerometerValues.setText(str);
        } else if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
            gyroscope[0]=event.values[0];
            gyroscope[1]=event.values[1];
            gyroscope[2]=event.values[2];
            String str = String.format("Żyroskop:\nX: %3.0f\nY: %3.0f\nZ: %3.0f",
                    Math.toDegrees(gyroscope[0]), Math.toDegrees(gyroscope[1]), Math.toDegrees(gyroscope[2]));
            mGyroscopeValues.setText(str);
        }
    }
}
