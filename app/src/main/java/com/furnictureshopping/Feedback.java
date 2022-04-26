package com.furnictureshopping;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Feedback extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Bundle extras=null;
    EditText fb, card, dadrs, cvv, namee,dtime,date,guest,speint;
    private RadioGroup payment;
    private RadioButton radioButton;
    Button btnDatePicker, btnTimePicker;
    String unm;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_order ="https://furnitureshoppingtamucc.com/FurnitureShopping/feedback.php";
    int sts;
    private static final String TAG_SUCCESS = "success";
    String fb1,card1,cvv1,badrs1,dadrs1,dtime1,date1,guest1,rname,name;
   // EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fb = (EditText) findViewById(R.id.fb);

    }


    public void submit(View v) {


       fb1=fb.getText().toString();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        unm  = b.getString("unm");

        //Toast.makeText(this,date1,Toast.LENGTH_SHORT).show();


        if (null == fb1 || fb1.trim().length() == 0  ) {
            fb.setError("Enter Feed back");
            fb.requestFocus();
        }  else {

                new feedback().execute();

                return;
            }

        
    }
    class feedback extends AsyncTask<Bitmap, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Feedback.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(Bitmap... img) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("unm",unm));

            params.add(new BasicNameValuePair("fb",fb1));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_order,
                    "POST", params);

            // check log cat for response
            Log.d("Response for Register", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 2) {
                }

                if (success == 1) {
                    // successfully created product

                    //  Toast.makeText(getApplicationContext(),"suces",Toast.LENGTH_SHORT).show();
                    // closing this screen
                    sts=1;
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(sts==1) {
                Intent i = new Intent(Feedback.this,UserHome.class);
                i.putExtra("unm",unm);
                startActivity(i);
                  Toast.makeText(getApplicationContext(), "Thanks for Your Feedback", Toast.LENGTH_SHORT).show();

            }
            if(sts==2) {

                Toast.makeText(getApplicationContext(), " Not Inserted", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }

    }



}