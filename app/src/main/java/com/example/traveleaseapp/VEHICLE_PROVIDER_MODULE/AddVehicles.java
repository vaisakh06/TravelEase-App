package com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.ADMIN_MODULE.AdminHomeActivity;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddVehicles extends AppCompatActivity {

    private EditText etVehicleName, etVehicleType, etBrand, etRegistrationNo,
            etSeatingCapacity, etLuggageCapacity, etFuelType,
            etVehicleColor, etRentalPrice;
    private Button btnUploadImage, btnSubmit;
    private ImageView ivVehicleImage;

    private static final int PICK_IMAGE = 1;
    private String imageBase64 = "";  // Store Base64-encoded image
    private Uri imageUri;
    private Bitmap selectedBitmap; // Store selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicles);

        // Initialize UI elements
        etVehicleName = findViewById(R.id.vehiclename);
        etVehicleType = findViewById(R.id.vtype);
        etBrand = findViewById(R.id.vbrand);
        etRegistrationNo = findViewById(R.id.registrationno);
        etSeatingCapacity = findViewById(R.id.seatingcapacity);
        etLuggageCapacity = findViewById(R.id.luggagecapacity);
        etFuelType = findViewById(R.id.fueltype);
        etVehicleColor = findViewById(R.id.vcolor);
        etRentalPrice = findViewById(R.id.rentalprice);
        ivVehicleImage = findViewById(R.id.vimage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Image Upload Button Click
        btnUploadImage.setOnClickListener(view -> openGallery());

        // Submit Button Click
        btnSubmit.setOnClickListener(view -> submitVehicleDetails());
    }

    // Open the gallery to select an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivVehicleImage.setImageBitmap(selectedBitmap);
                imageBase64 = convertBitmapToBase64(selectedBitmap); // Convert to Base64
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert Bitmap to Base64 string
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Validate inputs and submit vehicle details
    private void submitVehicleDetails() {
        String vehicleName = etVehicleName.getText().toString().trim();
        String vehicleType = etVehicleType.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String registrationNo = etRegistrationNo.getText().toString().trim();
        String seatingCapacity = etSeatingCapacity.getText().toString().trim();
        String luggageCapacity = etLuggageCapacity.getText().toString().trim();
        String fuelType = etFuelType.getText().toString().trim();
        String vehicleColor = etVehicleColor.getText().toString().trim();
        String rentalPrice = etRentalPrice.getText().toString().trim();

        // Validation: Ensure all fields are filled
        if (vehicleName.isEmpty() || vehicleType.isEmpty() || brand.isEmpty() ||
                registrationNo.isEmpty() || seatingCapacity.isEmpty() || luggageCapacity.isEmpty() ||
                fuelType.isEmpty() || vehicleColor.isEmpty() || rentalPrice.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an image is selected
        if (imageBase64.isEmpty()) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send vehicle details
        sendVehicleDetails(vehicleName, vehicleType, brand, registrationNo, seatingCapacity,
                luggageCapacity, fuelType, vehicleColor, rentalPrice, imageBase64);
    }

    // Send vehicle details via Volley StringRequest
    private void sendVehicleDetails(String vehicleName, String vehicleType, String brand, String registrationNo,
                                    String seatingCapacity, String luggageCapacity, String fuelType,
                                    String vehicleColor, String rentalPrice, String imageBase64) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            Toast.makeText(AddVehicles.this, "Vehicle added successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), VehicleProviderHome.class));
                            finish();
                        } else {
                            Toast.makeText(AddVehicles.this, "Failed: " + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = (error.getMessage() != null) ? error.getMessage() : "Unknown error";
                        Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("profileprefer", Context.MODE_PRIVATE);
                String usid = sharedPreferences.getString("UID", "");

                map.put("requestType", "AddVehicles");
                map.put("proid", usid);
                map.put("vehicle_name", vehicleName);
                map.put("vehicle_type", vehicleType);
                map.put("brand", brand);
                map.put("registration_no", registrationNo);
                map.put("seating_capacity", seatingCapacity);
                map.put("luggage_capacity", luggageCapacity);
                map.put("fuel_type", fuelType);
                map.put("vehicle_color", vehicleColor);
                map.put("rental_price", rentalPrice);
                map.put("vehicle_image", imageBase64); // Base64-encoded image

                return map;
            }
        };

        // Add request to the Volley queue
        queue.add(request);
    }
}
