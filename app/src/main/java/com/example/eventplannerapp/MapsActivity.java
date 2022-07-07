package com.example.eventplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.eventplannerapp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Stack strtAdd;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    private String strAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;


                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try{
                        addressList = geocoder.getFromLocationName(location,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if (addressList == null  || addressList.size() == 0) {
                        Toast.makeText(MapsActivity.this, R.string.address_not_found, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        mapFragment.getMapAsync(MapsActivity.this);
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {
        this.strAddress = strAddress;

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public String stacktoListForAdd(Stack strtAdd, int loc){
        this.strtAdd = strtAdd;
        List<String> addy;
        addy = new ArrayList<>(strtAdd);
        //String p2 = addy.get(loc);
        return addy.get(loc);
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //mapFragment.getMapAsync(MapsActivity.this);

        Stack<String> addresses = new Stack<>();
        addresses.add("582 Royal Drive");
        addresses.add("119 Redfield Place");


        /*List<String> stackToList = new ArrayList<>(addresses);
        String vineland = stackToList.get(0);*/

        LatLng home = getLocationFromAddress(MapsActivity.this, stacktoListForAdd(addresses,1));

        mMap.addMarker(new MarkerOptions().position(home).title("This is in vineland"));


    }


}