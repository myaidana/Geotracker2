package model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import reception.SampleService;

/**
 * Created by erevear on 5/16/15.
 */
public class SampleManager {


    private Context mContext;

    //private LocationDB mLocDB;
    //private static LocationManager mLocationManager;


    private String mUserID;
    private static long mPollInterval = 60000;







    public SampleManager(Context context, String userid, int pollInterval) {
        mContext = context;
        //mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        //mLocDB = new LocationDB(mContext);
        mUserID = userid;
        //mPollInterval = pollInterval;



    }

//    private PendingIntent getPendingIntent(boolean create) {
//        //Intent intent = new Intent(mContext, SampleService.class);
//        //intent.putExtra("userid", mUniqueID);
//        //int flags = create ? 0 : PendingIntent.FLAG_NO_CREATE;
//        //PendingIntent pi = PendingIntent.getService(mContext, 0, intent, flags);
//        return pi;
//    }

    public static void startIntent(Context context, boolean intentOn) {
        Log.d("SAMPLEMANAGER", "start intent");
        LocationManager mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        Intent intent = new Intent(context, SampleService.class);

        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        if (intentOn) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mPollInterval, 0, pi);
        } else {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }


        boolean enabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("LOCATION PROVIDER", "provider is " + enabled);


    }

//    public static void stopIntent(Context context) {
//
//        Intent intent = new Intent(context, SampleService.class);
//
//        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
//        mLocationManager.removeUpdates(pi);
//        pi.cancel();
//    }



//    public boolean isTracking() {
//        return getPendingIntent(false) != null;
//    }

    public static void changePolling(long pollTime) {
        //stopIntent();
        mPollInterval = pollTime;
        Log.d("SAMPLEMANAGER", "poll interval " + mPollInterval);
        //startIntent();
    }

    public static long returnPolling() {
        //stopIntent();
        return mPollInterval;
        //startIntent();
    }
}


