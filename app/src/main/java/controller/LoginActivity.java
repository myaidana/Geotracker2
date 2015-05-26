package controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import model.Constants;


public class LoginActivity extends Activity {

    private static final String URL = "http://450.atwebpages.com/login.php?";
    private final static String EMAIL_URL = "email=";
    private final static String PASSWORD_URL = "password=";
    private EditText mEmail;
    private EditText mPassword;
    Button mLogin;
    Button mCreateAccount;
    Button mForgotPassword;



    private String mUserID;
    private String mUserEmail;


    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

//    private SampleManager sampleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getSharedPreferences(Constants.sPreferences, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mEmail = (EditText)findViewById(R.id.name);
        mPassword = (EditText)findViewById(R.id.password);
        mLogin = (Button)findViewById(R.id.loginbutton);
        mCreateAccount = (Button)findViewById(R.id.createaccountbutton);
        mForgotPassword = (Button)findViewById(R.id.forgotpasswordbutton);

        mCreateAccount.setOnClickListener(new View.OnClickListener() {

            /**
             * On click method to start intent activity
             */
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {

            /**
             * On click method to start intent activity
             */
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * On click method to start intent activity
             */
            @Override
            public void onClick(View v) {
                mUserEmail = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String myURL = URL + EMAIL_URL + mUserEmail + "&" + PASSWORD_URL + password;


//                mEditor.putString(Constants.sEmail, mEmail.getText().toString().trim());
//                //editor.putString(Password,  mPassword.getText().toString().trim());
//
//                mEditor.commit();

                LoginTask task = new LoginTask();
                task.execute(new String[]{myURL});

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoginTask extends AsyncTask<String, Void, JSONObject> {
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
         * On post execute method to start intent activity
         */
        @Override
        public void onPostExecute(JSONObject feedback) {
            try {
                if (feedback.getString("result").equals("fail")) {
                    String error = feedback.getString("error");
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                } else {
                    if (feedback.getString("result").equals("success")) {
                        Toast.makeText(getApplicationContext(), "Welcome to Geotracker!", Toast.LENGTH_SHORT).show();

                        mUserID = feedback.getString("userid");

                        mEditor.putString(Constants.sEmail, mUserEmail);
                        mEditor.putString(Constants.sID, mUserID);
                        mEditor.putString(Constants.sLoggedIn, "true");
                        mEditor.commit();
//                        sampleManager = new SampleManager(getApplicationContext(), mUserID);
//                        sampleManager.startIntent();

//                        accountActivity = new AccountActivity(getApplicationContext(), mUserID);
//                        accountActivity.startIntent();
//
//
                        Intent i = new Intent(LoginActivity.this, FilterDatesActivity.class);
                        //i.putExtra("UserID", mUserID);
                        startActivity(i);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
