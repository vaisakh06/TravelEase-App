package com.example.traveleaseapp.ADAPTOR;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.traveleaseapp.COMMON.TravelEaseModel;
import com.example.traveleaseapp.R;

import java.io.IOException;
import java.util.List;

public class AdminUsersAdaptor extends ArrayAdapter<TravelEaseModel> {

    Activity context;
    List<TravelEaseModel> list;

    public AdminUsersAdaptor(Activity context, List<TravelEaseModel> list) {
        super(context, R.layout.activity_admin_users_adaptor, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_admin_users_adaptor, null, true);

        TextView name = view.findViewById(R.id.username);
        TextView phn = view.findViewById(R.id.userphone);
        TextView email = view.findViewById(R.id.useremail);
        TextView gen = view.findViewById(R.id.usergender);
        TextView dob = view.findViewById(R.id.userdob);
        TextView location  = view.findViewById(R.id.userlocation);

        name.setText("Name:" + list.get(position).getUsername());
        phn.setText("Phone:" + list.get(position).getUserphone());
        email.setText("Email:" + list.get(position).getUseremail());
        gen.setText("Gender:" + list.get(position).getUsergender());
        dob.setText("Date Of Birth:" + list.get(position).getUserdob());
        location.setText("Location:" + list.get(position).getUserlocation());

        return view;
    }
}