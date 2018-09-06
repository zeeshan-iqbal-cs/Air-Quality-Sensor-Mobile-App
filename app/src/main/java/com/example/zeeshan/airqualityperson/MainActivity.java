package com.example.zeeshan.airqualityperson;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.VolatileCallSite;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private static final String[] PERMISSION = {Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_CODE = 5050;
    private static final String LOG_PERMISSION = "PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mobile_connection_button).setOnClickListener(mobileConnectionButtonListener);
        getPermissions();
    }



    private View.OnClickListener mobileConnectionButtonListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            (new ConnectWithNode()).execute("");
        }
    };

    private class ConnectWithNode extends AsyncTask {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Object doInBackground(Object[] objects) {

            BlueToothLE blueToothLE = new BlueToothLE(getBaseContext(), "MyESP32");
            blueToothLE.turnOnBluetooth();
            blueToothLE.scanBluetoothLEDevices();

            BestLocation bl = new BestLocation(getBaseContext());
            Location location = bl.getLocation();

            Intent registrationConfigActivityIntent = new Intent(getBaseContext(),
                    RegistrationConfigActivity.class);
            registrationConfigActivityIntent.putExtra(BestLocation.TAG, location);
            startActivity(registrationConfigActivityIntent);
            return null;
        }
    }


    private void getPermissions(){
        ArrayList<String> permissionSet = new ArrayList<>();
        for(String permission:PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_PERMISSION, "Permission Denied: " + permission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission))
                    Log.d(LOG_PERMISSION, "Can't ask: " + permission);
                permissionSet.add(permission);
            } else{
                Log.d(LOG_PERMISSION, "Already Permitted: " + permission);
            }
        }

        // IF no permission is required return
        //-=-//=====// Removing this will cause NullPointerException
        if (permissionSet.size() <= 0)
            return;

        String [] perSet = new String[permissionSet.size()];
        for (int i =0; i< permissionSet.size(); i++){
            perSet[i] = permissionSet.get(i);
        }

        ActivityCompat.requestPermissions(this, perSet, REQUEST_CODE);
        Log.d(LOG_PERMISSION, "ASKING Permission: " + perSet.length);
    }
}
