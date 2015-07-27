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
        MainActivity.weather_icon = (TextView) view.findViewById(R.id.weather_icon);
        MainActivity.seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        MainActivity.nearByButton = (ImageButton) view.findViewById(R.id.imageButton);
        MainActivity.temperaturePic = (ImageView) view.findViewById(R.id.temperaturePic);
        MainActivity.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int distanceFromLocation = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceFromLocation = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //temperature.setText(Integer.toString(distanceFromLocation));
                Integer[] params = new Integer[2];
                params[0] = distanceFromLocation;
                new performUpdate().execute(params);
            }
        });




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


}
