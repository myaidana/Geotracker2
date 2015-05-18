package reception;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import data.LocationDB;

/**
 * Created by erevear on 5/16/15.
 */
public class UploadService extends IntentService {

    private LocationDB db;
    private static final String URL = "http://450.atwebpages.com/logAdd.php";
    private static final String AND = "&";
    private static long mPollInterval = 60000;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public UploadService() {
        super("UploadService");
        db = new LocationDB(this);
        //mPollInterval = 60000;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("UPLOADRECEIVER", "service starting");

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Cursor c = db.getAllData();
        //db.deleteAll();
        String[] myURLs = new String[c.getCount()];

        int rows = c.getCount();

        c.moveToFirst();
        int i = 0;
        while(!c.isAfterLast()){
            //Log.d("PUSH DATA", "Pushing");
            myURLs[i] = URL+"?lat="+c.getString(1)+AND+"lon="+ c.getString(2)+AND+"speed="+
                    c.getString(3)+AND + "heading="+c.getString(4)+AND + "timestamp="+c.getString(5) +AND
                    + "source=" + c.getString(0);
            Log.d("UPLOADSERVICE", myURLs[i]);
            i++;
            c.moveToNext();


        }
        db.deleteAll();
        UploadLocationDataTask task = new UploadLocationDataTask();

        task.execute(myURLs);



    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, UploadService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , mPollInterval, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static void setUploadPoll(long pollingInterval) {
        mPollInterval = pollingInterval;
    }


    private class UploadLocationDataTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog = ProgressDialog.show(CreateAccountActivitywWebServices.this, "Wait", "Downloading...");
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            Log.d("DATA PUSH", "doing in background");
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
                            Log.d("UPLOADSERVICE", "not uploaded");
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    } if (feedback.getString("result").equals("success")) {
                        //startCollectData();
                        //Log.i("Success", "starts collecting data");
                        Toast.makeText(getApplicationContext(), feedback.getString("result"), Toast.LENGTH_LONG).show();
                         Log.d("UPLOADSERVICE", "uploaded");
                        // Intent i = new Intent(CreateAccountActivitywWebServices.this, LoginActivitywWebServices.class);
                        // startActivity(i);


                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

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
