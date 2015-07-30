package com.example.aakash.augrealtaketwo.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.aakash.augrealtaketwo.Model.DirectionCitiesMap;
import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;
import com.example.aakash.augrealtaketwo.View.ListNearByCities;
import com.example.aakash.augrealtaketwo.View.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Aakash on 7/16/2015.
 */
public class InvokeRESTImpl extends AsyncTask<String,Integer,String> {

    private final HttpClient client = new DefaultHttpClient();
    ProgressDialog dialog;
    Context mainScreenContext;
    String thisCallFor;
    public static String NEAR_BY_LOCATION = "nearByLocation";
    public static String WEATHER_INFO = "weatherInfo";
    Context activityContext;
    public InvokeRESTImpl(Context context, String callFor, MainActivity mainActivity)
    {
        this.activityContext = mainActivity;
        mainScreenContext = context;
        thisCallFor = callFor;

    }
    private String sendHTTPrequest(String aInUrl)
    {
        InputStream inputStream = null;
        String response ="";

        try {
            HttpResponse httpResponse = client.execute(new HttpGet(aInUrl));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream!=null)
                response =StreamToStringConversion(inputStream);

        } catch (IOException e) {
            Toast.makeText(mainScreenContext, "Error while sending API HTTP request:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return response;
    }


    protected void onPreExecute() {
        dialog = new ProgressDialog(activityContext);
        dialog.setIndeterminate(false);
        dialog.setTitle("Please wait !");
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private  String StreamToStringConversion(InputStream aInInputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aInInputStream));
        String response ="";
        String line = bufferedReader.readLine();
        while (line!=null)
        {
            response+=line;
            line=bufferedReader.readLine();
        }
        aInInputStream.close();
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s==null)
        {
            return;
        }

        if(thisCallFor.equalsIgnoreCase(InvokeRESTImpl.NEAR_BY_LOCATION)) {
            JSONParser jsonParser = new JSONParser(s);
            ArrayList<NearByPlacesInfo> listOfCityInfo = new ArrayList<NearByPlacesInfo>();
            for (NearByPlacesInfo nearByPlacesInfo : jsonParser.extractNearbyCities()) {
                listOfCityInfo.add(nearByPlacesInfo);
            }

            DirectionCitiesMap.clearListAndMap();
            DirectionCitiesMap directionCitiesMap = new DirectionCitiesMap(listOfCityInfo);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Intent showList = new Intent(mainScreenContext, ListNearByCities.class);
            showList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainScreenContext.startActivity(showList);
        }
        else if(thisCallFor.equalsIgnoreCase(InvokeRESTImpl.WEATHER_INFO))
        {
            //Call to weather API to be implemented here


        }

    }

    @Override
    protected String doInBackground(String... params) {

        if(params[0]!=null && !params[0].trim().equalsIgnoreCase(""))
            return sendHTTPrequest(params[0]);
        return null;
    }
}
