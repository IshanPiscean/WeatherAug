package com.example.aakash.augrealtaketwo.Model;

/**
 * Created by Aakash on 7/1/2015.
 */
public class NearByPlacesInfo {
    private String placeName;
    private String placeState;
    private String placeDistance;
    private String placeDirection;
    private String placeLatitude;
    private String placeLongitude;

    public String getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(String placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public String getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(String placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public NearByPlacesInfo(String placeName, String placeState, String placeDistance, String placeDirection, String placeLatitude, String placeLongitude) {
        this.placeName = placeName;
        this.placeState = placeState;
        this.placeDistance = placeDistance;
        this.placeDirection = placeDirection;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceState() {
        return placeState;
    }

    public void setPlaceState(String placeState) {
        this.placeState = placeState;
    }

    public String getPlaceDistance() {
        return placeDistance;
    }

    public void setPlaceDistance(String placeDistance) {
        this.placeDistance = placeDistance;
    }

    public String getPlaceDirection() {
        return placeDirection;
    }

    public void setPlaceDirection(String placeDirection) {
        this.placeDirection = placeDirection;
    }
}
