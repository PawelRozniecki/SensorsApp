package com.github.pawelrozniecki.sensors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class GpsActivity extends AppCompatActivity implements LocationListener {


    private FusedLocationProviderClient fusedLocationProvider;



    private TextView latTextView, longTextView, distanceTv;
    private double latitude = 0.0;
    private ImageView setLocation;
    private EditText editText;
    private double longitude = 0.0;
    double maxDist = 20.0;
    private  LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private Location oldLocation;
    private Button setDist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //check if user granted the permission to access the current location
        checkPermission();


        latTextView = findViewById(R.id.lat);
        longTextView = findViewById(R.id.longitude);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1);

        distanceTv = findViewById(R.id.notification);
        setLocation = findViewById(R.id.setLocation);
        setDist = findViewById(R.id.setDist);
        editText = findViewById(R.id.editText);


        //gets users current location

        getNewLocation();


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {


                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latTextView.setText("Latitude:\n " + String.format(Locale.US, "%s ", latitude));
                    longTextView.setText("Longitude:\n  " + String.format(Locale.US, "%s ", longitude));

                    double distance = calculateDistance(oldLocation,location);

                    if(distance>maxDist){

                        distanceTv.setText("YOU LEFT THE AREA\n" + String.format("%.2f",distance) + " meters");
                        distanceTv.setTextColor(Color.parseColor("#ee5253"));

                    }else{
                        distanceTv.setText("Distance walked from source\n" + String.format("%.2f", distance) + " meters");
                        distanceTv.setTextColor(getColor(R.color.textcolor));
                    }


                }
            }
        };
        startLocationUpdates();
        setLocation.setOnClickListener(v -> {
            getNewLocation();
            Toast.makeText(getApplicationContext(), "New source location set",
                    Toast.LENGTH_LONG).show();            });

        setDist.setOnClickListener(v -> {
            if(editText.getText().length()>0) {
                double distance = Double.parseDouble(editText.getText().toString());
                //user cannot set the distance less than 5 meteres
                if (distance < 5) {
                    Toast.makeText(getApplicationContext(), "Cannot set distance less than 5m", Toast.LENGTH_SHORT).show();
                } else {
                    maxDist = distance;
                    Toast.makeText(getApplicationContext(), "Distance set to " + Double.toString(distance) + " m", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getNewLocation(){

        fusedLocationProvider.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        latTextView.setText("Latitude:\n  " + String.format(Locale.US, "%s ", latitude));
                        longTextView.setText("Longitude:\n  " + String.format(Locale.US, "%s ", longitude));
                        oldLocation = location;
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public static double calculateDistance(Location oldLocation, Location newLocation){


        if(oldLocation!=null& newLocation!=null){
            double distance;
            distance = oldLocation.distanceTo(newLocation);
            return distance;
        }
        return 0.0;
    }

    private void startLocationUpdates() {
        fusedLocationProvider.requestLocationUpdates(mLocationRequest,locationCallback,Looper.getMainLooper());
        Log.v("startLocUpdate: " , "sending request");
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            startLocationUpdates();
            latTextView.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
