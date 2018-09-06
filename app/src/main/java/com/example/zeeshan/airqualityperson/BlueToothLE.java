package com.example.zeeshan.airqualityperson;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;

import javax.security.auth.callback.Callback;

public class BlueToothLE {
    private Context context;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothDevice bluetoothDevice;
    private static final String LOG_BLUETOOTH = "BLUETOOTH";
    private ReentrantLock callBackLock;
    private static String bluetoothName;
    private static volatile boolean deviceFound = false;
    public BlueToothLE(Context context, String deviceName){
        callBackLock = new ReentrantLock();
        this.context = context;
        bluetoothName = deviceName;


        bluetoothManager = (BluetoothManager) context.getSystemService(Service.BLUETOOTH_SERVICE);
        if (bluetoothManager == null)
            Log.d(LOG_BLUETOOTH, "BluetoothManager is null");
        else Log.d(LOG_BLUETOOTH, "Bluetooth Manager Started");
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null)
            Log.d(LOG_BLUETOOTH, "BluetoothAdapter is null");
        else Log.d(LOG_BLUETOOTH, "BluetoothAdapter Started");
    }

    public void turnOnBluetooth(){
        Log.d(LOG_BLUETOOTH, "Turning on Bluetooth");
        while (! bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(LOG_BLUETOOTH, "...................");
        }
        Log.d(LOG_BLUETOOTH, "Bluetooth turned on");
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BluetoothDevice scanBluetoothLEDevices(){
        Log.d("hell", "yeah");
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bluetoothLeScanner.stopScan(scanCallback);
        Log.d(LOG_BLUETOOTH, "NUMBER of DEVICES found ->> " + bluetoothDevice.getName());
        return bluetoothDevice;
    }

    ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            callBackLock.lock();
            if (deviceFound) {
                bluetoothDevice = result.getDevice();
                String deviceName = bluetoothDevice.getName();
                if (bluetoothName == deviceName)
                    deviceFound = true;
            }
            callBackLock.unlock();
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void connectGATT(String Uuid){
        /*
        bluetoothDevice.connectGatt(context, true, new BluetoothGattCallback() {

            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
            }


        });
*/
        BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord();
        bluetoothSocket.connect();
        InputStream bluetoothSocketInputStream = bluetoothSocket.getInputStream();
        BufferedReader bluetoothSocketBufferedReader = new BufferedReader(bluetoothSocketInputStream);
        Log.d(LOG_BLUETOOTH, bluetoothSocketBufferedReader.readLine());
    }

}
