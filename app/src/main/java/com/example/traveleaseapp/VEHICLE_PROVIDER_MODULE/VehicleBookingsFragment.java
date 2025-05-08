package com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.ADAPTOR.ProviderBookingsAdaptor;
import com.example.traveleaseapp.ADAPTOR.ProviderVehicleAdaptor;
import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VehicleBookingsFragment extends Fragment {

    ListView packlist;

    List<TravelEaseModel> arrayList;

    public ProviderBookingsAdaptor adap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vehicle_bookings, container, false);

        packlist =root.findViewById(R.id.book);
        arrayList = new ArrayList<TravelEaseModel>();
        ViewVehicles();
        return root;
    }
    private void ViewVehicles() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                System.out.println("EEE" + response);
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    String data = response.trim();
                    arrayList = Arrays.asList(gson.fromJson(data, TravelEaseModel[].class));
                    adap = new ProviderBookingsAdaptor(requireActivity(), arrayList);
                    packlist.setAdapter(adap);
                    registerForContextMenu(packlist);

                }  else {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("profileprefer", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("UID", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "BookingVehicles");
                map.put("proid", uid);
                return map;
            }
        };
        queue.add(request);
    }
}