package com.example.traveleaseapp.USER_MODULE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    EditText amount, accno, accna, cardn, cvv, exp;
    TextView packname, price;
    Button paybtn;
    String packid, uid, bid, packna, pri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amount = findViewById(R.id.paymentAmount);
        accno = findViewById(R.id.cardNumber);
        accna = findViewById(R.id.cardHolderName);
        cardn = findViewById(R.id.cardNumber);
        cvv = findViewById(R.id.cvv);
        exp = findViewById(R.id.expiryDate);
        paybtn = findViewById(R.id.payNowButton);
        packname = findViewById(R.id.packageName);
        price = findViewById(R.id.totalPrice);

        Intent i = getIntent();
        packid = i.getStringExtra("packid");
        uid = i.getStringExtra("uid");
        bid = i.getStringExtra("bid");
        packna = i.getStringExtra("pack_na");
        pri = i.getStringExtra("price");

        packname.setText("Package: " + packna);
        price.setText("Total Price: Rs." + pri);

        paybtn.setOnClickListener(v -> userPay());

        exp.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, (datepicker, selectedYear, selectedMonth, selectedDay) -> {
            selectedMonth += 1;
            exp.setText(selectedMonth + "/" + selectedDay + "/" + selectedYear);
        }, year, month, day);

        mDatePicker.setTitle("Select Expiry Date");
        mDatePicker.show();
    }

    private void userPay() {
        // Validation
        String cardNumber = accno.getText().toString().trim();
        String cardHolder = accna.getText().toString().trim();
        String cvvCode = cvv.getText().toString().trim();
        String expiryDate = exp.getText().toString().trim();
        String amt = amount.getText().toString().trim();

        if (cardNumber.isEmpty() || cardNumber.length() != 16) {
            Toast.makeText(this, "Enter a valid 16-digit card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardHolder.isEmpty()) {
            Toast.makeText(this, "Enter cardholder name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cvvCode.isEmpty() || cvvCode.length() != 3) {
            Toast.makeText(this, "Enter a valid 3-digit CVV", Toast.LENGTH_SHORT).show();
            return;
        }

        if (expiryDate.isEmpty()) {
            Toast.makeText(this, "Enter expiry date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amt.isEmpty()) {
            Toast.makeText(this, "Enter payment amount", Toast.LENGTH_SHORT).show();
            return;
        }

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, response -> {
            if (!response.trim().equals("failed")) {
                Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                Intent feedbackIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                feedbackIntent.putExtra("bid", bid);
                startActivity(feedbackIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Payment Failed: " + response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            Log.i("Error", "" + error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("profileprefer", Context.MODE_PRIVATE);
                String usid = sharedPreferences.getString("UID", "");

                map.put("requestType", "userPayment");
                map.put("uid", usid);
                map.put("sid", packid);
                map.put("bid", bid);
                return map;
            }
        };
        queue.add(request);
    }
}
