package controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eva_aidana.geotracker.R;
import model.LocationData;
import model.LocationLog;

/**
 * Created by erevear on 5/15/15.
 */
public class FilterDatesActivity extends Activity {
    private static final String VIEW_URL = "http://450.atwebpages.com/view.php?";

    static final int START_DATE_DIALOG_ID = 1;
    static final int END_DATE_DIALOG_ID = 2;
    static final int START_TIME_DIALOG_ID = 3;
    static final int END_TIME_DIALOG_ID = 4;

    private Button mSubmit;



    private Button mStartDate;
    private Button mStartTime;

    private Button mEndDate;
    private Button mEndTime;

    private String mStartFilter;
    private String mEndFilter;

    private Button mMyProfile;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private String MyPREFERENCES = "myPrefs";
    private String UserUniqueID = "userUniqueID";

    private String mUniqueID;

    private TextView mSeeStartDate;
    private TextView mSeeStartTime;

    private TextView mSeeEndDate;
    private TextView mSeeEndTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_data);

        mSubmit = (Button)findViewById(R.id.submitfilter);


        mStartDate = (Button)findViewById(R.id.pickstartdate);
        mStartTime = (Button)findViewById(R.id.pickstarttime);

        mEndDate = (Button)findViewById(R.id.pickenddate);
        mEndTime = (Button)findViewById(R.id.pickendtime);

        mSeeStartDate = (TextView)findViewById(R.id.chosenstartdate);
        mSeeStartTime = (TextView)findViewById(R.id.chosenstarttime);

        mSeeEndDate = (TextView)findViewById(R.id.chosenenddate);
        mSeeEndTime = (TextView)findViewById(R.id.chosenendtime);

        mMyProfile = (Button)findViewById(R.id.myprofile);


        calendar = Calendar.getInstance();
        mStartYear = calendar.get(Calendar.YEAR);
        mStartMonth = calendar.get(Calendar.MONTH);
        mStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        mStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartMinute = calendar.get(Calendar.MINUTE);

        mEndYear = calendar.get(Calendar.YEAR);
        mEndMonth = calendar.get(Calendar.MONTH);
        mEndDay = calendar.get(Calendar.DAY_OF_MONTH);
        mEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        mEndMinute = calendar.get(Calendar.MINUTE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mUniqueID = sharedpreferences.getString(UserUniqueID, null);



        mStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(START_DATE_DIALOG_ID);

            }

        });

        mMyProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i= new Intent(FilterDatesActivity.this, MyProfileActivity.class);
                startActivity(i);

            }

        });

        mEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(END_DATE_DIALOG_ID);

            }

        });

        mStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(START_TIME_DIALOG_ID);

            }

        });

        mEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(END_TIME_DIALOG_ID);

            }

        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            String startDate;
            String endDate;
            long startStamp;
            long endStamp;

            String myURL;


            @Override
            public void onClick(View v) {

                startDate = mStartYear + "-" + mStartMonth + "-" + mStartDay + " " + mStartHour + ":" + mStartMinute;
                endDate = mEndYear + "-" + mEndMonth  + "-" + mEndDay + " " + mEndHour + ":" + mEndMinute;
                //Log.d("FILTERDATES", "start: " + startDate + " end: " + endDate);
                startStamp = makeTimeStamp(startDate);
                endStamp = makeTimeStamp(endDate);
                //Log.d("FILTERDATES", "start: " + startStamp + " end: " + endStamp);
                myURL = VIEW_URL + "uid=" + mUniqueID +"&start=" + startStamp + "&end="+endStamp;
                Log.d("FILTERDATES", myURL);
                GetLocationDataTask task = new GetLocationDataTask();
                task.execute(new String[]{myURL});

            }

        });






    }


    private long makeTimeStamp(String date) {
        Log.d("FILTERDATES", date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date theDate = null;
        try {
            theDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timestamp = theDate.getTime()/1000;
        return timestamp;

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, StartDatePickerListener,
                        mStartYear, mStartMonth,mStartDay);
            case END_DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, EndDatePickerListener,
                        mEndYear, mEndMonth,mEndDay);

            case START_TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        StartTimePickerListener, mStartHour, mStartMinute,false);

            case END_TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        EndTimePickerListener, mEndHour, mEndMinute,false);

        }
        return null;
    }





    private DatePickerDialog.OnDateSetListener StartDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        String date;

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            mStartYear = selectedYear;
            mStartMonth = selectedMonth + 1;
            mStartDay = selectedDay;
            date = mStartYear + "-" + mStartMonth + "-" + mStartDay;
            mSeeStartDate.setText(date);


            // set selected date into textview



        }
    };



    private DatePickerDialog.OnDateSetListener EndDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        String date;

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            mEndYear = selectedYear;
            mEndMonth = selectedMonth;
            mEndDay = selectedDay;

            date = mEndYear + "-" + mEndMonth + "-" + mEndDay;
            mSeeEndDate.setText(date);


        }
    };


    private TimePickerDialog.OnTimeSetListener StartTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                String time;
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    mStartHour = selectedHour;
                    mStartMinute = selectedMinute;
                    time = mStartHour + ":" + mStartMinute;
                    mSeeStartTime.setText(time);



                }
            };

    private TimePickerDialog.OnTimeSetListener EndTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                String time;
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    mEndHour = selectedHour;
                    mEndMinute = selectedMinute;

                    time = mEndHour + ":" + mEndMinute;
                    mSeeEndTime.setText(time);



                }
            };



    private class GetLocationDataTask extends AsyncTask<String, Void, JSONObject> {
        LocationLog log = new LocationLog();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog = ProgressDialog.show(CreateAccountActivitywWebServices.this, "Wait", "Downloading...");
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            //Log.d("DATA PUSH", "doing in background");
            JSONObject data = new JSONObject();
            String result = "";

            for (String url : urls) {
                //Log.d("DATA PUSH", "pushing URL");
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(url);


                try {
                    HttpResponse response = client.execute(httpGet);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        result += s;
                    }

                    try {
                        //String json = EntityUtils.toString(response.getEntity());
                        data = new JSONObject(result);
                        //String feedback = data.getString("result");
                        //Log.d("Http Response:", data.getString("result"));
                        //provideFeedback(data);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // System.out.print("response " + response.toString());
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block




                } catch (Exception e) {

                    e.printStackTrace();
                }



            }

            return data;
        }

        @Override
        public void onPostExecute(JSONObject feedback) {
            try {
                if (feedback.has("result")) {

                    try {
                        if (feedback.getString("result").equals("fail")) {


                            String error = feedback.getString("error");
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    } if (feedback.getString("result").equals("success")) {
                        Toast.makeText(getApplicationContext(), "yay", Toast.LENGTH_LONG).show();
                        JSONArray points = feedback.getJSONArray("points");
                        for(int i = 0; i < points.length(); i++) {
                            JSONObject point = points.getJSONObject(i);
                            long lat = point.getLong("lat");
                            long lon = point.getLong("lon");
                            String heading = point.getString("heading");
                            long speed = point.getInt("speed");
                            String time = point.getString("time");
                            LocationData loc = new LocationData(lat, lon, speed,
                                    heading, time);
                            log.addLocation(loc);
                            Log.d("FILTERDATES", "points: " + lat);
                        }

                        Intent i = new Intent(FilterDatesActivity.this, DisplayDataActivity.class);
                        i.putExtra("locations", log);

                        startActivity(i);


                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



}
