package com.example.pizzaorderingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Store;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class AllStores extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "AllStores";
    private static final int MAX_RETRIES = 3; // Maximum number of retries
    private static final long RETRY_DELAY_MS = 2000; // Delay between retries in milliseconds

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper dbHelper;
    private Handler handler = new Handler();

    private List<LatLng> storeLocations = new ArrayList<>();
    private List<String> storeNames = new ArrayList<>();
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnGoToMyLocation = findViewById(R.id.btnGoToMyLocation);
        btnGoToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(); // Reuse the existing getCurrentLocation() method
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        loadStoreData();

        Bitmap customMarker = BitmapFactory.decodeResource(getResources(), R.drawable.store);

        for (int i = 0; i < storeLocations.size(); i++) {
            myMap.addMarker(new MarkerOptions()
                    .position(storeLocations.get(i))
                    .title(storeNames.get(i))
                    .icon(BitmapDescriptorFactory.fromBitmap(customMarker)));
        }

        if (myMap != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null && mapFragment.getView() != null) {
                mapFragment.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!storeLocations.isEmpty()) {
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng storeLocation : storeLocations) {
                                builder.include(storeLocation);
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 50;
                            myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                        } else {
                            Toast.makeText(AllStores.this, "No stores available", Toast.LENGTH_SHORT).show();
                        }

                        mapFragment.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    private void loadStoreData() {
        List<Store> stores = dbHelper.getAllStores(); // Assuming getAllStores() returns List<Store>

        storeLocations.clear();
        storeNames.clear();

        for (Store store : stores) {
            storeLocations.add(new LatLng(store.getLatitude(), store.getLongitude()));
            storeNames.add(store.getName());
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && myMap != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            myMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title("You are here")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                            findClosestStore(currentLocation);
                        } else {
                            if (retryCount < MAX_RETRIES) {
                                retryCount++;
                                Log.e(TAG, "Location is null, retrying... (" + retryCount + "/" + MAX_RETRIES + ")");
                                handler.postDelayed(() -> getCurrentLocation(), RETRY_DELAY_MS);
                            } else {
                                Toast.makeText(AllStores.this, "Unable to get current location. Please try again later.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Failed to get location after " + MAX_RETRIES + " retries");
                            }
                        }
                    }
                });
    }

    private void findClosestStore(LatLng currentLocation) {
        LatLng closestStore = null;
        float minDistance = Float.MAX_VALUE;

        for (LatLng storeLocation : storeLocations) {
            float[] results = new float[1];
            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                    storeLocation.latitude, storeLocation.longitude, results);
            float distance = results[0];

            if (distance < minDistance) {
                minDistance = distance;
                closestStore = storeLocation;
            }
        }

        if (closestStore != null && myMap != null) {
            myMap.addMarker(new MarkerOptions()
                    .position(closestStore)
                    .title("Closest Store")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            myMap.addPolyline(new PolylineOptions()
                    .add(currentLocation, closestStore)
                    .width(10)
                    .color(ContextCompat.getColor(this, R.color.teal_700)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
