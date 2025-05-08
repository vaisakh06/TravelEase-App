package com.example.traveleaseapp.USER_MODULE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.ADAPTOR.UserPackageAdaptor;
import com.example.traveleaseapp.ADAPTOR.UserVehicleProvidersAdaptor;
import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VehicleProvidersFragment extends Fragment {

    ListView packlist;

    List<TravelEaseModel> arrayList;

    public UserVehicleProvidersAdaptor adap;
    String searchText;
    ImageView searchimg,rec;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vehicle_providers, container, false);

        packlist =root.findViewById(R.id.pro);
        search=root.findViewById(R.id.searchPackage);
        searchimg=root.findViewById(R.id.searchIcon);

        userViewProviders();
        arrayList = new ArrayList<TravelEaseModel>();

        searchimg.setOnClickListener(v -> {
            validate();
        });
        userViewProviders();
        return root;
    }

    private void validate() {
        searchText=search.getText().toString();
        if(searchText.isEmpty())
        {
            userViewProviders();

        }
        else
        {
            getSearchResult();
        }
    }
    private void getSearchResult()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    String data = response.trim();
                    arrayList = Arrays.asList(gson.fromJson(data, TravelEaseModel[].class));
                    adap = new UserVehicleProvidersAdaptor(requireActivity(), arrayList);
                    packlist.setAdapter(adap);
                    registerForContextMenu(packlist);

                } else {
                    Toast.makeText(getContext(), "NO_DATA", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", Context.MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "UserSearchProvider");
                map.put("uid", uid);
                map.put("searchValue",  searchText);
                return map;
            }

        };
        queue.add(request);
    }

    private void userViewProviders() {

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
                    adap = new UserVehicleProvidersAdaptor(requireActivity(), arrayList);
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
                map.put("requestType", "UserViewVehicleProviders");
                map.put("uid", uid);
                return map;
            }
        };
        queue.add(request);
    }
}