package com.example.zeeshan.airqualityperson;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
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


    private String[] email;
    public static final String ACCOUNT_TYPE = "com.google";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_config);

        getEmailList();
        setEmailSpinner();

    }

    private String[] getEmailList(){
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] account = accountManager.getAccountsByType(ACCOUNT_TYPE);

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

}
