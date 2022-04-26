package com.furnictureshopping;



import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Payment extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Bundle extras=null;
    EditText  card, dadrs, cvv, name,dtime,date,guest,speint;
    TextView cash;
    private RadioGroup payment;
    private RadioButton radioButton;
    Button btnDatePicker, btnTimePicker;
    private ProgressDialog pDialog;int sts;
    JSONParser jsonParser = new JSONParser();
    private static String url_order ="https://furnitureshoppingtamucc.com/FurnitureShopping/order.php";
    String pmode,name1,email1,badrs1,dadrs1,dtime1,cash1,guest1,speint1,mno1,rname,foodcatg,menu,date1,card1,cvv1,date11,menua,menub;
   // EditText txtDate, txtTime;
   double mcost,mcost1;
   private static final String TAG_SUCCESS = "success";
    private int mYear, mMonth, mDay, mHour;
    String total,unm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cash = (TextView) findViewById(R.id.cash);
        card = (EditText) findViewById(R.id.card);
        date = (EditText) findViewById(R.id.date);
        cvv = (EditText) findViewById(R.id.cvv);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        unm  = b.getString("unm");
total=b.getString("total");
        cash.setText("$"+total);
    }


    public void order(View v) {


        cash1=cash.getText().toString();
        card1 = card.getText().toString();
        date11 = date.getText().toString();
        cvv1= cvv.getText().toString();
        //Toast.makeText(this,date1,Toast.LENGTH_SHORT).show();


        if (null == card1 || card1.trim().length() == 0  ) {
            card.setError("Enter Card Number");
            card.requestFocus();
        } else if (card1.trim().length()==16  ) {
            card.setError("Card Number must be 16 digits ");
            card.requestFocus();
        } else if (null == date11 || date11.trim().length() == 0) {
            date.setError("Enter Date");
            date.requestFocus();
        }  else if (null == cvv1 || cvv1.trim().length() == 0) {
                cvv.setError("Enter CVV No.");
                cvv.requestFocus();
            } else if (cvv1.length() != 3) {
                cvv.setError("CVV No. Should be 3 digits.");
                cvv.requestFocus();

            } else {

            new order().execute();

                return;
            }

        
    }
    class order extends AsyncTask<Bitmap, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Payment.this);
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
                Intent i = new Intent(Payment.this,Feedback.class);
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