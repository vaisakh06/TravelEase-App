package com.example.traveleaseapp.USER_MODULE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserRegActivity extends AppCompatActivity {

    EditText name,email,phone,password,dobb,address,confirmpass;
    RadioGroup genderGroup;
    Button signup;
    TextView loginn;
    String na,em,pa,dob,ph,ad,gen,cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        name=findViewById(R.id.etFullName);
        email=findViewById(R.id.etEmail);
        phone=findViewById(R.id.etPhone);
        password=findViewById(R.id.etPassword);
        dobb=findViewById(R.id.etDob);
        address=findViewById(R.id.etCountry);
        genderGroup = findViewById(R.id.radioGender);
        confirmpass=findViewById(R.id.etConfirmPassword);
        loginn=findViewById(R.id.tvLogin);
        signup=findViewById(R.id.btnRegister);

        dobb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(
                        v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                                selectedMonth += 1;
                                dob = selectedMonth + "/" + selectedDay + "/" + selectedYear;
                                dobb.setText(dob);
                            }
                        },
                        year, month, day
                );

                mDatePicker.setTitle("Select Date Of Birth");
                mDatePicker.show();
            }
        });


        loginn.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        });

        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMale) {
                gen = "Male";
            } else if (checkedId == R.id.rbFemale) {
                gen = "Female";
            } else if (checkedId == R.id.rbOther) {
                gen = "Other";
            }
        });

        signup.setOnClickListener(v -> {
            na = name.getText().toString();
            em = email.getText().toString();
            pa = password.getText().toString();
            cpass = confirmpass.getText().toString();
            ph = phone.getText().toString();
            dob = dobb.getText().toString();
            ad = address.getText().toString();

            if (na.isEmpty()) {
                showSnackbar(name, "Please Enter Name");
            } else if (em.isEmpty()) {
                showSnackbar(email, "Please Enter Email");
            } else if (!em.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                showSnackbar(email, "Please Check Email Format");
            } else if (ph.isEmpty()) {
                showSnackbar(phone, "Please Enter Contact Number");
            } else if (ph.length() != 10) {
                showSnackbar(phone, "Please Enter Correct Contact Number");
            } else if (ad.isEmpty()) {
                showSnackbar(address, "Please Enter Address");
            } else if (pa.isEmpty()) {
                showSnackbar(password, "Please Enter Password");
            } else if (!pa.equals(cpass)) {
                showSnackbar(confirmpass, "Password and Confirm Password do not match");
            } else if (gen == null || gen.isEmpty()) {
                showSnackbar(genderGroup, "Please select your gender");
            } else {
                userReg();
            }
        });
    }

    void userReg() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (!response.trim().equals("failed")) {
                    Toast.makeText(UserRegActivity.this, "Sign Up successful ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed" + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my Error :" + error, Toast.LENGTH_SHORT).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "UserReg");
                map.put("name", na);
                map.put("email", em);
                map.put("phn", ph);
                map.put("passwd", pa);
                map.put("dob", dob);
                map.put("gen", gen);
                map.put("add", ad);
                return map;
            }
        };
        queue.add(request);
    }
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", v -> {})
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }
}