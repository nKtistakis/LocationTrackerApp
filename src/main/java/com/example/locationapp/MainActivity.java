package com.example.locationapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onResume() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(new WifiConnection(), intentFilter);

    super.onResume();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Uri uri = Uri.parse("content://com.example.locationapp.LocationProvider/location");
    ContentValues values = new ContentValues();

    values.put("LAT", "sdad");
    values.put("LON", "23gd2");
    values.put("UNIX_TIMESTAMP", "677843");

    Uri result = this.getContentResolver().insert(uri, values);
    Cursor c = this.getContentResolver().query(result, null, null, null);
    if (!c.moveToFirst()) {
      Toast.makeText(this, "not move to first", Toast.LENGTH_SHORT).show();
      return;
    }

    do {
      Toast.makeText(this, c.getString(1), Toast.LENGTH_SHORT).show();
    } while (c.moveToNext());

    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 69);
    }

  }
}
