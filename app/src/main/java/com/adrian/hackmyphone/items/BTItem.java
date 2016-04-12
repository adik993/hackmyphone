package com.adrian.hackmyphone.items;

import android.bluetooth.BluetoothDevice;

/**
 * Created by adrian on 4/12/16.
 */
public class BTItem implements Titled {

    BluetoothDevice mDevice;

    public BTItem(BluetoothDevice device) {
        mDevice = device;
    }

    @Override
    public String getTitle() {
        return mDevice.getName() + "(" + mDevice.getAddress()+")";
    }
}
