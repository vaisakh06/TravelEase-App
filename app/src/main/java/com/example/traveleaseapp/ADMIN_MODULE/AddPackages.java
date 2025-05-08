package com.example.traveleaseapp.ADMIN_MODULE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPackages extends AppCompatActivity {

    EditText packageName, destiName, packagePrice, packageDuration, packageDescription, packageInclusions, packageExclusions, departureDate, returnDate;
    Button uploadImageButton, addPackageButton;
    ImageView packageImage;
    private static final int PICK_IMAGE = 1;
    String imgg = "";
    Uri imageUri;
    Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_packages);

        packageImage = findViewById(R.id.packageImage);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        addPackageButton = findViewById(R.id.addPackageButton);

        packageName = findViewById(R.id.packageName);
        destiName = findViewById(R.id.destiName);
        packagePrice = findViewById(R.id.packagePrice);
        packageDuration = findViewById(R.id.packageDuration);
        packageDescription = findViewById(R.id.packageDescription);
        packageInclusions = findViewById(R.id.packageInclusions);
        packageExclusions = findViewById(R.id.packageExclusions);
        departureDate = findViewById(R.id.departureDate);
        returnDate = findViewById(R.id.returnDate);

        uploadImageButton.setOnClickListener(v -> openGallery());

        // Date Pickers for Departure and Return Date
        departureDate.setOnClickListener(v -> showDatePicker(departureDate));
        returnDate.setOnClickListener(v -> showDatePicker(returnDate));

        // Add Package Button Click
        addPackageButton.setOnClickListener(v -> savePackageToDatabase());
    }

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
                packageImage.setImageBitmap(selectedBitmap);
                imgg = convertBitmapToBase64(selectedBitmap); // Convert image to Base64 string
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert Bitmap to Base64
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Method to show DatePickerDialog
    private void showDatePicker(EditText dateField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> dateField.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
        // ⛔ Disable past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    // Method to save package details to database
    private void savePackageToDatabase() {
        String name = packageName.getText().toString().trim();
        String destination = destiName.getText().toString().trim();
        String price = packagePrice.getText().toString().trim();
        String duration = packageDuration.getText().toString().trim();
        String description = packageDescription.getText().toString().trim();
        String inclusions = packageInclusions.getText().toString().trim();
        String exclusions = packageExclusions.getText().toString().trim();
        String depDate = departureDate.getText().toString().trim();
        String retDate = returnDate.getText().toString().trim();

        if (name.isEmpty() || destination.isEmpty() || price.isEmpty() || duration.isEmpty() ||
                description.isEmpty() || inclusions.isEmpty() || exclusions.isEmpty() || depDate.isEmpty() || retDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imgg.isEmpty()) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        addPackages(name, destination, price, duration, description, inclusions, exclusions, depDate, retDate, imgg);
    }

    private void addPackages(String name, String destination, String price, String duration,
                             String description, String inclusions, String exclusions,
                             String depDate, String retDate, String imageBase64) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url,
                response -> {
                    if (!response.trim().equalsIgnoreCase("failed")) {
                        Toast.makeText(AddPackages.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Volley Error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("requestType", "addPackages");
                params.put("name", name);
                params.put("destination", destination);
                params.put("price", price);
                params.put("duration", duration);
                params.put("description", description);
                params.put("inclusions", inclusions);
                params.put("exclusions", exclusions);
                params.put("departure_date", depDate);
                params.put("return_date", retDate);
                params.put("image", imageBase64); // Send Base64-encoded image
                return params;
            }
        };
        queue.add(request);
    }
}