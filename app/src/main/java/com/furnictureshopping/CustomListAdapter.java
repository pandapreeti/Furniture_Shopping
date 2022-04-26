package com.furnictureshopping;



import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private ImageView imageView;
	Context mcontext;
	private List<ProductView> movieItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<ProductView> movieItems,Context context) {
		this.activity = activity;
		this.movieItems = movieItems;
		this.mcontext=context;
	}

	@Override
	public int getCount() {
		return movieItems.size();
	}

	@Override
	public Object getItem(int location) {
		return movieItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		ImageView thumbNail =  convertView
				.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		imageView = (ImageView)convertView.findViewById(R.id.imageView);
		TextView saveOffer = convertView.findViewById(R.id.saveOffer);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		ProductView m = movieItems.get(position);

		// thumbnail image
		Glide.with(mcontext).load(m.getThumbnailUrl()).into(thumbNail);
		//thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// title
		title.setText(m.getTitle());
		
		// rating
		rating.setText(m.getRating());
		genre.setText(m.getGenre());
		saveOffer.setText("Save "+m.getYear()+"%");
		// genre
		/*String genreStr = "";
		for (String str : m.getGenre()) {
			genreStr += str + ", "
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;
		genre.setText(genreStr);*/
		
		// release year
		//year.setText(String.valueOf(m.getYear()));
//		LabelView label = new LabelView(mcontext);
//		label.setText("Save\n"+m.getYear()+"%");
//		label.setBackgroundColor(0xffE91E63);
//		label.setTargetView(convertView.findViewById(R.id.imageView), 10,
//				LabelView.Gravity.RIGHT_TOP);

		//parent.addView(convertView);


		return convertView;
	}

}