package com.example.aakash.augrealtaketwo.View;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;
import com.example.aakash.augrealtaketwo.R;
import com.example.aakash.augrealtaketwo.Services.InvokeRESTImpl;
import com.example.aakash.augrealtaketwo.Services.LocationFinder;
import com.example.aakash.augrealtaketwo.Services.PrepareInitialScreenUtil;


public class MainActivity extends Activity {


    public static TextView city_field;
    public static TextView updated_field;
    public static TextView weather_icon;
    public static TextView temperature;
    public static SeekBar seekBar;
    public static ImageView temperaturePic;
    public static ImageButton nearByButton;
    public static Camera camera = null;
    public static NearByPlacesInfo dataForNearbyCity;
    LayoutInflater inflater = null;
    ShowCamera showCamera;
    public static boolean showCurrentLocation = true;
    public static String[] lat = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent startServiceIntent = new Intent(this, LocationFinder.class);
        bindService(startServiceIntent, serviceConnection, BIND_AUTO_CREATE);


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

        } else if (dataForNearbyCity != null) {
            PrepareInitialScreenUtil prep = new PrepareInitialScreenUtil();
            camera = prep.makeCameraAvailable();
            inflater = LayoutInflater.from(getBaseContext());
            View view = inflater.inflate(R.layout.overlay, null);
            prep.prepareViews(view);
            MainActivity.city_field.setText(dataForNearbyCity.getPlaceName());
            MainActivity.updated_field.setText(dataForNearbyCity.getPlaceLatitude() + "," + dataForNearbyCity.getPlaceLongitude());
            LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            this.addContentView(view, layoutParamsControl);
        }
        //Opens the camera
        showCamera = new ShowCamera(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
        nearByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(lat==null)
                    {
                        lat = locationFinder.getLatAndLong();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), " Cannot fetch GeoLocation details : " + ex, Toast.LENGTH_LONG).show();
                }
              /*   lat[0]="45.4";
                lat[1]="-75.3";*/
                try {

                    String serverURL = "http://getnearbycities.geobytes.com/GetNearbyCities?callback=?&radius=100&latitude=" + lat[0] + "&longitude=" + lat[1];
                    new InvokeRESTImpl(getApplication(),InvokeRESTImpl.NEAR_BY_LOCATION,MainActivity.this).execute(serverURL);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "API to find nearby cities"+ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Debug",ex.getMessage());
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    LocationFinder locationFinder;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
            LocationFinder.MyBinder myBinder = (LocationFinder.MyBinder) iBinder;
            locationFinder = myBinder.getService();

              /*
        Call the Weather Info API at the start of the application
         */
            
            try {
                lat = locationFinder.getLatAndLong();
                // Create the URL based on the latitude and longitude information fetched from the previous method
                String serverURLforWeatherInfo = "";
                //    new InvokeRESTImpl(getApplicationContext(), InvokeRESTImpl.WEATHER_INFO,MainActivity.this).execute(serverURLforWeatherInfo);
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), "API to find weather Info not working. Please try later", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_LONG).show();

        }
    };


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
    protected void onStop() {
        super.onStop();
       unbindService(serviceConnection);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
