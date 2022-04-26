package com.furnictureshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /** Called when the activity is first created. */
    String item,unm;
    private int mNotificationsCount = 0;
    private ProgressDialog pDialog, pDialog1, pDialog2;
    private static final String TAG_SUCCESS = "success";

    JSONParser jsonParser1 = new JSONParser();

    private static final String viewcart= "https://furnitureshoppingtamucc.com/FurnitureShopping/count.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhome);
        Bundle b = getIntent().getExtras();

        // retrieving item name from// intent.
        unm = b.getString("unm");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(UserHome.this);
        new ViewCart1().execute();
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("CARPETS & AREA RUGS");
        categories.add("FLOORING");
        categories.add("BED SHEETS");
        categories.add("CURTAINS & BLINDS");
        categories.add("TEAPOTS & KETTLES");
        categories.add("CHAIRS");
        categories.add("TABLES");
        categories.add("SOFA");
        categories.add("ENTERTAINMENT UNITS");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void search(View view) {

        Intent i=new Intent(UserHome.this,ProductList.class);
        i.putExtra("unm",unm);
        i.putExtra("catgry",item);
        startActivity(i);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:

                Intent i = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(i);
                break;


            case R.id.action_notifications:
                Intent i1 = new Intent(this, ViewCart.class);
                i1.putExtra("unm", unm);
                startActivity(i1);



                break;
            case R.id.history:
                Intent i2 = new Intent(this, History.class);
                i2.putExtra("unm",unm);
                startActivity(i2);

                break;

            case android.R.id.home:
                onBackPressed();


            default:
                break;

        }
        return true;
    }



    class ViewCart1 extends AsyncTask<String, String, Integer> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserHome.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Inbox JSON
         */
        protected Integer doInBackground(String... args) {
            // Building Parameters
            String s = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("unm",unm));

            // getting JSON string from URL
            JSONObject json = jsonParser1.makeHttpRequest(viewcart, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("JSON: ", json.toString());
            int cnt=0;
            try {
                int success = json.getInt(TAG_SUCCESS);
                if(success==1)
                    cnt = Integer.parseInt(json.getString("cnt"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return cnt;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(Integer cnt) {

            pDialog.dismiss();
            updateNotificationsBadge(cnt);
//Toast.makeText(UserHome.this,"call",Toast.LENGTH_SHORT).show();

        }
    }


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

}

