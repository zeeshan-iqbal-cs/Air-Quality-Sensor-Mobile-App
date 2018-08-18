package com.example.zeeshan.airqualityperson;

import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent registrationConfigActivityIntent = new Intent(this.getBaseContext(),
                RegistrationConfigActivity.class);
        startActivity(registrationConfigActivityIntent);
       // findViewById(R.id.mobile_connection_button).setOnClickListener(mobileConnectionButtonListener);
    }

    private View.OnClickListener mobileConnectionButtonListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new ConnectWithMobile().execute("");
        }
    };

    private class ConnectWithMobile extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            // ----------------- Load Data from /Resources/String
            String wifiSSID = getResources().getString(R.string.device_hotspot_SSID);
            String wifiPassword = getResources().getString(R.string.device_hotspot_password);
            WiFiConnect wiFiConnect = new WiFiConnect(getBaseContext(), wifiSSID, wifiPassword);
            while (! wiFiConnect.connected){
                Thread.sleep(1000);
            }

            //========================================================
            try {
                Socket s = new Socket("192.168.1.17",5005);
                PrintWriter printWriter = new PrintWriter(s.getOutputStream());
                printWriter.flush();
                printWriter.print("{\"Email\":\"batmeez@gmail.com\",\"SSID\":\"DIR Makeistan\",\"Password\":\"makeitall\",\"Location\":[48.756080,2.302038]}\0");
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
