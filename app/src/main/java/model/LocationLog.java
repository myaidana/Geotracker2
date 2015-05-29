package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erevear on 5/15/15.
 */
public class LocationLog implements Parcelable {

    private List<LocationData> mLocationList;

    public LocationLog() {
        mLocationList = new ArrayList<>();
    }



    public LocationLog(Parcel in) {
        mLocationList = new ArrayList<>();
        mLocationList = in.readArrayList(LocationData.class.getClassLoader());
    }

    public void addLocation(LocationData location) {
        mLocationList.add(location);
    }

    public List<LocationData> getLocationList() {
        return mLocationList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mLocationList);
    }

    public static final Parcelable.Creator<LocationLog> CREATOR
            = new Parcelable.Creator<LocationLog>() {

        public LocationLog createFromParcel(Parcel in) {
            return new LocationLog(in);
        }

        public LocationLog[] newArray(int size) {
            return new LocationLog[size];
        }
    };

}

