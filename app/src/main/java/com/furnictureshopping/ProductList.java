package com.furnictureshopping;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductList extends AppCompatActivity implements AdapterView.OnItemClickListener{
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
String pname,catgry;JSONObject json;

	private static final String url ="https://furnitureshoppingtamucc.com/FurnitureShopping/viewprodctlist.php";
	private static final String viewcart= "https://furnitureshoppingtamucc.com/FurnitureShopping/count.php";
	TextView tv;
	int sts=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		unm  = b.getString("unm");
		catgry  = b.getString("catgry");


		new productlist().execute();
		new ViewCart1().execute();
		listView = (ListView)findViewById(R.id.list);

		listView.setOnItemClickListener(ProductList.this);

		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility(View.GONE);

		setTitle("Product List");
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

	}
	class productlist extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog1 = new ProgressDialog(ProductList.this);
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
			params.add(new BasicNameValuePair("catgry",catgry));
			// getting JSON string from URL
		 json = jsonParser.makeHttpRequest(url, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("Results:", json.toString());

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
						movie.setRating("Cost: " + c.getString("cost") + " $");
						movie.setGenre("Category: " + c.getString("catgry"));
						movie.setYear( c.getString("discnt"));
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

			//Toast.makeText(ProductList.this,"RES"+sts,Toast.LENGTH_LONG).show();


			adapter = new CustomListAdapter(ProductList.this, productList,getApplicationContext());
			listView.setAdapter(adapter);
			pDialog1.dismiss();


			if(sts==2){

				tv.setVisibility(View.VISIBLE);
				tv.setText("No Records Found");
			}
			// updating UI from Background Thread


		}


	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {


ProductView p=(ProductView)productList.get(position);
		String iname=p.getTitle();
		Intent intent=new Intent(ProductList.this,ProductDetails.class);
		intent.putExtra("iname",iname);
		intent.putExtra("catgry", catgry);
		intent.putExtra("unm", unm);
		startActivity(intent);

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
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
				break;

			case R.id.action_notifications:
				Intent i1 = new Intent(this, ViewCart.class);
				i1.putExtra("unm",unm);
				startActivity(i1);
				break;


			case android.R.id.home:
				Intent intent = new Intent(getApplicationContext(),UserHome.class);
				intent.putExtra("unm", unm);
				startActivity(intent);
				finish();

			case R.id.aboutUs:
				Intent historyIntent = new Intent(this,AboutUs.class);
				startActivity(historyIntent);
				finish();


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
			pDialog = new ProgressDialog(ProductList.this);
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


		}
	}



	private void updateNotificationsBadge(int count) {
		mNotificationsCount = count;

		// force the ActionBar to relayout its MenuItems.
		// onCreateOptionsMenu(Menu) will be called again.
		invalidateOptionsMenu();
	}












}
