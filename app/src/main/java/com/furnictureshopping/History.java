package com.furnictureshopping;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener{
	EditText  rno,pwd;

	String unm1,pwd1;

	private Spinner spinner;
	String subject,unm;
	private CustomListAdapter adapter;
	JSONArray json1 = null, json2 = null;
	Location location;
	double latitude;
	double longitude;
	float dist;
	String totalamnt;
	double mLatitude=0;
	double mLongitude=0;
	private List<ProductView> productList = new ArrayList<ProductView>();
	private ListView listView;
	private int mNotificationsCount = 0;
	private ProgressDialog pDialog, pDialog1, pDialog2;
	JSONParser jsonParser = new JSONParser();
	JSONParser jsonParser1 = new JSONParser();
	JSONParser jsonParser2 = new JSONParser();
	private static final String TAG_SUCCESS = "success";

	private FloatingActionButton fab,fab1;
	// url to create new product
String pname,catgry;

	private static final String url ="https://furnitureshoppingtamucc.com/FurnitureShopping/viewhistory.php";
	TextView tv,total;
	int sts=0;
	String tot="tot";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		unm  = b.getString("unm");
		///pname  = b.getString("pname");
		new productlist().execute();

		listView = (ListView)findViewById(R.id.list);
		total=(TextView)findViewById(R.id.total);

		//listView.setOnItemClickListener(this);

		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility(View.GONE);

		setTitle("History");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}
	class productlist extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog1 = new ProgressDialog(History.this);
			pDialog1.setMessage("Loading ...");
			pDialog1.setIndeterminate(false);
			pDialog1.setCancelable(false);
			pDialog1.show();
		}

		/**
		 * getting Inbox JSON
		 */
		protected String doInBackground(String... args) {
			// Building Parameters
			String s = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("pname",pname));
			params.add(new BasicNameValuePair("unm", unm));
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				//itemPricelist.clear();
				int success = json.getInt("success");

				productList.clear();
				if (success == 1) {
					sts=1;

					json2 = json.getJSONArray("pdetail");
					// looping through All messages
					for (int i = 0; i < json2.length(); i++) {
						JSONObject c = json2.getJSONObject(i);
						// Storing each json item in variable
						ProductView movie = new ProductView();
						movie.setTitle(c.getString("rname"));
						movie.setThumbnailUrl(c.getString("imgurl"));
						movie.setRating("Cost: " + c.getString("cost") + "$");
						movie.setGenre("Quantity: " + c.getString("qty"));
						movie.setYear(c.getString("discnt"));
						productList.add(movie);
					}
				}else{
					sts=2;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		protected void onPostExecute(String file_url) {

			adapter = new CustomListAdapter(History.this, productList,getApplicationContext());
			listView.setAdapter(adapter);
			pDialog1.dismiss();

			if(sts==2){

				tv.setVisibility(View.VISIBLE);
				tv.setText("No Records Found");
			}else{
				//Toast.makeText(ViewCart.this,"t="+totalamnt,Toast.LENGTH_SHORT).show();
				//total.setText("Total: " + totalamnt + " Rs.");
			}
			// updating UI from Background Thread


		}


	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {


			case android.R.id.home:
				Intent intent = new Intent(getApplicationContext(),UserHome.class);

				intent.putExtra("unm", unm);
				startActivity(intent);
				finish();


			default:
				break;

		}
		return true;
	}



}
