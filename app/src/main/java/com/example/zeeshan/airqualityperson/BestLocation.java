package com.example.zeeshan.airqualityperson;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class BestLocation {
    private final Context context;
    public static final String TAG = "LOCATION";
    public BestLocation(Context context){
        this.context = context;
    }
    public boolean enabled(){
        LocationManager mLocationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        if (mLocationManager == null)
            return false;
        if (! mLocationManager.isLocationEnabled())
            return false;
        return true;
    }
    public Location getLocation(){
        LocationManager mLocationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        if (mLocationManager == null){
            Log.d(TAG, "Location Manager is NULL");
            return null;
        }

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                Log.d(TAG, "IS NULL:" + provider);
                continue;
            }else{
                Log.d(TAG, "NOT NULL:" + provider);
            }

            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        Log.d(TAG, "Final location provider " + bestLocation.getProvider());
        Log.d(TAG, "Accuracy of Picked location " + bestLocation.getAccuracy());
        return bestLocation;
    }
}
