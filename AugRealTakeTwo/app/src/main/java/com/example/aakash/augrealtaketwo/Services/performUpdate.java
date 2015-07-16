package com.example.aakash.augrealtaketwo.Services;

import android.os.AsyncTask;

import com.example.aakash.augrealtaketwo.View.MainActivity;
import com.example.aakash.augrealtaketwo.R;

/**
 * Created by Aakash on 6/22/2015.
 */
public class performUpdate extends AsyncTask<Integer, Integer, String[]>
{

    @Override
    protected String[] doInBackground(Integer... params) {
        String[] updateView = new String[10];
        int i = params[0];
        // Where all the info fetched from webservice would come in
        updateView[0]=Integer.toString(params[0]);
        updateView[1]="Sunny";
        updateView[2]="Going to get hotter soon";
        updateView[3]="Kanata";
        updateView[4]="31st May 2015";
        return updateView;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        MainActivity.temperature.setText(strings[0]);
        MainActivity.weather_icon.setText(strings[1]);
        MainActivity.city_field.setText(strings[3]);
        MainActivity.updated_field.setText(strings[4]);
        MainActivity.temperaturePic.setImageResource(R.drawable.sunnywithclouds);
    }
}