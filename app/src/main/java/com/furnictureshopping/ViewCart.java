package com.furnictureshopping;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCart extends AppCompatActivity implements AdapterView.OnItemClickListener{
	EditText  rno,pwd;

	String date,pwd1;

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
	private static final String url_delete= "https://furnitureshoppingtamucc.com/FurnitureShopping/delete.php";
	private static final String url ="https://furnitureshoppingtamucc.com/FurnitureShopping/viewcart.php";
	TextView tv,total;
	int sts=0,sts3=0;
	String tot="tot";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_cart);
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		unm  = b.getString("unm");
		///pname  = b.getString("pname");
		new productlist().execute();

		listView = (ListView)findViewById(R.id.list);
		total=(TextView)findViewById(R.id.total);

		listView.setOnItemClickListener(this);

		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility(View.GONE);

		setTitle("Cart List");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(ViewCart.this, Gateway.class);
				i.putExtra("unm",unm);
				i.putExtra("total",totalamnt);
				startActivity(i);
				//Toast.makeText(ViewCart.this,totalamnt,Toast.LENGTH_SHORT).show();

			}
		});

	}
	class productlist extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog1 = new ProgressDialog(ViewCart.this);
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
					totalamnt=json.getString("total");
					json2 = json.getJSONArray("pdetail");
					// looping through All messages
					for (int i = 0; i < json2.length(); i++) {
						JSONObject c = json2.getJSONObject(i);
						// Storing each json item in variable
						ProductView cart = new ProductView();
						cart.setTitle(c.getString("rname"));
						cart.setThumbnailUrl(c.getString("imgurl"));
						cart.setRating("Cost: " + c.getString("cost") + "$");
						cart.setGenre("Quantity: " + c.getString("qty"));
						cart.setYear(c.getString("discnt"));
						cart.setDate(c.getString("date"));
						productList.add(cart);
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

			adapter = new CustomListAdapter(ViewCart.this, productList,getApplicationContext());
			listView.setAdapter(adapter);


			if(sts==2){

				tv.setVisibility(View.VISIBLE);
				tv.setText("No Records Found");
			}else{
				//Toast.makeText(ViewCart.this,"t="+totalamnt,Toast.LENGTH_SHORT).show();
				total.setText("Total: $" + totalamnt);
			}
			// updating UI from Background Thread

			pDialog1.dismiss();

		}


	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		ProductView p=(ProductView)productList.get(position);
		date=p.getDate();

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewCart.this);
		// Setting Dialog Title

		// Setting Dialog Message
		alertDialog.setMessage("Do you want Remove the product item?");
		// Setting Icon to Dialog
		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new deletePHP().execute();

			}
		});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();







	}


	class deletePHP extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog2 = new ProgressDialog(ViewCart.this);
			pDialog2.setMessage("Processing ....");
			pDialog2.setIndeterminate(false);
			pDialog2.setCancelable(false);
			pDialog2.show();
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
			params.add(new BasicNameValuePair("date", date));
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(url_delete, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				//itemPricelist.clear();
				int success = json.getInt("success");


				if (success == 1) {
					sts3=1;

				}else{
					sts3=2;
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


			if(sts3==1){

				Toast.makeText(getApplicationContext(), "Product item deleted..!", Toast.LENGTH_SHORT).show();
				new productlist().execute();


			}
			pDialog2.dismiss();

		}


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
