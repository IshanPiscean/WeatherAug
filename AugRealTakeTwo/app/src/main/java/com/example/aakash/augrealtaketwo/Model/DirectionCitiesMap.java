package com.example.aakash.augrealtaketwo.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aakash on 7/1/2015.
 */
public class DirectionCitiesMap {

    public static ArrayList<String> availableDirectionsList = new ArrayList<String>();
    public static HashMap<String,ArrayList<NearByPlacesInfo>> directionToCitiesMap = new HashMap<String, ArrayList<NearByPlacesInfo>>();

    public static void clearListAndMap()
    {
        if(availableDirectionsList!=null && availableDirectionsList.size()>0)
        availableDirectionsList.clear();
        if(directionToCitiesMap!=null && directionToCitiesMap.size()>0)
        directionToCitiesMap.clear();
    }
    public DirectionCitiesMap(ArrayList<NearByPlacesInfo> aInInputObject)
    {
        for(NearByPlacesInfo nearByPlaces : aInInputObject)
        {
            if(!availableDirectionsList.contains(nearByPlaces.getPlaceDirection()))
                {
                    availableDirectionsList.add(nearByPlaces.getPlaceDirection());
                        if(directionToCitiesMap.containsKey(nearByPlaces.getPlaceDirection()))
                        {
                            directionToCitiesMap.get(nearByPlaces.getPlaceDirection()).add(nearByPlaces);
                        }
                        else
                        {
                            ArrayList<NearByPlacesInfo> listOfCities = new ArrayList<NearByPlacesInfo>();
                            listOfCities.add(nearByPlaces);
                            directionToCitiesMap.put(nearByPlaces.getPlaceDirection(),listOfCities);
                        }
                }
            else
            {
                if(directionToCitiesMap.containsKey(nearByPlaces.getPlaceDirection()))
                {
                    directionToCitiesMap.get(nearByPlaces.getPlaceDirection()).add(nearByPlaces);
                }
                else
                {
                    ArrayList<NearByPlacesInfo> listOfCities = new ArrayList<NearByPlacesInfo>();
                    listOfCities.add(nearByPlaces);
                    directionToCitiesMap.put(nearByPlaces.getPlaceDirection(),listOfCities);
                }

            }

        }
    }
}
