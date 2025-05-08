package com.example.traveleaseapp.ADMIN_MODULE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;

public class AdminHomeActivity extends AppCompatActivity {

    ImageView logout;
    CardView user,provider,addpack,viewpack,feedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout=findViewById(R.id.log);
        user=findViewById(R.id.users);
        provider=findViewById(R.id.providers);
        addpack=findViewById(R.id.packages);
        viewpack=findViewById(R.id.pack);
//        feedbacks=findViewById(R.id.feed);

        logout.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        });
        user.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(),UsersActivity.class);
            startActivity(i);
        });
        provider.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(),VehicleProviders.class);
            startActivity(i);
        });
        addpack.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(),AddPackages.class);
            startActivity(i);
        });
        viewpack.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(),PackagesActivity.class);
            startActivity(i);
        });
//        feedbacks.setOnClickListener(v -> {
//            Intent i=new Intent(getApplicationContext(), ReviewsAndFeedbacks.class);
//            startActivity(i);
//        });
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Logout", Toast.LENGTH_SHORT).show();
    }
}