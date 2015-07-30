package com.example.aakash.augrealtaketwo.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fetch weather info from location coordinates
 * Weather service used : OpenWeather Map.org
 * Update location algorithm inspired from Formula taken from MATH Forum ORG
 * (Link: http://mathforum.org/library/drmath/view/51816.html)
 *
 * Created by Ishan on 2015-07-27.
 */
public class WeatherInfo {
    // Final request :api.openweathermap.org/data/2.5/weather?lat=35&lon=139
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s";
    // Final image icon request: http://openweathermap.org/img/w/10d.png
    private static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";

    /**
     * Send Http Request using location coordinates parameters
     *
     * @param aInLocation - Location object on earth
     */
    public static String getWeatherInfo(Location aInLocation)
    {
        String lWeatherResponseStr = "";
        try
        {
            HttpURLConnection lConnection = null;
            URL lUrl = new URL(String.format(WEATHER_API_URL, String.valueOf(aInLocation.getLatitude()),
                    String.valueOf(aInLocation.getLongitude())));
            System.out.println(lUrl); //TODO: Remove later
            lConnection = (HttpURLConnection)lUrl.openConnection();
            lConnection.setRequestMethod("GET");
            lConnection.setDoInput(true);
            lConnection.setDoOutput(true);
            lConnection.connect();

            // Let's read the response
            StringBuilder lBuilder = new StringBuilder();
            InputStream lInputStream = lConnection.getInputStream();

            // Read content from input character stream
            BufferedReader lReader = new BufferedReader(
                    new InputStreamReader(lInputStream)); // Input stream reader - reads bytes and decodes them into characters

            String ltemp = "";


            while((ltemp = lReader.readLine()) != null)
            {
                lBuilder.append(ltemp);
            }

            lInputStream.close();
            System.out.println(lBuilder.toString());
            lWeatherResponseStr = lBuilder.toString();
            lConnection.disconnect();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return  lWeatherResponseStr;
    }

    /**
     * To get updated location after distance seekbar is adjusted
     *
     * Formula taken from MATH Forum ORG
     * (Link: http://mathforum.org/library/drmath/view/51816.html)
     *
     * @param location
     * @param seekBarDistance
     * @param compassPosition
     */
    public static Location updateLocation(Location location, double seekBarDistance, float compassPosition)
    {
        Location lReturnLocation = location;
        double lLat_radians = (location.getLatitude() * Math.PI)/180;
        double lLong_radians = (location.getLongitude() * Math.PI)/180;
        double lEarthRadius = 6371; // in Kms
        double lArc = seekBarDistance/lEarthRadius; // Convert distance to arc radians

        String lDirection = "";
        // Set direction according to compass position
        if(compassPosition <= 45.00 && compassPosition >= 315.00){lDirection = "N";}
        else if(compassPosition >= 45.01 && compassPosition <= 135.00){lDirection = "E";}
        else if(compassPosition >= 135.01 && compassPosition <= 225.00){lDirection = "S";}
        else if(compassPosition >= 225.01 && compassPosition <= 314.99){lDirection = "W";}

        // Heading in radians
        double tc=0; // Not sure what it is?
        if(lDirection == "N"){tc = 0;}
        else if(lDirection == "S"){tc = Math.PI;}
        else if(lDirection == "E"){tc = Math.PI/2;}
        else if(lDirection == "W"){tc = 3*Math.PI/2;}

        double lReturnLat, lReturnLong;
        lReturnLat = Math.asin(Math.sin(lLat_radians)* Math.cos(lArc)+ Math.cos(lLong_radians)*
                Math.sin(lArc)* Math.cos(tc));
        double dLon = Math.atan2(Math.sin(tc) * Math.sin(lArc) * Math.cos(lLat_radians),
                Math.cos(lArc) - Math.sin(lLat_radians) * Math.sin(lLat_radians));
        lReturnLong = ((lLong_radians + dLon + Math.PI) % (2 * Math.PI)) - Math.PI;

        // Convert back to degrees
        lReturnLat = (lReturnLat * 180)/Math.PI;
        lReturnLong = (lReturnLong * 180)/Math.PI;

        lReturnLocation.setLatitude(lReturnLat);
        lReturnLocation.setLongitude(lReturnLong);

        return lReturnLocation;
    }

    /**
     * TODO: Not being as we have internal weather image icon repository
     * Get weather icon according to icon code.
     *
     * @param aInIconCode : Weather icon code accprding to openweather API
     * @return
     */
    public static Bitmap getImageIcon(String aInIconCode) {

        HttpURLConnection lConnection = null;
        InputStream lInputStream = null;
        BufferedInputStream lBufferedInputStream = null;
        Bitmap lIconBitMap = null;
        try
        {
            URL lUrl = new URL(WEATHER_ICON_URL + aInIconCode + ".png");
            System.out.println(lUrl); //TODO: Remove later
            lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("GET");
            lConnection.setDoInput(true);
            lConnection.setDoOutput(true);
            lConnection.connect();

            // Now read response
            lInputStream =  lConnection.getInputStream();
            lBufferedInputStream = new BufferedInputStream(lInputStream, 1024*8);
            byte[] lBufferSize = new byte[1024];
            ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

          /*Reads a single byte from this stream and returns it as an integer in the
           *range from 0 to 255. Returns -1 if the end of the stream has been reached.
           */
            while(lBufferedInputStream.read(lBufferSize) != -1)
            {
                lOutputStream.write(lBufferSize, 0, lBufferedInputStream.read(lBufferSize));
            }

            byte[] lIconData =  lOutputStream.toByteArray();
            lIconBitMap = BitmapFactory.decodeByteArray(lIconData, 0, lIconData.length);

            lOutputStream.close();
            lBufferedInputStream.close();



        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try
            {
                lConnection.disconnect();
            }
            catch (Throwable tEx)
            {

            }
        }

        return lIconBitMap;
    }
}

