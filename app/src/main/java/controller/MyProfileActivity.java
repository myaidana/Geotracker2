package controller;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import eva_aidana.geotracker.R;
import model.Constants;
import reception.SampleService;
import reception.UploadService;


/**
 * Created by erevear on 5/19/15.
 * Edited by anurla92
 */
public class MyProfileActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "MyProfileActivity";
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-updates-key";

    private long mPollInterval;
    //public static final long FASTEST_INTERVAL = 10000;
    private long mFastPollInterval;

    private Button mBack;

    private Button mStartUpdates;
    private Button mStopUpdates;

    private Button mChangeSampleRate;
    private EditText mSampleRate;

    private Button mChangeUploadRate;
    private EditText mUploadRate;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    protected Boolean mRequestingUpdates;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String mEmail;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
//        if (savedInstanceState != null) {
//            // Restore value of members from saved state
//            mRequestingUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
//        } else {
//            mRequestingUpdates = false;
//        }

        mSharedPreferences = getSharedPreferences(Constants.sPreferences, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mEmail = mSharedPreferences.getString(Constants.sEmail, null);
        Log.d(TAG, mEmail);
        mPollInterval = mSharedPreferences.getLong(mEmail + " " + Constants.sSampleRate, 0);
        mFastPollInterval = mPollInterval/2;

        mBack = (Button)findViewById(R.id.back_to_filter);

        mStartUpdates = (Button)findViewById(R.id.startservice);
        mStopUpdates = (Button)findViewById(R.id.endservice);

        mSampleRate = (EditText)findViewById(R.id.sample_rate);
        mChangeSampleRate = (Button)findViewById(R.id.changesampling);

        mUploadRate = (EditText)findViewById(R.id.upload_rate);
        mChangeUploadRate = (Button)findViewById(R.id.changeuploading);




        mRequestingUpdates = mSharedPreferences.getBoolean(Constants.sServiceOn, false);
        setupButtons();





        ComponentName receiver = new ComponentName(getApplicationContext(), UploadService.class);
        PackageManager pm = getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);




        //updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();


        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i= new Intent(MyProfileActivity.this, FilterDatesActivity.class);
                startActivity(i);

            }

        });



        mStartUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!mRequestingUpdates) {
                    mRequestingUpdates = true;
                    Log.d("MYPROFILE", "requesting updates from start update = " + mRequestingUpdates);
                    //UploadService.setServiceAlarm(getApplicationContext(), true);
                    startUploadService();
                    startLocationUpdates();
                    setupButtons();
                    Log.d(TAG, "poll interval " + mPollInterval);
                }
            }
        });

        mStopUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(mRequestingUpdates) {
                    mRequestingUpdates = false;
                    stopLocationUpdates();
                    stopUploadService();
                    setupButtons();

                }


            }

        });

        mChangeSampleRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mSampleRate.getText().toString().length()>1 && mSampleRate.getText().toString().length()<4){
                    mFastPollInterval = Long.parseLong(mSampleRate.getText().toString());
                }

                if(mFastPollInterval >= 10 && mFastPollInterval <= 300) {

                    mFastPollInterval = mFastPollInterval * 1000;
                    Log.d(TAG, "Fast Poll Interval " + mFastPollInterval);
                    mPollInterval = mFastPollInterval * 2;
                    mEditor.putLong(mEmail + " " + Constants.sSampleRate, mPollInterval);
                    //mEditor.putLong("fast_poll_interval", mFastPollInterval);
                    mEditor.commit();

                    if(mRequestingUpdates) {
                        stopLocationUpdates();
                        mLocationRequest.setInterval(mPollInterval);
                        mLocationRequest.setFastestInterval(mFastPollInterval);
                        startLocationUpdates();
                    } else {
                        mLocationRequest.setInterval(mPollInterval);
                        mLocationRequest.setFastestInterval(mFastPollInterval);

                    }
                    Toast.makeText(getApplicationContext(), "Sample rate changed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You must enter a time between 10 " +
                            "and 300 seconds", Toast.LENGTH_SHORT).show();
                }




            }

        });

        mChangeUploadRate.setOnClickListener(new View.OnClickListener() {
            long uploadInterval;

            @Override
            public void onClick(View v) {

                uploadInterval = Long.parseLong(mUploadRate.getText().toString());
                //if(uploadInterval >= 1 && uploadInterval <= 24) {

                Log.d(TAG, "Upload Interval " + uploadInterval);

                mEditor.putLong(mEmail + " " + Constants.sUploadRate, uploadInterval);
                //mEditor.putLong("fast_poll_interval", mFastPollInterval);
                mEditor.commit();

                //UploadService.setServiceAlarm(getApplicationContext(), false);
                if(mRequestingUpdates) {
                    stopUploadService();

                    startUploadService();
                } else {
                    UploadService.setUploadPoll(uploadInterval);

                }

                //UploadService.setServiceAlarm(getApplicationContext(), true);

//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "You must enter a time between 1 " +
//                            "and 24 hours", Toast.LENGTH_SHORT).show();
//                }


                Toast.makeText(getApplicationContext(), "Upload rate changed", Toast.LENGTH_SHORT).show();

            }

        });





    }
    private void setupButtons() {
        if(mRequestingUpdates) {
            mStartUpdates.setEnabled(false);
            mStopUpdates.setEnabled(true);

        } else {
            mStopUpdates.setEnabled(false);
            mStartUpdates.setEnabled(true);
        }
    }
