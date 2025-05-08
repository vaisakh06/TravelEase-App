package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;
import com.example.traveleaseapp.USER_MODULE.VehiclesActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserVehicleProvidersAdaptor extends ArrayAdapter<TravelEaseModel> {

    Activity context;
    List<TravelEaseModel> list;
    String proid,bid,ddate,rdate;

    public UserVehicleProvidersAdaptor(Activity context, List<TravelEaseModel> list) {
        super(context, R.layout.activity_user_vehicle_providers_adaptor, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_user_vehicle_providers_adaptor, null, true);

        TextView name = view.findViewById(R.id.providerName);
        TextView phn = view.findViewById(R.id.providerPhone);
        TextView email = view.findViewById(R.id.providerEmail);
        TextView location = view.findViewById(R.id.providerAddress);
        Button btnvehicle = view.findViewById(R.id.view_vehi);

        TravelEaseModel provider = list.get(position);

        btnvehicle.setOnClickListener(v -> {
            proid=list.get(position).getPid();
            Intent intent=new Intent(getContext(), VehiclesActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("proid",proid);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });

        name.setText("Name: " + provider.getProvidername());
        phn.setText("Phone: " + provider.getProviderphone());
        email.setText("Email: " + provider.getProvideremail());
        location.setText("Location: " + provider.getProvideraddress());


        return view;
    }
}