package com.furnictureshopping;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    EditText unm, pwd, email, mno, nm;
    SQLiteDatabase db;
    int sts=0;
    String rno1,unm1,pwd1,email1,mno1;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    // url to create new product
    private static String url_StuRegister ="https://furnitureshoppingtamucc.com/FurnitureShopping/custreg.php";
    String[] listItems;
    String[] listmovies;
    boolean[] checkedItems;
    boolean[] checkedmovies;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<Integer> moviesItems = new ArrayList<>();
    Button food,movies;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);


        unm = (EditText) findViewById(R.id.unm);
        pwd = (EditText) findViewById(R.id.passwd_edtext);

        mno = (EditText) findViewById(R.id.mno);
        email = (EditText) findViewById(R.id.empid);
        setTitle("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void register(View v) {


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String unmpattern="[A-Za-z]+";


        unm1 = unm.getText().toString();
        pwd1 = pwd.getText().toString();
        mno1 = mno.getText().toString();
        email1 = email.getText().toString();
        if (null == unm1 || unm1.trim().length() == 0) {
            unm.setError("Enter User Name");
            unm.requestFocus();
        }   else if (null == pwd1 || pwd1.trim().length() == 0) {
            pwd.setError("Enter Your Password");
            pwd.requestFocus();
        }
      else if (null == email1 || email1.trim().length() == 0) {
        email.setError("Enter Your Email Id");
        email.requestFocus();
    }
        else if (!email.getText().toString().trim().matches(emailPattern)) {

            Toast.makeText(this,"Incorect Email Id Entry ",Toast.LENGTH_LONG).show();
        }
        else  if (null == mno1 || mno1.trim().length() == 0) {
            mno.setError("Enter Your Mobile No.");
            mno.requestFocus();
        } else {
            if (mno1.length() != 10) {
                mno.setError("Invalid Mobile Number.");
                mno.requestFocus();
            } else {


                new Register().execute();

                // Toast.makeText(getApplicationContext(), "Registered Successfully..!", Toast.LENGTH_SHORT).show();

                // finish();
                return;
            }



        }







    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();


            default:
                break;

        }
        return true;
    }
    class Register extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registration.this);
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("unm",unm1));
            params.add(new BasicNameValuePair("pwd",pwd1));
            params.add(new BasicNameValuePair("mno",mno1));
            params.add(new BasicNameValuePair("email",email1));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_StuRegister,
                    "POST", params);

            // check log cat for response
            Log.d("Response for Register", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 2) {

                    sts=2;
                }

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("status","success");
                    startActivity(i);
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

                Toast.makeText(getApplicationContext(), "Registered Successfully..!", Toast.LENGTH_LONG).show();
            }
            if(sts==2) {

                Toast.makeText(getApplicationContext(), "User Name Already Exist..!", Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }

    }



}
