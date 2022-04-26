package com.furnictureshopping;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import  android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    EditText  uid,pwd;
    private static final String TAG_SUCCESS = "success";
    String unm1,pwd1;
    private static String url_login ="https://furnitureshoppingtamucc.com/FurnitureShopping/custlogin.php";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    Button fab;
    boolean sts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uid = (EditText)findViewById(R.id.uid);
        pwd = (EditText)findViewById(R.id.pwd);
        fab= (Button)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Registration.class);

                startActivity(i);

            }
        });


    }

    public void login(View view){


        unm1 = uid.getText().toString();
        pwd1 = pwd.getText().toString();
        // Toast.makeText(getActivity(),unm1+""+pwd1,Toast.LENGTH_SHORT).show();
        if (null == unm1 || unm1.trim().length() == 0) {
            uid.setError("Enter User Name");
            uid.requestFocus();
        }
        else if (null == pwd1 || pwd1.trim().length() == 0) {
            pwd.setError("Enter Your Password");
            pwd.requestFocus();
        } else {
            new Login().execute();

            return;
        }



    }
    class Login extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login Verification...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("unm",unm1));
            params.add(new BasicNameValuePair("pwd",pwd1));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login,"POST",params);

            // check log cat for response
            Log.d("Result for Login", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    Intent i=new Intent(MainActivity.this,UserHome.class);
                    i.putExtra("unm",unm1);
                    startActivity(i);

                    sts=true;
                } else {
                    sts=false;
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

            if(!sts){
                Toast.makeText(MainActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }

    }






}