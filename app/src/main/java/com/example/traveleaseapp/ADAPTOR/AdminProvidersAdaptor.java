package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProvidersAdaptor extends ArrayAdapter<TravelEaseModel> {

    Activity context;
    List<TravelEaseModel> list;

    public AdminProvidersAdaptor(Activity context, List<TravelEaseModel> list) {
        super(context, R.layout.activity_admin_providers_adaptor, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_admin_providers_adaptor, null, true);

        TextView name = view.findViewById(R.id.providerName);
        TextView phn = view.findViewById(R.id.providerPhone);
        TextView email = view.findViewById(R.id.providerEmail);
        TextView location = view.findViewById(R.id.providerAddress);

        TravelEaseModel provider = list.get(position);

        name.setText("Name: " + provider.getProvidername());
        phn.setText("Phone: " + provider.getProviderphone());
        email.setText("Email: " + provider.getProvideremail());
        location.setText("Location: " + provider.getProvideraddress());

        view.setOnClickListener(v -> showAcceptDialog(provider, position));

        return view;
    }

    private void showAcceptDialog(TravelEaseModel provider, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Accept Provider");
        builder.setMessage("Do you want to accept the vehicle provider: " + provider.getProvidername() + "?");

        builder.setPositiveButton("Accept", (dialog, which) -> {
            acceptProvider(provider, position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void acceptProvider(TravelEaseModel provider, int position) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, Utility.url,
                response -> {
                    Log.d("Response", response);
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, provider.getProvidername() + " accepted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to accept provider", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Volley Error", "" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = context.getSharedPreferences("profileprefer", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("UID", "");

                Map<String, String> params = new HashMap<>();
                params.put("requestType", "AdminAcceptProvider");
                params.put("pid", provider.getPid());
                return params;
            }
        };

        queue.add(request);
    }
}
