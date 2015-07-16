package com.example.aakash.augrealtaketwo.Services;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * Created by Aakash on 6/30/2015.
 */
public class LocationFinder extends Service implements   GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    LocationClient mLocationClient;
    public static String RESULTANT_ADDRESS = "Address Returned";
    public static Boolean isConnected =false;
    private final IBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        public LocationFinder getService() {
            return LocationFinder.this;
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
     isConnected = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();

    }

    @Override
    public void onDisconnected() {
        isConnected =false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failure : " +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public String[] getLatAndLong() {
        String[] coordinates = new String[2];
        if(isConnected)
        // Get the current location's latitude & longitude
        {
            Location currentLocation = mLocationClient.getLastLocation();

            if(mLocationClient!=null) {
                coordinates[0] =
                        Double.toString(currentLocation.getLatitude());

                coordinates[1] =
                        Double.toString(currentLocation.getLongitude());
            }
                return coordinates;
        }
        else
        {

            Toast.makeText(getApplicationContext(),"Not connected",Toast.LENGTH_SHORT).show();
            return null;
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
