package com.coderfolk.policemap;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    Context context = this;
    DBHelper db;
    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        db = new DBHelper(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng talegaon = new LatLng(18.7392, 73.6831);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(talegaon, 13.5f));

        // Intent map=getIntent();
//
//        double latitude=map.getDoubleExtra("latitude",0.0);
//        double longitude=map.getDoubleExtra("longitude",0.0);
//        // Add a marker in Sydney and move the camera
//        LatLng mumbai = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(mumbai).title("Test 1"));
////        mMap.setMaxZoomPreference();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai,12.0f));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(mumbai));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng d=marker.getPosition();
                Toast.makeText(context,(CharSequence)String.valueOf(d.latitude)+String.valueOf(d.longitude),Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                // db.insertData(latLng.latitude, latLng.longitude);
                //mMap.addMarker(new MarkerOptions().position(latLng).title("Test value"));

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.show();
                final Button b = (Button) dialog.findViewById(R.id.insertdata);
                final EditText vi, s, t;
                vi = (EditText) dialog.findViewById(R.id.victim);
                s = (EditText) dialog.findViewById(R.id.suspect);
                t = (EditText) dialog.findViewById(R.id.type);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String victim = vi.getText().toString();
                        String suspect = s.getText().toString();
                        String type = t.getText().toString();
                        db.insertData(latLng.latitude, latLng.longitude, victim, suspect, type);
                        boolean b = refresh(mMap);
                        if(b){
                            Toast.makeText(context,"Map Reloaded",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                // Toast.makeText(getApplicationContext(), ((CharSequence) String.valueOf(latLng.latitude) + " " + String.valueOf(latLng.longitude)), Toast.LENGTH_SHORT).show();
            }
        });

        boolean b = refresh(mMap);
        if(b){
            Toast.makeText(context,"Map loaded",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean refresh(GoogleMap mMap) {
        try {
            List<Data> data = db.readData();
            for (int i = 0; i < data.size(); i++) {
                double latitude = data.get(i).latitude;
                double longitude = data.get(i).longitude;
                LatLng temp = new LatLng(latitude, longitude);
                String victim = data.get(i).victim;
                String suspect = data.get(i).suspect;
                String type = data.get(i).type;
                mMap.addMarker(new MarkerOptions().position(temp).title("Victim: " + victim + "\nSuspect: " + suspect + "\nType: " + type));
                //"Victim: " + victim + "\nSuspect: " + suspect + "\nType: " + type
                // ;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
