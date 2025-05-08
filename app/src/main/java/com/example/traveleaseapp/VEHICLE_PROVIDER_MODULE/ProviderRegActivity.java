package com.example.traveleaseapp.VEHICLE_PROVIDER_MODULE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.traveleaseapp.COMMON.GPSTracker;
import com.example.traveleaseapp.COMMON.Utility;
import com.example.traveleaseapp.LoginActivity;
import com.example.traveleaseapp.R;
import com.example.traveleaseapp.USER_MODULE.UserRegActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProviderRegActivity extends AppCompatActivity {

    EditText name,email,phone,password,addressEditText,confirmpass;
    Button signup,btn;
    ImageView locationMarker;
    String ADDRESS = "";
    GPSTracker gps;
    double latitude;
    double longitude;
    TextView latitudeTextView, longitudeTextView;
    EditText  address;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    TextView LAT, LONG, mapaddress;
    TextView loginn;
    String na,em,pa,ph,cpass,addresss,latitudee,longitudee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_reg);

        name=findViewById(R.id.etProviderName);
        email=findViewById(R.id.etProviderEmail);
        phone=findViewById(R.id.etProviderPhone);
        password=findViewById(R.id.etProviderPassword);
        addressEditText = findViewById(R.id.etProviderLocation);
        locationMarker=findViewById(R.id.ivLocationIcon);
        latitudeTextView = findViewById(R.id.Lat);
        longitudeTextView = findViewById(R.id.Long);
        LAT = findViewById(R.id.Lat);
        LONG = findViewById(R.id.Long);
        confirmpass=findViewById(R.id.etProviderConfirmPassword);
        loginn=findViewById(R.id.tvLogin);
        signup=findViewById(R.id.btnProviderRegister);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        loginn.setOnClickListener(v -> {
            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        });

        signup.setOnClickListener(v -> {
            na = name.getText().toString();
            em = email.getText().toString();
            pa = password.getText().toString();
            cpass = confirmpass.getText().toString();
            ph = phone.getText().toString();
            addresss=addressEditText.getText().toString();
            latitudee = latitudeTextView.getText().toString().trim();
            longitudee = longitudeTextView.getText().toString().trim();

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
            } else if (addresss.isEmpty()) {
                showSnackbar(address, "Please Enter Address");
            } else if (pa.isEmpty()) {
                showSnackbar(password, "Please Enter Password");
            } else if (!pa.equals(cpass)) {
                showSnackbar(confirmpass, "Password and Confirm Password do not match");
            } else {
                providerReg();
            }
        });
        locationMarker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                PopMap(LAT, LONG, address, "address");
                return false;
            }
        });
    }

    void providerReg() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                if (!response.trim().equals("failed")) {
                    Toast.makeText(ProviderRegActivity.this, "Sign Up successful ", Toast.LENGTH_SHORT).show();
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
                map.put("requestType", "ProviderReg");
                map.put("name", na);
                map.put("email", em);
                map.put("phn", ph);
                map.put("passwd", pa);
                map.put("latitudee", latitudee);
                map.put("longitudee", longitudee);
                map.put("address", ADDRESS);
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

    void PopMap(final TextView lat, final TextView longg, final EditText address, final String ADDTYPE) {
        final Dialog dialog = new Dialog(ProviderRegActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.custom_map_lattlongg);////your custom content



        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);

        mapaddress = dialog.findViewById(R.id.custom_map_address);
        btn = dialog.findViewById(R.id.custom_map_btnadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ADDTYPE.equals("address")) {
                    if (ADDRESS.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select location", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something gone wrong", Toast.LENGTH_SHORT).show();

                }


            }
        });

        MapsInitializer.initialize(getApplicationContext());

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {


                LatLng posisiabsen = new LatLng(latitude, longitude); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Yout"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.5f));


                //                ....
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("New Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;

                        lat.setText("" + latitude);
                        longg.setText("" + longitude);

                        //getAddress strat
                        try {
                            Geocoder geocoder;
                            List<Address> yourAddresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            yourAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            System.out.println("CURRENT ADDRESS: " + yourAddresses);
                            if (yourAddresses.size() > 0) {
                                String yourAddress = yourAddresses.get(0).getAddressLine(0);
                                String yourCity = yourAddresses.get(0).getAddressLine(1);
                                String yourCountry = yourAddresses.get(0).getAddressLine(2);
                                Log.d("*", yourAddress);
                                if (ADDTYPE.equals("address")) {
                                    ADDRESS = yourAddress;
                                }
                                ADDRESS = yourAddress;
                                addressEditText.setText(yourAddress);
                                mapaddress.setText(yourAddress);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            latitude = lat;
                            longitude = longi;
                            // Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();
            latitude = lat;
            longitude = longi;
            Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();
            //latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(ProviderRegActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}