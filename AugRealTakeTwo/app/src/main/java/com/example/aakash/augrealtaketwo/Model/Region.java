package com.example.aakash.augrealtaketwo.Model;

import android.location.Location;

/**
 * Created by Ishan on 2015-07-27.
 */
public class Region {
    private static String myCity;
    private static float temperature;
    private static String weather_description;
    // City weather instance
    private static Region myWeather = null;

    private static Location myLocation;

    private static String myLocation_latitude;

    private static String myLocation_longitude;

    private static byte[] iconData;

    private Region(){
        // Private constructor
    }

    public static synchronized Region getWeatherInstance()
    {
        if(myWeather == null)
        {
            myWeather = new Region();
        }

        return myWeather;
    }

    public static String getMyCity() {
        return myCity;
    }
    public static void setMyCity(String myCity) {
        Region.myCity = myCity;
    }
    public static float getTemperature() {
        return temperature;
    }
    public static void setTemperature(float temperature) {
        Region.temperature = temperature;
    }
    public static String getWeather_description() {
        return weather_description;
    }
    public static void setWeather_description(String weather_description) {
        Region.weather_description = weather_description;
    }

    public static Location getMyLocation() {
        return myLocation;
    }

    public static void setMyLocation(Location myLocation) {
        Region.myLocation = myLocation;
    }

    public static String getMyLocation_latitude() {
        return myLocation_latitude;
    }

    public static void setMyLocation_latitude(String myLocation_latitude) {
        Region.myLocation_latitude = myLocation_latitude;
    }

    public static String getMyLocation_longitude() {
        return myLocation_longitude;
    }

    public static void setMyLocation_longitude(String myLocation_longitude) {
        Region.myLocation_longitude = myLocation_longitude;
    }

    public static byte[] getIconData() {
        return iconData;
    }

    public static void setIconData(byte[] iconData) {
        Region.iconData = iconData;
    }
}
