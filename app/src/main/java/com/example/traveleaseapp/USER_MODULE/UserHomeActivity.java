package com.example.traveleaseapp.USER_MODULE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHomeActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation_user);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        int itemId = item.getItemId();

                        if (itemId == R.id.action_home) {
                            selectedFragment = new UserHomeFragment();
                        } else if (itemId == R.id.action_package) {
                            selectedFragment = new TravelPackageFragment();
                        } else if (itemId == R.id.action_booking) {
                            selectedFragment = new MyBookingFragment();
                        } else if (itemId == R.id.action_provider) {
                            selectedFragment = new VehicleProvidersFragment();
                        } else if (itemId == R.id.action_vbooking) {
                            selectedFragment = new BookVehicleFragment();
                        }


                        if (selectedFragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.fragment_container_User, selectedFragment);
                            transaction.commit();
                        }

                        return true;
                    }
                });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_User, new UserHomeFragment())
                .commit();

    }
    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(UserHomeActivity.this);
        builder.setMessage("Logout!! Are You Sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the logout action
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Call the default behavior of the back button
                        UserHomeActivity.super.onBackPressed();
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Logout");
        alert.show();
    }
}