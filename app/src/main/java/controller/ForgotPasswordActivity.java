package controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import eva_aidana.geotracker.R;

/**
 * Created by erevear on 5/15/15.
 */
public class ForgotPasswordActivity extends Activity {

    private static final String URL = "http://450.atwebpages.com/reset.php?";
    private final static String EMAIL_URL = "email=";

    EditText mEmail;
    Button mSubmit;
    /**
     * On create method to initialize instances
     */
    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        mEmail = (EditText)findViewById(R.id.enter_name);

        mSubmit = (Button)findViewById(R.id.submit_email);

        mSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();




                String myURL = URL + EMAIL_URL + email;

                Log.d("URL ", myURL);

                ForgotPasswordTask task = new ForgotPasswordTask();

                task.execute(new String[]{myURL});



            }
        });


    }

    private class ForgotPasswordTask extends AsyncTask<String, Void, JSONObject> {





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog = ProgressDialog.show(CreateAccountActivitywWebServices.this, "Wait", "Downloading...");
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject data = new JSONObject();
            String result = "";

            for (String url : urls) {
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
                        String feedback = data.getString("result");
                        Log.d("Http Response:", data.getString("result"));
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
        /**
         * On post execute method for web services
         */
        @Override
        public void onPostExecute(JSONObject feedback) {
            try {
                if (feedback.getString("result").equals("fail")) {
                    String error = feedback.getString("error");
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                } else if (feedback.getString("result").equals("success")) {
                    Toast.makeText(getApplicationContext(), feedback.getString("message"), Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
