package com.example.aakash.augrealtaketwo.Services;

import android.content.Intent;
import android.hardware.Camera;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.aakash.augrealtaketwo.View.MainActivity;
import com.example.aakash.augrealtaketwo.R;

/**
 * Created by Aakash on 6/22/2015.
 */
public class PrepareInitialScreenUtil {

    public static Camera camera=null;
    public static Camera makeCameraAvailable()
    {
        releaseCamera();
        camera = Camera.open();
        return camera;
    }

    private static void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public void prepareViews(View view)
    {
        MainActivity.temperature = (TextView) view.findViewById(R.id.current_temperature_field);
        MainActivity.city_field = (TextView) view.findViewById(R.id.city_field);
        MainActivity.updated_field = (TextView) view.findViewById(R.id.updated_field);
//        MainActivity.weather_icon = (TextView) view.findViewById(R.id.weather_icon);
        MainActivity.seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        MainActivity.nearByButton = (ImageButton) view.findViewById(R.id.imageButton);
        MainActivity.temperaturePic = (ImageView) view.findViewById(R.id.temperaturePic);
        MainActivity.weather_description = (TextView) view.findViewById(R.id.weather_description);

        trackSeekBar();

//        MainActivity.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int distanceFromLocation = 0;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                distanceFromLocation = progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                //temperature.setText(Integer.toString(distanceFromLocation));
//                Integer[] params = new Integer[2];
//                params[0] = distanceFromLocation;
//                new performUpdate().execute(params);
//            }
//        });

        MainActivity.temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Search distance seekbar activity tracker
     */
    public static void trackSeekBar()
    {
        // Set MAX searchable distance in Kms
        MainActivity.seekBar.setMax(300);
        MainActivity.updated_field.setText("Search distance :" + String.valueOf(MainActivity.seekBar.getProgress()));

        // Register a listener to distance_seekbar
        MainActivity.seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        MainActivity.updated_field.setText("Search distance :" + MainActivity.seekBar_distance_covered.toString());
//						Toast.makeText(MainActivity.this, "Seekbar in stop tracking", Toast.LENGTH_SHORT).show();

                        // When dragstops then add the distance to the increment over longitude and lat.

                        // Invoke a method which fetch's new lat and long positions of this new searchable distance
                        // Feed this new longitude and lat to the get getWeatherInfo(Location aInLocation) and get new weather update
                        // then update UI components of new city weather on the MainActivity

                        MainActivity.currentLocation = WeatherInfo.updateLocation(MainActivity.currentLocation,
                                MainActivity.seekBar_distance_covered, 0);

                        // make a call to asyn weatherinfo method
                        new GetWeatherTask().execute(MainActivity.currentLocation);
//                        MainActivity.new GetWeatherTask().execute(MainActivity.currentLocation);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

//						Toast.makeText(MainActivity.this, "Seekbar in start tracking", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int currentProgress,
                                                  boolean fromUser) {

                        // Assign current progress value to our search distance variable
                        MainActivity.seekBar_distance_covered = currentProgress;
                        MainActivity.updated_field.setText("Search distance :" + currentProgress + " " + "Kms");

//						Toast.makeText(MainActivity.this, "Seekbar in progress tracking", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
