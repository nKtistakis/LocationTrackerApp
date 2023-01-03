package com.example.locationapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class WifiConnection extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Intent i = new Intent(context.getApplicationContext(), GPS_Service.class);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            context.startService(i);
        } else {
            Toast.makeText(context, "Device not connected to Wifi", Toast.LENGTH_SHORT).show();
            context.stopService(i);
        }

    }
}



