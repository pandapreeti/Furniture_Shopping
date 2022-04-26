package com.furnictureshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_us);
    setTitle("About Us");
    TextView desc = findViewById(R.id.textView5);
    desc.setText("Furniture Shopping is the app to get exclusive deals on everything home. We’re talking major coupon bump-ups and deals on your favorite home brands - all available only on the Furniture Shopping app. All the things you want to improve the look of your home for less. Sign up for price alerts to shop rugs, living and dining room furniture, and garden and patio. You won’t find these home savings anywhere else on the internet. And deals drop daily right here on the Furniture Shopping app. We’ll even alert you when they do. Plus, with the Furniture Shopping app, get free shipping on EVERYTHING!");
  }
}