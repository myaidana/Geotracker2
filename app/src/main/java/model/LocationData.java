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

    public LocationData(double latitude, double longitude, double speed, String heading, String timestamp) {
        //mySourceID = id;
        setMyLatitude(latitude);
        myLongitude = longitude;
        mySpeed = speed;
        myHeading = heading;
        myTimestamp = timestamp;
    }


    public String getMySourceID() {
        return mySourceID;
    }


    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double latitude) {
        myLatitude = latitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double longitude) {
        myLongitude = longitude;
    }

    public double getMySpeed() {
        return mySpeed;
    }

    public void setMySpeed(double speed) {
        mySpeed = speed;
    }

    public String getMyHeading() {
        return myHeading;
    }

    public void setMyHeading(String heading) {
        myHeading = heading;
    }

    public String getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(String timestamp) {
        myTimestamp = timestamp;
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

