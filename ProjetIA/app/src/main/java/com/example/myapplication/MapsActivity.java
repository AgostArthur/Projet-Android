package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        SensorEventListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String[] a;
    private String url;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private LatLng previusLatIng;
    private SensorManager sensorManager;
    private float[] acceleration;
    Sensor accelerometer;
    private static final int Request_User_Location_Code = 99;
    private boolean occ = true;

    Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        start = findViewById(R.id.button_start);
        stop = findViewById(R.id.button_stop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
    }


    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    public String date() {
        String patern = "yyyyMMddHHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patern);
        return simpleDateFormat.format(new Date());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    public void saveFile(String str, boolean append) {
        FileOutputStream fos;
        try {
            if (append) {
                fos = openFileOutput("data.txt", Context.MODE_APPEND);
            } else {
                fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
            }
            fos.write(str.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        try {
            FileInputStream fls = openFileInput("data.txt");
            int size = fls.available();
            byte[] buffer = new byte[size];
            fls.read(buffer);
            String text = new String(buffer);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (occ) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18), 3000, null);
            occ = false;
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                    mMap.getCameraPosition().zoom), 1000, null);
            if (location.getSpeed() > 2)
                mMap.addPolyline(new PolylineOptions()
                        .add(latLng).add(previusLatIng).width(10).color(Color.GREEN).geodesic(true));
        }
        previusLatIng = latLng;
        if (location.getSpeed() > 2) {
            OkHttpClient client = new OkHttpClient();
            url = "http://82.231.49.191:8080/data/add?"
                    + "email=" + getIntent().getStringExtra("mail")
                    + "&transport=" + getIntent().getStringExtra("setting")
                    + "&vitesse=" + location.getSpeed()
                    + "&longitude=" + location.getLongitude()
                    + "&latitude=" + location.getLatitude()
                    + "&altitude=" + location.getAltitude()
                    + "&accelerationX=" + acceleration[0]
                    + "&accelerationY=" + acceleration[1]
                    + "&accelerationZ=" + acceleration[2]
                    + "&normeAcceleration=" + Math.sqrt(Math.pow(acceleration[0], 2) + Math.pow(acceleration[1], 2) + Math.pow(acceleration[2], 2))
                    + "&date=" + date();
            final Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "connection au server impossible.", Toast.LENGTH_SHORT).show();
                            saveFile(url + "\n", true);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        a = readFile().split("\n");
                        if (a[0] != "") {
                            for (int i = 0; i < a.length; i++) {
                                OkHttpClient client = new OkHttpClient();
                                final Request request = new Request.Builder().url(a[i]).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, final Response response) throws IOException {
                                    }
                                });
                            }
                        }
                        saveFile("", false);
                    }
                }
            });
        }
    }

    public void test(View view) {
        Toast.makeText(getApplicationContext(), "" + readFile(), Toast.LENGTH_SHORT).show();
    }

    public void startLoc(View view) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5500);
        locationRequest.setFastestInterval(4500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        start.setVisibility(Button.INVISIBLE);
        stop.setVisibility(Button.VISIBLE);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    public void stopLoc(View view) {
        start.setVisibility(Button.VISIBLE);
        stop.setVisibility(Button.INVISIBLE);
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        acceleration = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}