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
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.pizzaorderingapp.R;
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

public class AllStores extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;

    private LatLng[] storeLocations = {
            new LatLng(37.3382, -121.8863), // San Jose
            new LatLng(37.3541, -121.9552), // Santa Clara
            new LatLng(37.3220, -121.8832), // South San Jose
            new LatLng(37.3688, -122.0363), // Sunnyvale
            new LatLng(37.7749, -122.4194), // San Francisco
            new LatLng(37.4419, -122.1430)  // Palo Alto
    };

    private String[] storeNames = {
            "Store 1",
            "Store 2",
            "Store 3",
            "Store 4",
            "Store 5",
            "Store 6"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Move permission check to onMapReady or handle it after map is initialized
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Check and request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        Bitmap customMarker = BitmapFactory.decodeResource(getResources(), R.drawable.store);

        for (int i = 0; i < storeLocations.length; i++) {
            myMap.addMarker(new MarkerOptions()
                    .position(storeLocations[i])
                    .title(storeNames[i])
                    .icon(BitmapDescriptorFactory.fromBitmap(customMarker)));
        }

        // Use ViewTreeObserver to wait for layout completion
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null && mapFragment.getView() != null) {
            mapFragment.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng storeLocation : storeLocations) {
                        builder.include(storeLocation);
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 50; // Padding around the edges of the map in pixels
                    myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                    // Remove the listener after layout is done
                    mapFragment.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void getCurrentLocation() {
        try {
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
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
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
                    .color(ContextCompat.getColor(this, R.color.teal_700))); // Adjust the color as needed
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
