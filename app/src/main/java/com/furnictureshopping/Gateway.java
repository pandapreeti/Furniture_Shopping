package com.furnictureshopping;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Gateway extends AppCompatActivity {
    /** Called when the activity is first created. */
    private Spinner spinner,spinner1;
    String unm;
    private static final String TAG_SUCCESS = "success";
    int sts=0;
    private ProgressDialog pDialog, pDialog1, pDialog2;
    JSONParser jsonParser = new JSONParser();
    String total;
    private static String url_order ="https://furnitureshoppingtamucc.com/FurnitureShopping/order.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gateway);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        setTitle("Card Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        unm  =b.getString("unm");
        total=b.getString("total");
        }

    public void cash(View view) {

               new order().execute();

            }

    public void card(View view) {

        Intent i = new Intent(Gateway.this, Payment.class);
        i.putExtra("unm",unm);
        i.putExtra("total",total);
        startActivity(i);
    }

    // fill in the grid_item layout




    class order extends AsyncTask<Bitmap, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Gateway.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(Bitmap... img) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("unm",unm));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_order,
                    "POST", params);

            // check log cat for response
            Log.d("Response for Register", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 0) {
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
                Intent i = new Intent(Gateway.this,Feedback.class);
                i.putExtra("unm",unm);
                //  i.putExtra("name",name1);
                startActivity(i);
                //   Toast.makeText(getApplicationContext(), "Registered Successfully..!", Toast.LENGTH_SHORT).show();

            }
            if(sts==0) {

                Toast.makeText(getApplicationContext(), " Not Inserted", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }

    }

}

