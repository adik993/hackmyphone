package com.adrian.hackmyphone.items;

import android.net.wifi.ScanResult;

/**
 * Created by adrian on 4/12/16.
 */
public class WifiItem implements Titled {

    ScanResult mScanResult;

    public WifiItem(ScanResult scanResult) {
        mScanResult = scanResult;
    }

    @Override
    public String getTitle() {
        return mScanResult.SSID+"("+mScanResult.level+")";
    }
}
