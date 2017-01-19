package com.raulcorvo.googlemapsm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback{

    private static final String[] PERMISOS_LOCALIZACION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final int PETICION_LOCALIZACION = 123;

    private final LatLng BENIDORM = new LatLng(38.543685, -0.132227);
    private final LatLng PEREMARIA = new LatLng(38.553489, -0.121579);
    private GoogleMap mapa;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        if (!hayPermisoLocalizacion()) {
            ActivityCompat.requestPermissions(this, PERMISOS_LOCALIZACION, PETICION_LOCALIZACION);
        }
        else{
            mapFragment.getMapAsync(this);
        }
    }

    private boolean hayPermiso(String perm) {
        return (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hayPermisoLocalizacion() {
        return(hayPermiso(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private void error() {
        Toast.makeText(this, "Permisos de localización denegados", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PETICION_LOCALIZACION:
                if(hayPermisoLocalizacion()){
                    mapFragment.getMapAsync(this);
                }else{
                    error();
                }
                break;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        mapa.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public void onMapReady(GoogleMap mapa) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            this.mapa = mapa;
            mapa.setMyLocationEnabled(true);
            mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(BENIDORM, 13));
            mapa.setOnMapClickListener(this);
            mapa.addMarker(new MarkerOptions().position(PEREMARIA).title("IES Pere Mª")
                    .snippet("IES Pere Maria Orts i Bosch"));
        }
    }

    void moverCamara(View v){
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(BENIDORM, 13));
    }

    void animarCamara(View v){
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(PEREMARIA, 18));
    }

    void aMiPosicion(View v){
        if(mapa.getMyLocation() != null){
            mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapa.getMyLocation().getLatitude(),
                            mapa.getMyLocation().getLongitude()), 20));
        }
    }
}
