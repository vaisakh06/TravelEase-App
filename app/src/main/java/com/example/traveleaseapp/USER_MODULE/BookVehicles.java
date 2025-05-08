package com.example.traveleaseapp.USER_MODULE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class BookVehicles extends AppCompatActivity {

    EditText etDepartureDate, etReturnDate;
    Button btnBookNow;
    String proid, vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_vehicles);

        // Initialize Views
        etDepartureDate = findViewById(R.id.etDepartureDate);
        etReturnDate = findViewById(R.id.etReturnDate);
        btnBookNow = findViewById(R.id.btnbooknow);

        // Set Click Listeners for Date Pickers
        etDepartureDate.setOnClickListener(v -> showDatePicker(etDepartureDate));
        etReturnDate.setOnClickListener(v -> showDatePicker(etReturnDate));

        Intent i = getIntent();
        proid = i.getExtras().getString("proid");
        vid = i.getExtras().getString("vid");

        // Handle Booking Button Click
        btnBookNow.setOnClickListener(v -> {
            String departureDate = etDepartureDate.getText().toString().trim();
            String returnDate = etReturnDate.getText().toString().trim();

            if (departureDate.isEmpty()) {
                Toast.makeText(this, "Please select a departure date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (returnDate.isEmpty()) {
                Toast.makeText(this, "Please select a return date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with Booking Process
//            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show();
            bookTravelPackage(departureDate, returnDate);
        });
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year, month, dayOfMonth) ->
                editText.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Expiry Date");
        datePickerDialog.show();
    }

    private void bookTravelPackage(String departureDate, String returnDate) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(BookVehicles.this, "Booking Successful!", Toast.LENGTH_SHORT).show();
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
                params.put("requestType", "BookVehicles");
                params.put("uid", userId);
                params.put("proid", proid);
                params.put("vid", vid);
                params.put("departure_date", departureDate);
                params.put("return_date", returnDate);
                return params;
            }
        };

        queue.add(request);
    }
}