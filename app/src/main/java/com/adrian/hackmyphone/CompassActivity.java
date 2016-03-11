package com.adrian.hackmyphone;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CompassActivity extends BaseSensorActivity {

    private static final float SHAKE_LIMIT = 11f;
    private final String TAG=getClass().getName();

    float[] lastAcc;
    float[] lastMagn;
    float[] rotationMatrix=new float[9];
    float[] orientationLast=new float[3];
    float[] orientation=new float[3];
    float accel;

    @Bind(R.id.arrow)
    ImageView mArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        setTitle(R.string.compass);
        ButterKnife.bind(this);
    }

    @Override
    protected int[] getSensorTypes() {
        return new int[]{Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD};
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            lastAcc= event.values;
            accel = (float) Math.sqrt(
                    Math.pow(lastAcc[0], 2) + Math.pow(lastAcc[1], 2) + Math.pow(lastAcc[2], 2));
        } else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
            lastMagn=event.values;
        }

        if(lastAcc!=null && lastMagn!=null && accel<=SHAKE_LIMIT) {
            boolean b = SensorManager.getRotationMatrix(this.rotationMatrix, null, lastAcc, lastMagn);
            if(b) {
                SensorManager.getOrientation(rotationMatrix, orientation);
                if(!Arrays.equals(orientationLast, orientation)) {
                    onOrientationChange(orientation);
                    System.arraycopy(orientation, 0, orientationLast, 0, orientation.length);
                }
            }
        }
    }

    long lastUpdate=0;

    private void onOrientationChange(float[] orientation) {
        long curr=System.currentTimeMillis();
        long diff=curr-lastUpdate;
        if(diff<250) return;
        lastUpdate=curr;
        float azimuth = (float)Math.toDegrees(-orientation[0]);
        float pitch = (float)Math.toDegrees(-orientation[1]);//X
        float roll = (float)Math.toDegrees(-orientation[2]);//Y
        ObjectAnimator aAnim = ObjectAnimator.ofFloat(mArrow, "rotation", mArrow.getRotation(), azimuth);
        ObjectAnimator pAnim = ObjectAnimator.ofFloat(mArrow, "rotationX", mArrow.getRotationX(), pitch);
        ObjectAnimator rAnim = ObjectAnimator.ofFloat(mArrow, "rotationY", mArrow.getRotationY(), roll);
        mArrow.clearAnimation();
        AnimatorSet set=new AnimatorSet();
        set.playTogether(aAnim, pAnim, rAnim);
        set.setDuration(250);
        set.start();
    }
}
