package com.example.aakash.augrealtaketwo.Services;

import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Aakash on 7/1/2015.
 */
public class JSONParser {
    String stringToBeParsed;
    public JSONParser(String inputString)
    {
        stringToBeParsed = inputString;
    }
    public ArrayList<NearByPlacesInfo> extractNearbyCities()
    {
        ArrayList<NearByPlacesInfo> nearByPlacesInfoList = new ArrayList<NearByPlacesInfo>();

        try {
         //   JSONObject jsonObject = new JSONObject(stringToBeParsed);
            JSONArray jsonArray= new JSONArray(stringToBeParsed.substring(2,stringToBeParsed.length()-2));
            if(jsonArray!=null)
            {
                int i=0;
                while(i < jsonArray.length())
                {
                    JSONArray localJsonArray = jsonArray.getJSONArray(i);
                    i++;
                    nearByPlacesInfoList.add(new NearByPlacesInfo(localJsonArray.getString(1),localJsonArray.getString(12),localJsonArray.getString(7),localJsonArray.getString(4),localJsonArray.getString(8),localJsonArray.getString(10)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nearByPlacesInfoList;
    }
}
