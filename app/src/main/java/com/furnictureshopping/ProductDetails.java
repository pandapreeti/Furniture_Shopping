package com.furnictureshopping;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a ItemActivity class , and it implements onCreate and convertMap_List
 * method.
 */

public class ProductDetails extends AppCompatActivity {
	private ArrayList<String> itemPricelist = new ArrayList<String>();
	private String pname,catgry;
	private String listitemname;
	private Integer[] noOfPiece = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private int piece;
	String cost,rating,desc,unm;
	ArrayList al = new ArrayList();
	ArrayList al1 = new ArrayList();
	private ImageView imageView;
	Object[] title, title1;
	JSONArray json1 = null, json2 = null;
	String[] titles, titles1;
	Button eWaranty;
	String imgname,iname1,rating1,dt,adrs,mno;
	private ProgressDialog pDialog, pDialog1, pDialog2;
	JSONParser jsonParser = new JSONParser();
	JSONParser jsonParser1 = new JSONParser();
	JSONParser jsonParser2 = new JSONParser();
	EditText cst;
	private static final String TAG_SUCCESS = "success";
	int sts;

	private static final String pdetails = "https://furnitureshoppingtamucc.com/FurnitureShopping/viewproductdetails.php";
	private static final String addcart= "https://furnitureshoppingtamucc.com/FurnitureShopping/addcart.php";
	private static final String viewcart= "https://furnitureshoppingtamucc.com/FurnitureShopping/count.php";
	/** Called when the activity is first created. */
	String discnt;
	private int mNotificationsCount = 0;
	private FloatingActionButton fab,fab1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.item);


		setTitle("Product Details");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		iname1 = b.getString("iname");
		catgry = b.getString("catgry");

		unm = b.getString("unm");
		setTitle(iname1);
		new productscost().execute();
		new ViewCart1().execute();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		java.util.Date date = new java.util.Date();
		 dt=dateFormat.format(date);
		//new productimage().execute();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		fab = (FloatingActionButton)findViewById(R.id.fab);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				new AddCart().execute();

			}
		});


		Spinner spinner = (Spinner) findViewById(R.id.SELECT_PIECE); // retrieving
		// the
		// spinner
		// component
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, noOfPiece);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				piece = (Integer) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView parent) {

			}
		});


//new History().execute();
	}



	private void setItemsonLayout() {
		TextView pname = (TextView) findViewById(R.id.ITEM_NAME);
		ImageView image = (ImageView) findViewById(R.id.ITEM_IMAGE);
		TextView catg = (TextView) findViewById(R.id.category);
		TextView rating1 = (TextView) findViewById(R.id.rating);
		TextView cno1 = (TextView) findViewById(R.id.cno);
		TextView discnttv = (TextView) findViewById(R.id.discnt);
		Glide.with(this).load(imgname)
				.crossFade()
				.placeholder(R.drawable.placeholder)
				.error(R.drawable.error)
				.thumbnail(0.5f)
				.diskCacheStrategy(DiskCacheStrategy.NONE)
				.skipMemoryCache(true)
				.into(image);
     pname.setText("Product Name:  " + iname1);
		catg.setText("Category: "+catgry);
		rating1.setText("Cost : "+cost+" $");
		cno1.setText("Description:  " + desc);
		discnttv.setText("Discount:  " + discnt +"%");
	}



	class productscost extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog1 = new ProgressDialog(ProductDetails.this);
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
			params.add(new BasicNameValuePair("iname",iname1));
			params.add(new BasicNameValuePair("catgry",catgry));
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(pdetails, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				itemPricelist.clear();

				json2 = json.getJSONArray("pdetail");
				// looping through All messages
				for (int i = 0; i < json2.length(); i++) {
					JSONObject c = json2.getJSONObject(i);
					// Storing each json item in variable

					catgry=c.getString("catgry") ;
					iname1=c.getString("iname");
					imgname=c.getString("imgurl");
					cost=c.getString("cost");
					desc=c.getString("des");
					discnt=c.getString("discnt");
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

			setItemsonLayout();

			pDialog1.dismiss();

			// updating UI from Background Thread


		}
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
				onBackPressed();


			default:
				break;

		}
		return true;
	}


	class AddCart extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetails.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 */
		protected String doInBackground(String... args) {
			// Building Parameters
			String s = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("catgry",catgry));
			params.add(new BasicNameValuePair("iname",iname1));
			params.add(new BasicNameValuePair("imgurl",imgname));
			params.add(new BasicNameValuePair("cost",cost));
			params.add(new BasicNameValuePair("discnt",discnt));
			params.add(new BasicNameValuePair("qty",new Integer(piece).toString()));
			params.add(new BasicNameValuePair("unm",unm));
			params.add(new BasicNameValuePair("dt",dt));
			// getting JSON string from URL
			JSONObject json = jsonParser1.makeHttpRequest(addcart, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 2) {

					sts=2;
				}

				if (success == 1) {
					// successfully created product

					// closing this screen
					sts=1;
					//finish();
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
		 **/
		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
			if(sts==1){

				Intent intent = new Intent(getApplicationContext(),ProductList.class);
				intent.putExtra("catgry", catgry);
				intent.putExtra("unm", unm);
				startActivity(intent);
				finish();
				Toast.makeText(getApplicationContext(),"Added to Cart Successfully",Toast.LENGTH_SHORT).show();

			}
			// updating UI from Background Thread


		}
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


	private void updateNotificationsBadge(int count) {
		mNotificationsCount = count;

		// force the ActionBar to relayout its MenuItems.
		// onCreateOptionsMenu(Menu) will be called again.
		invalidateOptionsMenu();
	}


	class ViewCart1 extends AsyncTask<String, String, Integer> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetails.this);
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


}