//    private void updateValuesFromBundle(Bundle savedInstanceState) {
//        Log.i(TAG, "Updating values from bundle");
//        if (savedInstanceState != null) {
//            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
//            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
//            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
//                mRequestingUpdates = savedInstanceState.getBoolean(
//                        REQUESTING_LOCATION_UPDATES_KEY);
//                setupButtons();
//                Log.d("MYPROFILE", "requesting updates from update bundle = " + mRequestingUpdates);
//
//            }
//        }
//    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(mPollInterval);

        mLocationRequest.setFastestInterval(mFastPollInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }




    protected void startLocationUpdates() {
        Intent intent = new Intent(this, SampleService.class);
        PendingIntent pendingIntent = PendingIntent
                .getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, pendingIntent);


    }

    protected void stopLocationUpdates() {
        Intent intent = new Intent(this, SampleService.class);
        PendingIntent pendingIntent = PendingIntent
                .getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, pendingIntent);
    }

    private void startUploadService() {
//        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
//        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;
//        ConnectivityManager cm =
//                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();

        //if(isCharging && isConnected) {
        long pollRate = mSharedPreferences.getLong(mEmail + " " + Constants.sUploadRate, 0);
        UploadService.setUploadPoll(pollRate);
        UploadService.setServiceAlarm(getApplicationContext(), true);
        //} else {
//            Toast.makeText(getApplicationContext(), "Your phone must be plugged in and" +
//                    "connected to a network in order to allow the upload portion of this " +
//                    "application", Toast.LENGTH_SHORT).show();
        //}



    }

    private void stopUploadService() {
        UploadService.setServiceAlarm(getApplicationContext(), false);
    }



    @Override
    protected void onStart() {
        super.onStart();


        Log.d(TAG, "on start requesting updates is " + mRequestingUpdates);
        //mRequestingUpdates = mSharedPreferences.getBoolean(Constants.sServiceOn, false);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();


        Log.d(TAG, "on stop requesting updates is " + mRequestingUpdates);
        mEditor.putBoolean(Constants.sServiceOn, mRequestingUpdates);
        mEditor.commit();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();


        Log.d(TAG, "on pause requesting updates is " + mRequestingUpdates);
        mEditor.putBoolean(Constants.sServiceOn, mRequestingUpdates);
        mEditor.commit();
        mGoogleApiClient.connect();
    }





    @Override
    public void onConnected(Bundle bundle) {
        if(mRequestingUpdates) {
            Log.d(TAG, "on connected requesting updates is " + mRequestingUpdates);

            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Log.d(TAG, "on connection suspended requesting updates is " + mRequestingUpdates);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    @Override
//    public void onLocationChanged(Location location) {
//        mLocation = location;
//        Log.d(TAG, "the location " + mLocation);
//
//    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        Log.d(TAG, "on saveinstancestate requesting updates is " + mRequestingUpdates);
//
//
//        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingUpdates);
//
//        super.onSaveInstanceState(savedInstanceState);
//    }
}