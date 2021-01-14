package com.adcour.mymapas;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        //Marca de castellon
        LatLng castellon = new LatLng(40, 0);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(castellon)
                .title("Parque de Castellon")
                .snippet("Habitantes: 18000") //AÑADIR INFORMACION
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.parque))
        );
        marker.setTag("Ciudad"); //NO VISIBLE
        mMap.setOnInfoWindowClickListener(this); //CUANDO SE PULSA EL MARCADOR
        mMap.moveCamera(CameraUpdateFactory.newLatLng(castellon)); // MOVER CAMARA HASTA EL MARCADOR ANTERIOR


        LatLngBounds MIZONA = new LatLngBounds(new LatLng(37.5, -3), new LatLng(41.5, 2));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MIZONA.getCenter(), 7.5f));

        Toast.makeText(this, "Zoommin:" + mMap.getMinZoomLevel(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Zoommax:" + mMap.getMaxZoomLevel(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Zoomactual:" + mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();


        //LISTENER PARA SABER CUANDO CAMBIAMOS ZOOM
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Toast.makeText(MapsActivity.this, "Zoom cambiado a:" + mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();
            }
        });

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //CAMBIAR TIPO DE MAPA
        mMap.getUiSettings().setZoomControlsEnabled(true); // PERMITIR BOTONES DE ZOOM
        mMap.getUiSettings().setCompassEnabled(false); // DESACTIVA LA ROTACION CON LA BRUJULA
        mMap.getUiSettings().setMapToolbarEnabled(false); // DESACTIVA LOS BOTONES DE COMO LLEGAR Y GOOGLE MAPS


        //LISTENER PARA SABER CUANDO SE PULSA EL MAPA
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Mapa pulsado", Toast.LENGTH_SHORT).show();
            }
        });


        //CAMBIO DE ANIMACION AL HACER DOBLE CLICK SOBRE UN MARCADOR / LUGAR DEL MAPA
/*
        LatLng SYDNEY=new LatLng(-33.88,151.21);
        LatLng MOUNTAIN_VIEW=new LatLng(37.4,-122.1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));//Iniciocon15dezoom



        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 5000,null); //5segundos
        CameraPosition cameraPosition=new CameraPosition.Builder()
                .target(castellon)  //Destinofinal
                .zoom(17)// Nuevozoomfinal
                .bearing(90)//Orientación de la cámara al este
                 .tilt(30)//Cámaraa 30 grados
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.oscuro)); //CARGAR ESTILO DESDE JSON

        //AÑADE LA POSICION ACTUAL ( SI NO TIENE PERMISOS APARECE UN MENSAJE PARA SOLICITARLOS)
        if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else{
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onInfoWindowClick(Marker m){
        Toast.makeText(this,
                "Has pulsado en: "+m.getPosition().latitude+" "+m.getPosition().longitude+" "+m.getTag(),
                Toast.LENGTH_LONG).show();
    }
}