package reception;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import data.LocationDB;

/**
 * Created by erevear on 5/16/15.
 */
public class SampleService extends IntentService {

    LocationDB db;
    String mUniqueID;


    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;



    private String MyPREFERENCES = "myPrefs";
    private String UserUniqueID = "userUniqueID";
    /**
     * Constructor
     */
    public SampleService() {
        super("SampleService");
        db = new LocationDB(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        db = new LocationDB(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mUniqueID = sharedpreferences.getString(UserUniqueID, null);
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

        // mUniqueID = intent.getStringExtra("userid");
        Location loc = (Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(loc != null){
            onLocationReceived(loc);
            Log.d("LOCATION", loc.toString());


        }
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
            db.insert(mUniqueID, location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing());

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
        if (!db.equals(null)) {
            db.close();
        }
    }

}
