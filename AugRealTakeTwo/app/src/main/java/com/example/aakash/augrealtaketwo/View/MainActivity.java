package com.example.aakash.augrealtaketwo.View;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;
import com.example.aakash.augrealtaketwo.R;
import com.example.aakash.augrealtaketwo.Services.InvokeRESTImpl;
//import com.example.aakash.augrealtaketwo.Services.LocationFinder;
import com.example.aakash.augrealtaketwo.Services.PrepareInitialScreenUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;


import com.example.aakash.augrealtaketwo.Services.GetWeatherTask;
import com.example.aakash.augrealtaketwo.Model.Region;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    public static TextView city_field;
    public static TextView updated_field;
//    public static TextView weather_icon;
    public static TextView temperature;
    public static TextView weather_description;
    // Search distance
    public static Integer seekBar_distance_covered = 0;
    public static SeekBar seekBar;
    public static ImageView temperaturePic;
    public static ImageButton nearByButton;
    public static Camera camera = null;
    public static NearByPlacesInfo dataForNearbyCity;
    LayoutInflater inflater = null;
    ShowCamera showCamera;
    public static boolean showCurrentLocation = true;

   //TODO: Commenting out as we already having location field stored in Region class
//    public static String[] lat = new String[2];

    public static Button weatherButton;
    // Stores Json city weather response from OpenWeather Map.org
    public static String weatherJSONResponse;
    // Google client api reference
    private GoogleApiClient googleApiClient;
    // Sensor Manager reference. Access device sensors via sensor manager
    private static SensorManager deviceSensorManager;
    // Sensor represents device sensors
    private static Sensor sensor;

    // TODO: Check why null on app starting
    // Holds the current location
    public static Location currentLocation = null;

    //TODO: Investigate further about connection failure scenario
    // Error resolution class variables to be used in onConnectionFailed()
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Boolean to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Intent startServiceIntent = new Intent(this, LocationFinder.class);
//        bindService(startServiceIntent, serviceConnection, BIND_AUTO_CREATE);


        //Logic to determine if the current location weather to be displayed or
        //the weather of the place as chosen from the Near by Cities List
        if (showCurrentLocation) {
            PrepareInitialScreenUtil prep = new PrepareInitialScreenUtil();
            camera = prep.makeCameraAvailable();
            inflater = LayoutInflater.from(getBaseContext());
            View view = inflater.inflate(R.layout.overlay, null);
            prep.prepareViews(view);
            LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            this.addContentView(view, layoutParamsControl);

        }
        // Changing city info based on Region class
        else if (Region.getWeatherInstance() != null) {
            PrepareInitialScreenUtil prep = new PrepareInitialScreenUtil();
            camera = prep.makeCameraAvailable();
            inflater = LayoutInflater.from(getBaseContext());
            View view = inflater.inflate(R.layout.overlay, null);
            prep.prepareViews(view);

            MainActivity.city_field.setText(Region.getMyCity());
            // Not displaying longitude & latitude on the screen
            LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            this.addContentView(view, layoutParamsControl);

//            MainActivity.city_field.setText(dataForNearbyCity.getPlaceName());
//            MainActivity.updated_field.setText(dataForNearbyCity.getPlaceLatitude() + "," + dataForNearbyCity.getPlaceLongitude());
//            LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//            this.addContentView(view, layoutParamsControl);
        }
        //Opens the camera
        showCamera = new ShowCamera(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);

        googleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build()
        ;
        // TODO: Investigate further about connection failure scenario
        // Recover resolution recover boolean from saveInstance State method()
        mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        deviceSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor =  deviceSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        deviceSensorManager.registerListener(mySensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        nearByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    if(lat==null)
//                    {
//                        lat = locationFinder.getLatAndLong();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(), " Cannot fetch GeoLocation details : " + ex, Toast.LENGTH_LONG).show();
//                }
              /*   lat[0]="45.4";
                lat[1]="-75.3";*/
                //TODO: We have location details already existing in Regions class
                try {
                    String serverURL = "http://getnearbycities.geobytes.com/GetNearbyCities?callback=?&radius=100&latitude=" +
                            Region.getMyLocation_latitude() + "&longitude=" + Region.getMyLocation_longitude();

                    new InvokeRESTImpl(getApplication(),InvokeRESTImpl.NEAR_BY_LOCATION,MainActivity.this).execute(serverURL);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "API to find nearby cities"+ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Debug",ex.getMessage());
                }
            }
        });


    }

    /***
     * Invoked when get weather button is pressed
     *
     * @param view
     */
    public void getWeather(View view) {
        new GetWeatherTask().execute(currentLocation);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register device sensor when user starts app interaction
        if(sensor != null)
        {
            deviceSensorManager.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=ßWest
            float azimuth = event.values[0];
            // TODO: Uncomment if we want to display live compass on front screen
//            compassView.updateData(azimuth);
//            Toast.makeText(MainActivity.this, "Degrees :" + String.valueOf(azimuth), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

//    LocationFinder locationFinder;
//    ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
//            LocationFinder.MyBinder myBinder = (LocationFinder.MyBinder) iBinder;
//            locationFinder = myBinder.getService();

              /*
        Call the Weather Info API at the start of the application
         */
            
//            try {
////                lat = locationFinder.getLatAndLong();
//                // Create the URL based on the latitude and longitude information fetched from the previous method
//                String serverURLforWeatherInfo = "";
//                //    new InvokeRESTImpl(getApplicationContext(), InvokeRESTImpl.WEATHER_INFO,MainActivity.this).execute(serverURLforWeatherInfo);
//            }
//            catch (Exception ex)
//            {
//                Toast.makeText(getApplicationContext(), "API to find weather Info not working. Please try later", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_LONG).show();
//
//        }
//    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect with google services api on app start
        if (!mResolvingError) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(serviceConnection);
        // Disconnect with google services api
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister device sensor on app pause
        if (sensor != null) {
            deviceSensorManager.unregisterListener(mySensorEventListener);
            sensor = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            deviceSensorManager.unregisterListener(mySensorEventListener);
            sensor = null;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get_last_location return location object from which we retrieve latitude and longitude position
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        Region.setMyLocation(currentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Here error resolution takes place.
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                googleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "Error Message (TODO)");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment
    {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity)getActivity()).onDialogDismissed();
        }
    }

    // Maintain state while resolving an error
    // After error resolution we should set error flag back to FALSE.
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            mResolvingError = false;
        }
        else if(resultCode == RESULT_CANCELED)
        {
            mResolvingError = false;
        }
    }

    /**
     * To maintain state of app when activity restarts or app was killed abruptly
     */
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
        // Then we recover the saved state in onCreate()
    }
}
