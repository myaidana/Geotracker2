package reception;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import data.LocationDB;
import model.Constants;

/**
 * Created by erevear on 5/16/15.
 */
public class SampleService extends IntentService {

    LocationDB mDB;
    String mUniqueID;


    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;




    /**
     * Constructor
     */
    public SampleService() {
        super("SampleService");
        mDB = new LocationDB(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        db = new LocationDB(this);
        mSharedPreferences = getSharedPreferences(Constants.sPreferences, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mUniqueID = mSharedPreferences.getString(Constants.sID, null);
        Log.d("USER", mUniqueID);
        //Log.d("INTENT", "intent " + intent.toString());

        //Log.i(TAG, "service starting");

        return START_STICKY;
    }
    /**
     * On handle intent method
     */
    @Override
    public void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();



        if (bundle == null) {
            return;
        }

        Location location = bundle.getParcelable("com.google.android.location.LOCATION");

        if (location == null) {
            return;
        }

        Log.d("LocationService", "Location " + location);
        onLocationReceived(location);

    }
    /**
     * On location received method to update database
     */
    public void onLocationReceived(Location loc){
        updateDB(loc);
    }
    /**
     * update method to update database
     */
    public void updateDB(Location location) {
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        try {
            mDB.insert(mUniqueID, location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing());

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * On destroy method to close database
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mDB.equals(null)) {
            mDB.close();
        }
    }

}
