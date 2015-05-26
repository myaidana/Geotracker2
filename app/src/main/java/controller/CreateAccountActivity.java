package controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

/**
 * Created by erevear on 5/15/15.
 */
public class CreateAccountActivity extends Activity {


    private final static String EMAIL_URL = "email=";
    private final static String PASSWORD_URL = "password=";
    private final static String QUESTION_URL = "question=";
    private final static String ANSWER_URL = "answer=";
    private static final String URL = "http://450.atwebpages.com/adduser.php?";
    private static final String AGREEMENT_URL = "http://450.atwebpages.com/agreement.php";

    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserQuestion;
    private EditText mUserAnswer;
    private Button mSaveData;

    private CheckBox mAgree;
    private TextView mAgreement;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String mEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        CreateAccountTask task = new CreateAccountTask();

        task.execute(new String[]{AGREEMENT_URL});

        mSharedPreferences = getSharedPreferences(Constants.sPreferences, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mUserEmail = (EditText) findViewById(R.id.useremail);
        mUserPassword = (EditText) findViewById(R.id.userpassword);
        mUserQuestion = (EditText) findViewById(R.id.usersecurityquestion);
        mUserAnswer = (EditText) findViewById(R.id.usersecurityanswer);

        mAgree = (CheckBox)findViewById(R.id.Agreement);

        mAgreement = (TextView)findViewById(R.id.agreement_text);


        mSaveData = (Button) findViewById(R.id.terms);

        mSaveData.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mEmail = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();
                String question = mUserQuestion.getText().toString().trim().replace(" ", "%20").replace("?", "%3F");
                String answer = mUserAnswer.getText().toString().trim().replace(" ", "%20");


                String myURL = URL + EMAIL_URL + mEmail + "&" + PASSWORD_URL + password + "&" + QUESTION_URL + question + "&" +
                        ANSWER_URL + answer;

                Log.d("URL ", myURL);
                if (mAgree.isChecked()) {
                    CreateAccountTask task = new CreateAccountTask();

                    task.execute(new String[]{myURL});

                } else {
                    Toast.makeText(getApplicationContext(), "You must agree to the terms and" +
                            " conditions before continuing", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }



    private class CreateAccountTask extends AsyncTask<String, Void, JSONObject> {
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
                        //String feedback = data.getString("result");
                        // Log.d("Http Response:", data.getString("result"));
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
                if (feedback.has("result")) {

                    try {
                        if (feedback.getString("result").equals("fail")) {


                            String error = feedback.getString("error");
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    if (feedback.getString("result").equals("success")) {
                        mEditor.putLong(mEmail + " " + Constants.sSampleRate, 60000);
                        mEditor.putLong(mEmail + " " + Constants.sUploadRate, 60000);
                        mEditor.commit();
                        Log.d("CREATEACCOUNT", mEmail);
                        //startCollectData();
                        //Log.i("Success", "starts collecting data");
                        Toast.makeText(getApplicationContext(), feedback.getString("message"), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        startActivity(i);


                    }
                } else if (feedback.has("agreement")) {
                    String agreement = feedback.getString("agreement");
                    agreement = agreement.replace("<h2>", "");
                    agreement  = agreement.replace("</h2>", "");
                    agreement = agreement.replace("<h3>", "");
                    agreement = agreement.replace("</h3>", "");
                    agreement = agreement.replace("<p>", "");
                    agreement = agreement.replace("</p>", "");
                    agreement = agreement.replace("<ol type=\"a\">", "");
                    agreement = agreement.replace("<li>", "");
                    agreement = agreement.replace("<ol type=\"i\">", "");
                    agreement = agreement.replace("</li>", "");
                    agreement = agreement.replace("</ol>", "");
                    agreement = agreement.replace("<ul>", "");
                    agreement = agreement.replace("</ul>", "");


                    mAgreement.setText(agreement);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
