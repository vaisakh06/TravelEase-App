package com.example.traveleaseapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.ADMIN_MODULE.AdminHomeActivity;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.USER_MODULE.UserHomeActivity;
import com.example.traveleaseapp.USER_MODULE.UserRegActivity;
import com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE.ProviderRegActivity;
import com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE.VehicleProviderHome;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signin;
    TextView signup;
    String em, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signin = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.Login);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_MEDIA_LOCATION,

        };
        if (!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        signin.setOnClickListener(v -> {
            em = email.getText().toString();
            pass = password.getText().toString();

            if (em.isEmpty()) {
                Snackbar.make(email, "Please Enter Email", Snackbar.LENGTH_LONG)
                        .setAction("dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light)).show();
            } else if (pass.isEmpty()) {
                Snackbar.make(password, "Please Enter Password", Snackbar.LENGTH_LONG)
                        .setAction("dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light)).show();
            } else {
                login();
            }

        });

        signup.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            builder.setTitle("Choose Registration Type")
            .setMessage("Select an option to continue with registration.");

            // Inflate custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_registration_options, null);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            // Find buttons in the custom layout
            Button btnUserReg = dialogView.findViewById(R.id.btnUserRegistration);
            Button btnProviderReg = dialogView.findViewById(R.id.btnProviderRegistration);

            // Set button actions
            btnUserReg.setOnClickListener(v1 -> {
                startActivity(new Intent(this, UserRegActivity.class));
                dialog.dismiss();
            });

            btnProviderReg.setOnClickListener(v2 -> {
                startActivity(new Intent(this, ProviderRegActivity.class));
                dialog.dismiss();
            });

            dialog.show();
        });


    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    void login() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (!response.trim().equals("failed")) {
                    System.out.println("LOGINDD" + response);
                    String respArr[] = response.trim().split("#");
                    String uid = respArr[0];
                    String type = respArr[1];
                    System.out.println(uid);
                    System.out.println(type);
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa" + respArr[1]);
                    SharedPreferences.Editor editor = getSharedPreferences("profileprefer", MODE_PRIVATE).edit();
                    editor.putString("UID", uid);
                    editor.putString("type", type);
                    editor.commit();

                    if (type.equals("ADMIN")) {
                        startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));

                    } else if (type.equals("USER")) {
                        startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));

                    } else if (type.equals("PROVIDER")) {
                        startActivity(new Intent(getApplicationContext(), VehicleProviderHome.class));

                   } else {
                        Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "My Error :" + error, Toast.LENGTH_SHORT).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "login");
                map.put("email", em);
                map.put("pass", pass);
                return map;
            }
        };
        queue.add(request);
    }
}