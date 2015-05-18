package controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import eva_aidana.geotracker.R;
import model.SampleManager;
import reception.UploadService;

/**
 * Created by erevear on 5/16/15.
 */
public class MyProfileActivity extends Activity {


    private Button mBack;

    private Button mStartActivity;
    private Button mEndActivity;
    private Button mSubmitSample;
    private Button mSubmitUpload;

    private EditText mSampleRate;
    private EditText mUploadRate;

    private SampleManager sampleManager;

    private String mUserID;
    private int mPollInterval;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private String MyPREFERENCES = "myPrefs";
    private String UserUniqueID = "userUniqueID";
    private String mPollTime = "myPollTime";

    private ConnectivityManager mConnectivityManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);



        mBack = (Button)findViewById(R.id.back_to_filter);

        mStartActivity = (Button)findViewById(R.id.startservice);
        mEndActivity = (Button)findViewById(R.id.endservice);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mUserID = sharedpreferences.getString(UserUniqueID, null);
        mPollInterval = sharedpreferences.getInt(mPollTime, 6000);

        mSampleRate = (EditText)findViewById(R.id.sample_rate);
        mSubmitSample = (Button)findViewById(R.id.changesampling);

        mUploadRate = (EditText)findViewById(R.id.upload_rate);
        mSubmitUpload = (Button)findViewById(R.id.changeuploading);



        //sampleManager = new SampleManager(getApplicationContext(), mUserID, mPollInterval);
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);







        mStartActivity.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(isConnected == true) {
                    SampleManager.startIntent(v.getContext(), true);
                    UploadService.setServiceAlarm(v.getContext(), true);

                } else {
                    Toast.makeText(getApplicationContext(), "Connect to a network to start service", Toast.LENGTH_SHORT).show();
                }

            }

        });



        mEndActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SampleManager.startIntent(v.getContext(), false);
                UploadService.setServiceAlarm(v.getContext(), false);

            }

        });




        mSubmitSample.setOnClickListener(new View.OnClickListener() {
            long sampleRate;


            @Override
            public void onClick(View v) {
                sampleRate = Integer.parseInt(mSampleRate.getText().toString());

                if(sampleRate >= 10 && sampleRate <= 300 ) {
                    sampleRate = sampleRate * 1000;
                    SampleManager.startIntent(v.getContext(), false);
                    SampleManager.changePolling(sampleRate);
                    SampleManager.startIntent(v.getContext(), true);

                    //editor.putInt(mPollTime, sampleRate);
                } else {
                    Toast.makeText(getApplicationContext(), "Sample rates must be between 10 seconds " +
                            "and 300 seconds", Toast.LENGTH_LONG).show();
                }

            }

        });

        mSubmitUpload.setOnClickListener(new View.OnClickListener() {
            long uploadRate;

            @Override
            public void onClick(View v) {
                uploadRate = Integer.parseInt(mUploadRate.getText().toString());

                if(uploadRate >= 1 && uploadRate <= 24 ) {
                    uploadRate = uploadRate * 3600000;
                    UploadService.setServiceAlarm(v.getContext(), false);
                    SampleManager.changePolling(uploadRate);
                    UploadService.setServiceAlarm(v.getContext(), true);

                    //editor.putInt(mPollTime, sampleRate);
                } else {
                    Toast.makeText(getApplicationContext(), "Sample rates must be between 1 hour " +
                            "and 1 day", Toast.LENGTH_LONG).show();
                }

            }

        });



    }






}
