package com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;
import com.example.traveleaseapp.USER_MODULE.MyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VehicleProviderHome extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_provider_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation_provider);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.action_home) {
                    selectedFragment = new ProviderHomeFragment();
                } else if (itemId == R.id.action_viewvehi) {
                    selectedFragment = new VehicleFragment();
                } else if (itemId == R.id.action_booking) {
                    selectedFragment = new VehicleBookingsFragment();
//                } e
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

        // Load the default fragment (Provider Home) when the activity starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_User, new ProviderHomeFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(VehicleProviderHome.this);
        builder.setMessage("Logout!! Are You Sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish(); // Finish the current activity to prevent going back
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Close the dialog without doing anything
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Logout");
        alert.show();
    }
}
