package model;

import java.io.Serializable;

/**
 * Created by erevear on 5/15/15.
 */
public class LocationData implements Serializable {

    private String mySourceID;
    private double myLatitude;
    private double myLongitude;
    private double mySpeed;
    private String myHeading;
    private String myTimestamp;
    private static final String ID = "id", LATITUDE = "latitude", LONGITUDE = "longitude",
            SPEED = "speed", HEADING = "heading", TIMESTAMP = "timestamp";

    public LocationData(double latitude, double longitude, double speed, String heading, String timestamp){
        //mySourceID = id;
        myLatitude = latitude;
        myLongitude = longitude;
        mySpeed = speed;
        myHeading = heading;
        myTimestamp = timestamp;
    }


    public String getMySourceID() {
        return mySourceID;
    }

    public void setMySourceID(String mySourceID) {
        this.mySourceID = mySourceID;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }

    public double getMySpeed() {
        return mySpeed;
    }

    public void setMySpeed(double mySpeed) {
        this.mySpeed = mySpeed;
    }

    public String getMyHeading() {
        return myHeading;
    }

    public void setMyHeading(String myHeading) {
        this.myHeading = myHeading;
    }

    public String getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(String myTimestamp) {
        this.myTimestamp = myTimestamp;
    }

    @Override
    public String toString() {
        return "com.example.aidana.geotracker.model.Location{" +
                "mySourceId='" + mySourceID + '\'' +
                ", myLatitude='" + myLatitude + '\'' +
                ", myLongitude='" + myLongitude + '\'' +
                ", mySpeed=" + mySpeed +
                ", myHeading=" + myHeading +
                ", myTimestamp" + myTimestamp +
                '}';
    }

}

