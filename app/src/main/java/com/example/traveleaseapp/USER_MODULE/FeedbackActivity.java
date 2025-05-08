package com.example.traveleaseapp.USER_MODULE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    EditText des;
    String sub, de, bid, rate;
    Button feed;
    RatingBar star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        des = findViewById(R.id.des);
        star = findViewById(R.id.startid);
        feed = findViewById(R.id.btnfeed);

        Intent i = getIntent();
        bid = i.getStringExtra("bid");

        feed.setOnClickListener(v -> {
            de = des.getText().toString().trim();
            rate = String.valueOf(star.getRating());

            if (de.isEmpty()) {
                showSnackbar(des, "Please Enter Description");
            } else if (rate.equals("0.0")) {
                showSnackbar(star, "Please Enter Rating");
            } else {
                sendFeedback();
            }
        });
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("DISMISS", v -> {})
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void sendFeedback() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, response -> {
            if (!response.trim().equals("failed")) {
                Toast.makeText(FeedbackActivity.this, "Feedback Sent successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "Failed: " + response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            Log.e("Volley Error", "Error: ", error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("profileprefer", MODE_PRIVATE);
                String uid = sharedPreferences.getString("UID", "");

                map.put("requestType", "UserSendFeed");
                map.put("descr", de);
                map.put("rating", rate);
                map.put("uid", uid);
                map.put("bid", bid);
                return map;
            }
        };
        queue.add(request);
    }
}
