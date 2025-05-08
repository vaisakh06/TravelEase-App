package com.example.traveleaseapp.USER_MODULE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.ADAPTOR.UserVehiclesAdaptor;
import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiclesActivity extends AppCompatActivity {

    private ListView deslist;
    private List<TravelEaseModel> arraylist;
    private UserVehiclesAdaptor adap;
    private String proid,bid,rdate,ddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        deslist = findViewById(R.id.vehi);

        Intent i = getIntent();
        proid=i.getExtras().getString("proid");
        arraylist=new ArrayList<TravelEaseModel>();

        view_Vehicles();

    }
    public void view_Vehicles() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    arraylist = Arrays.asList(gson.fromJson(response.trim(), TravelEaseModel[].class));
                    adap = new UserVehiclesAdaptor(VehiclesActivity.this, arraylist);
                    deslist.setAdapter(adap);
                } else {
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Volley Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("requestType", "UserViewVehicles");
                map.put("proid", proid);
                return map;
            }
        };
        queue.add(request);
    }

}