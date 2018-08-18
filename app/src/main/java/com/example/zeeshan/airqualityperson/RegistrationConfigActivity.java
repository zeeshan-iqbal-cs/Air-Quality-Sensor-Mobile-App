package com.example.zeeshan.airqualityperson;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.Permission;
import java.util.List;

public class RegistrationConfigActivity extends AppCompatActivity {

    private static final String[] PERMISSION = {Manifest.permission.GET_ACCOUNTS,
            Manifest.permission_group.LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE};
    private static final int REQUEST_CODE = 5050;

    private String[] email;
    private String[] savedWifiNames;
    private List<WifiConfiguration> savedWifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_config);

        getPermissions();
        getEmailList();
        setEmailSpinner();
     //   getSavedWifiList();
      //  setWifiSpinner();
     //   getLocation();



    }


    private void getPermissions(){
        for(String permission:PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission))
                    Toast.makeText(getBaseContext(), "Mrs fuji some thing went wrong!!", Toast.LENGTH_SHORT).show();
                else
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
            }
        }
    }

    private String[] getEmailList(){
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] account = accountManager.getAccounts();

        email = new String[account.length];
        for (int i = 0; i < account.length; i++)
            email[i] =account[i].name;
        return email;
    }

    private void setEmailSpinner(){
        Spinner emailSpinner = findViewById(R.id.email_spinner);
        ArrayAdapter<String> emailSpinnerAdapter =
                new ArrayAdapter<>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, email);
        emailSpinner.setAdapter(emailSpinnerAdapter);
    }

    private void getSavedWifiList(){
        WifiManager wifiManager = (WifiManager) getSystemService(Service.WIFI_SERVICE);
        savedWifiConfiguration = wifiManager.getConfiguredNetworks();
        if (savedWifiNames == null){
            Log.d("hell","please don't");
        }
        savedWifiNames = new String[savedWifiConfiguration.size()];
        for (int i = 0; i < savedWifiNames.length; i++)
            savedWifiNames[0] = savedWifiConfiguration.get(0).SSID;
    }

    private void setWifiSpinner(){
        Spinner wifiSpinner = findViewById(R.id.wifi_spinner);
        ArrayAdapter<String> wifiSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, savedWifiNames);
        wifiSpinner.setAdapter(wifiSpinnerAdapter);
    }

    private void getLocation(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
            if (locationManager != null) {
                //@SuppressLint("MissingPermission")
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Toast.makeText(this,
                        location.getLatitude() + "," + location.getLongitude(),
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getBaseContext(),
                        "batmeez mazak",
                        Toast.LENGTH_SHORT).show();
            }
        } catch(SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Location Exception",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
