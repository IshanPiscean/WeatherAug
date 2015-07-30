package com.example.aakash.augrealtaketwo.Services;

import android.location.Location;
import android.os.AsyncTask;

import com.example.aakash.augrealtaketwo.R;
import com.example.aakash.augrealtaketwo.View.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.aakash.augrealtaketwo.Model.Region;

/**
 * Created by Ishan on 2015-07-27.
 */
public class GetWeatherTask extends AsyncTask<Location, Void, String> {

    @Override
    protected String doInBackground(Location... locations) {
        Location lCityLocation = locations[0];

        // Location values in Regions class
        Region.setMyLocation_latitude(String.valueOf(lCityLocation.getLatitude()));
        Region.setMyLocation_longitude(String.valueOf(lCityLocation.getLongitude()));

        MainActivity.weatherJSONResponse = WeatherInfo.getWeatherInfo(lCityLocation);

        return MainActivity.weatherJSONResponse;
    }

    /**
     * Read JSON weather response
     * &
     * Set UI text elements
     */
    @Override
    protected void onPostExecute(String aInWeatherJsonStr)
    {
        try{

            // Get Root JSON Object
            JSONObject lWeatherJSONRootObj = new JSONObject(aInWeatherJsonStr);
            // Save city weather information
            Region.setMyCity(lWeatherJSONRootObj.getString("name").toUpperCase());

            // main object contains city temperature information
            JSONObject lWeatherMainObj = lWeatherJSONRootObj.getJSONObject("main");

            // Convert kelvin to Celsius temperature
            Region.setTemperature((float)lWeatherMainObj.getDouble("temp") - 273.15f);

            // Contains weather description ( Example: "Moderate rain")
            JSONArray lWeatherArray = lWeatherJSONRootObj.getJSONArray("weather");
            JSONObject lWeatherDescription = lWeatherArray.getJSONObject(0);
            Region.setWeather_description(lWeatherDescription.getString("description"));

            String lDegreeSymbol  = "\u00b0";
            // Display city weather information
            MainActivity.city_field.setText(Region.getMyCity());
            MainActivity.temperature.setText(String.valueOf(Math.round(Region.getTemperature())) + lDegreeSymbol + " " + "C");
            MainActivity.weather_description.setText(Region.getWeather_description());

            // Call weather image icon task
//                JSONObject lWeatherIconJsonObj = lWeatherArray.getJSONObject(3);
            String lWeatherIconCode = lWeatherDescription.getString("icon");
            if(lWeatherIconCode!=null){
            switch (lWeatherIconCode) {
                case "01d":
                    MainActivity.temperaturePic.setImageResource(R.drawable.sunny);
                    break;
                case "02d":
                case "03d":
                case "04d":
                    MainActivity.temperaturePic.setImageResource(R.drawable.sunnywithclouds);
                    break;
                case "09d":
                case "09n":
                case "10d":
                case "10n":
                    MainActivity.temperaturePic.setImageResource(R.drawable.lightrain);
                    break;
                case "11d":
                case "11n":
                    MainActivity.temperaturePic.setImageResource(R.drawable.thunder);
                    break;
                case "13d":
                case "13n":
                    MainActivity.temperaturePic.setImageResource(R.drawable.snow);
                    break;
                case "01n":
                case "02n":
                case "03n":
                case "04n":
                    MainActivity.temperaturePic.setImageResource(R.drawable.night);
                    break;
                case "50d":
                case "50n":
                    MainActivity.temperaturePic.setImageResource(R.drawable.mist);
                    break;
            }
            }

            //**TODO: We have internal image resource to display weather icons
//                new GetWeatherIconTask().execute(lWeatherIconCode);
//
//                if (Region.getIconData() != null && Region.getIconData().length > 0) {
//                    Bitmap img = BitmapFactory.decodeByteArray(Region.getIconData(), 0, Region.getIconData().length);
//                    weatherImageView.setImageBitmap(img);
//                }

        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
        }

    }
}
