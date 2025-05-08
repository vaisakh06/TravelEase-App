package com.example.traveleaseapp.USER_MODULE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingPackages extends AppCompatActivity {

    TextView packageName, destination, departureDate, returnDate, price;
    EditText numTravellers, specialRequests;
    Button bookNow;
    String packageNameStr,packid,departureDateStr,destinationStr,returnDateStr,priceStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_packages);

        // Initialize Views
        packageName = findViewById(R.id.book_packname);
        destination = findViewById(R.id.book_destname);
        departureDate = findViewById(R.id.book_departuredate);
        returnDate = findViewById(R.id.book_returndate);
        price = findViewById(R.id.bookprice);
        numTravellers = findViewById(R.id.etNumTravellers);
        specialRequests = findViewById(R.id.etSpecialRequests);
        bookNow = findViewById(R.id.btnbooknow);

        // Fetch data from Intent
        Intent intent = getIntent();
        packid = intent.getStringExtra("package_id");

        packageNameStr = intent.getStringExtra("package_name");
        destinationStr = intent.getStringExtra("destination_name");
        departureDateStr = intent.getStringExtra("departure_date");
        returnDateStr = intent.getStringExtra("return_date");
        priceStr = intent.getStringExtra("price");

        packageName.setText(packageNameStr);
        destination.setText("Destination Name: "+destinationStr);
        departureDate.setText("Departure Date: "+departureDateStr);
        returnDate.setText("Return Date: "+returnDateStr);
        price.setText("Package Price: "+priceStr);


        // Booking Process
        bookNow.setOnClickListener(v -> {
            String travellers = numTravellers.getText().toString().trim();
            String requests = specialRequests.getText().toString().trim();

            if (TextUtils.isEmpty(travellers)) {
                numTravellers.setError("Enter number of travellers");
                return;
            }

            bookTravelPackage(travellers, requests);
        });
    }

    private void bookTravelPackage(String travellers, String requests) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(BookingPackages.this, "Booking Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Booking Failed: " + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                Log.e("Booking Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("profileprefer", Context.MODE_PRIVATE);
                String userId = prefs.getString("UID", "not_data");

                Map<String, String> params = new HashMap<>();
                params.put("requestType", "BookTravelPackage");
                params.put("uid", userId);
                params.put("packid", packid);
                params.put("package_name", packageNameStr);
                params.put("destination", destinationStr);
                params.put("departure_date", departureDateStr);
                params.put("return_date", returnDateStr);
                params.put("price", priceStr);
                params.put("num_travellers", travellers);
                params.put("special_requests", requests);
                return params;
            }
        };

        queue.add(request);
    }
}
