package com.mursitaffandi.fireholic;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mursitaffandi.fireholic.maps.Mapsragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.quentinklein.slt.LocationTracker;

public class MapsFActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MapsFActivity.class.getSimpleName();
    @BindView(R.id.tv_maps_start)
    TextView tv_maps_start;

    @BindView(R.id.btn_maps_start_select)
    Button btn_maps_start_select;

    @BindView(R.id.btn_maps_start_gps)
    Button btn_maps_start_gps;

    @BindView(R.id.tv_maps_destiny)
    TextView tv_maps_destiny;

    @BindView(R.id.btn_maps_destiny_select)
    Button btn_maps_destiny_select;
    @BindView(R.id.btn_maps_go)
    Button btn_maps_go;

    @BindView(R.id.tv_maps_order_details)
    TextView tv_maps_order_details;

    private double LATITUDE_ORIGIN;
    private double LONGITUDE_ORIGIN;
    private int LOCATION_DESTINATION = 0;
    private double LATITUDE_DESTINATION;
    private double LONGITUDE_DESTINATION;

    private BitmapDescriptor iconMe;
    private GoogleMap mMap;
    private LocationTracker tracker;
    private Marker myMarker;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_f);
        ButterKnife.bind(this);
        iconMe = BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frm_main_maps);
        mapFragment.getMapAsync(this);
        context = this;

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        btn_maps_destiny_select.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_maps_destiny_select:
                pickLocation(LOCATION_DESTINATION);
                break;
        }
    }

    public void pickLocation(int REQUEST_PLACE_PICKER) {
        Log.d(TAG, "onClick: setLocationPickup clicked");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
        } else {
            mMap.setMyLocationEnabled(true);
            tracker = new LocationTracker(context
            ) {
                @Override
                public void onLocationFound(Location location) {
                    // Do some stuff
                    Log.d(TAG, String.valueOf(location.getLatitude()));
                    Log.d(TAG, String.valueOf(location.getLongitude()));

                    LatLng my_location = new LatLng(location.getLatitude(), location.getLongitude());

                    if (myMarker == null) {
                        Log.d(TAG, "Marker null");
                        LATITUDE_ORIGIN = location.getLatitude();
                        LONGITUDE_ORIGIN = location.getLongitude();
                        displayLocation(tv_maps_start, LATITUDE_ORIGIN, LONGITUDE_ORIGIN);
                        myMarker = mMap.addMarker(new MarkerOptions().position(my_location).icon(iconMe));
                        CameraPosition myPosition = new CameraPosition.Builder()
                                .target(my_location).zoom(15).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                    } else {
                        Log.d(TAG, "Marker remove and update!");
                        myMarker.remove();
                        myMarker = mMap.addMarker(new MarkerOptions().position(my_location).icon(iconMe));
                    }
                }

                @Override
                public void onTimeout() {
                    Log.d(TAG, "Connection timeout!");
                }
            };
            tracker.startListening();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access Coarse Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void displayLocation(TextView addres, double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                addres.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    addres.setText(addresses.get(0).getFeatureName());
                    addres.setTextColor(Color.BLACK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public List<Address> getAddress(LatLng point) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            if (point.latitude != 0 || point.longitude != 0) {
                addresses = geocoder.getFromLocation(point.latitude,
                        point.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);

                Log.d(TAG,address + " - " + city + " - " + country);

                return addresses;

            } else {
                Toast.makeText(this, "latitude and longitude are null",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,e.toString());
            return null;
        }
    }
}
