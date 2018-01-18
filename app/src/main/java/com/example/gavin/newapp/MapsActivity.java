package com.example.gavin.newapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
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
        LocationListener{

    //events
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removes top header
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
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
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationMarker != null)
        {
            //removes current location marker when a topic is chosen
            currentLocationMarker.remove();

        }
        //once clicked will zoom in on current location
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch(v.getId())
        {
            //search button function
            case R.id.B_search1:
                EditText tf_location1 =  findViewById(R.id.TF_location1);
                String location = tf_location1.getText().toString();
                List<Address> addressList;


                if(!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        //only returns top 5 searches from the name given
                        addressList = geocoder.getFromLocationName(location, 5);

                        if(addressList != null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                //gives location variables, location, longitude, latitude, address, adds markers, changes camera position
                                LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());
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
                break;
                //shows nearby parks from place search api and displays message showing nearby park
            case R.id.B_park1:
                mMap.clear();
                String park = "park";
                String url = getUrl(latitude, longitude, park);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby parks", Toast.LENGTH_SHORT).show();
                break;

//shows nearby bowling alleys from place search api and displays message showing bowling alleys
            case R.id.B_bowling_alley1:
                mMap.clear();
                String bowling_alley = "bowling_alley";
                url = getUrl(latitude, longitude, bowling_alley);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby bowling alleys", Toast.LENGTH_SHORT).show();
                break;

            //shows nearby amusement parks from place search api and displays message showing nearby amusement parks
            case R.id.B_amusement_park1:
                mMap.clear();
                String amusement_park = "amusement_park";
                url = getUrl(latitude, longitude, amusement_park);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby amusement parks", Toast.LENGTH_SHORT).show();
                break;




            case R.id.B_parking1:
                mMap.clear();
                String parking = "parking";
                url = getUrl(latitude, longitude, parking);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby parking", Toast.LENGTH_SHORT).show();
                break;

//Transport footer given the user options on how to return
            case R.id.B_taxistand1:
                mMap.clear();
                String taxi_stand = "taxi_stand";
                url = getUrl(latitude, longitude, taxi_stand);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby taxi ranks", Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_bus1:
                mMap.clear();
                String bus_station = "bus_station";
                url = getUrl(latitude, longitude, bus_station);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby bus stations", Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_train1:
                mMap.clear();
                String train_station = "train_station";
                url = getUrl(latitude, longitude, train_station);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearbytrain stations", Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_subway1:
                mMap.clear();
                String subway_station = "subway_station";
                url = getUrl(latitude, longitude, subway_station);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Subway stations", Toast.LENGTH_SHORT).show();
                break;
//shows nearby spas from place search api and displays message showing spas

            case R.id.B_spa1:
                mMap.clear();
                String spa = "spa";
                url = getUrl(latitude, longitude, spa);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby spa's", Toast.LENGTH_SHORT).show();
                break;
//shows nearby shoping malls from place search api and displays message showing shopping malls

            case R.id.B_shopping_mall1:
                mMap.clear();
                String shopping_mall = "shopping_mall";
                url = getUrl(latitude, longitude, shopping_mall);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby shopping malls", Toast.LENGTH_SHORT).show();
                break;

            //shows nearby aqariunms from place search api and displays message showing aqariums

            case R.id.B_aquarium1:
                mMap.clear();
                String aquarium = "aquarium";
                url = getUrl(latitude, longitude, aquarium);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby aqariums", Toast.LENGTH_SHORT).show();
                break;

            //shows nearby beauty salons from place search api and displays message showing beauty salons

            case R.id.B_beauty_salon1:
                mMap.clear();
                String beauty_salon = "beauty_salon";
                url = getUrl(latitude, longitude, beauty_salon);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby beauty salons", Toast.LENGTH_SHORT).show();
                break;


        }
    }

//declares each string, and double
    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCIlwOmVMfbdo-6ad3X4G25CnmVCv1VoPs");

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
















/***************************************************************************************
 *    Title: Google Maps Nearby Places Tutorial | PART 1 ( Android Tutorials )
 *    Author: Priyanka Pakhale
 *    Date: 20/12/2017
 *
 *    Availability: https://www.youtube.com/watch?v=_Oljjn1fIAc&t=10s
 *
 ***************************************************************************************/

/***************************************************************************************
 *    Title: Updated Google Maps Tutorial | PART 1 ( Android Tutorials )
 *    Author: Priyanka Pakhale
 *    Date: 20/12/2017
 *
 *    Availability:https://www.youtube.com/watch?v=Ot8D-GZ8qfY&t=93s
 *
 ***************************************************************************************/
