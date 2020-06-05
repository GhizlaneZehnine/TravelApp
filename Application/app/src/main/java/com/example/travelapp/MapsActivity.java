package com.example.travelapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    String str="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        str = intent.getStringExtra("name");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;

        Object dataTransfer[] = new Object[2];
        Object dataTransfer2[] = new Object[2];
        Object dataTransfer3[] = new Object[2];

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        GetNearbyPlacesData getNearbyPlacesData2 = new GetNearbyPlacesData();
        GetNearbyPlacesData getNearbyPlacesData3 = new GetNearbyPlacesData();

        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }

        if(str != "") {
            switch (str) {

                case "Restaurants":
                    mMap.clear();
                    String restaurant = "restaurant";
                    String url = getUrl(latitude, longitude, restaurant);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
                    break;

                case "Monuments":
                    mMap.clear();
                    String museum = "museum";
                    url = getUrl(latitude, longitude, museum);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Monuments", Toast.LENGTH_SHORT).show();
                    break;


                case "Stations de transports":
                    mMap.clear();
                    String train_station = "train_station";

                    url = getUrl(latitude, longitude, train_station);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    getNearbyPlacesData.execute(dataTransfer);

                    Toast.makeText(MapsActivity.this, "Showing Nearby Transport Stations", Toast.LENGTH_SHORT).show();
                    break;

                case "Hôpitaux":
                    mMap.clear();
                    String hospital = "hospital";
                    url = getUrl(latitude, longitude, hospital);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
                    break;

                case "Banques":
                    mMap.clear();
                    String bank = "bank";
                    url = getUrl(latitude, longitude, bank);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Banks", Toast.LENGTH_SHORT).show();
                    break;

                case "Mosqués":
                    mMap.clear();
                    String mosque = "mosque";
                    url = getUrl(latitude, longitude, mosque);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Mosques", Toast.LENGTH_SHORT).show();
                    break;

                case "Salon de beauté":
                    mMap.clear();
                    String beauty_salon = "beauty_salon";
                    url = getUrl(latitude, longitude, beauty_salon);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Beauty Salons", Toast.LENGTH_SHORT).show();
                    break;

                case "Salles de cinéma":
                    mMap.clear();
                    String movie_theater = "movie_theater";
                    url = getUrl(latitude, longitude, movie_theater);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Movie Theaters", Toast.LENGTH_SHORT).show();
                    break;

                case "Café":
                    mMap.clear();
                    String cafe = "cafe";
                    url = getUrl(latitude, longitude, cafe);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Cafes", Toast.LENGTH_SHORT).show();
                    break;

                case "Pharmacies":
                    mMap.clear();
                    String pharmacy = "pharmacy";
                    url = getUrl(latitude, longitude, pharmacy);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Pharmacies", Toast.LENGTH_SHORT).show();
                    break;

                case "La poste":
                    mMap.clear();
                    String post_office = "post_office";
                    url = getUrl(latitude, longitude, post_office);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Post Offices", Toast.LENGTH_SHORT).show();
                    break;

                case "Hôtels":
                    mMap.clear();
                    String hotel = "hotel";
                    url = getUrl(latitude, longitude, hotel);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Hotels", Toast.LENGTH_SHORT).show();
                    break;

                case "Super-marchés":
                    mMap.clear();
                    String supermarket = "supermarket";
                    url = getUrl(latitude, longitude, supermarket);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Supermarkets", Toast.LENGTH_SHORT).show();
                    break;

                case "Shopping mall":
                    mMap.clear();
                    String shopping_mall = "shopping_mall";
                    url = getUrl(latitude, longitude, shopping_mall);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Shopping Malls", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    public void onClick(View v)
    {


        if(v.getId()==R.id.B_search) {
            EditText tf_location = findViewById(R.id.TF_location);
            String location = tf_location.getText().toString();
            List<Address> addressList;


            if (!location.equals("")) {
                Geocoder geocoder = new Geocoder(this);

                try {
                    addressList = geocoder.getFromLocationName(location, 5);

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            LatLng latLng = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(location);
                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyC7vC4v5akYbhwo7Vdw5MgDJb9lKyD1F5Y");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
