package com.adcour.mymapas;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    //VARIABLES GOOGLE LOCATION SERVICES API
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = 0.0, mLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        new DrawerBuilder().withActivity(this).build();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.ic_launcher)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Ejemplo MaterialDrawer")
                                .withEmail("micorreo@iesserraperenxisa.com")
                                .withIcon(getResources().getDrawable(R.mipmap.ic_launcher_round, getTheme()))
                )
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true) //Muestra o no el icono de hamburguesa
                //.withToolbar(tbMiToolBar) //Lo asocia a nuestra toolbar
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.START) //Lo pone a la derecha (La hamburguesa sigue a la izquierda)
                .withAccountHeader(headerResult)
                .withSelectedItem(2)
                .withSliderBackgroundColor(getResources().getColor(R.color.accent)) //Color de fondo
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Opción 1")
                                .withIcon(android.R.drawable.btn_star_big_on),
                        new PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName("Opción 2"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Cerrar menú"),
                        new SecondaryDrawerItem()
                                .withIdentifier(4)
                                .withName("Salir App")
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                switch ((int)drawerItem.getIdentifier()) {
                                    case 1: {
                                        Toast.makeText(MapsActivity.this, "Opción 1 pulsada", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 2: {
                                        Toast.makeText(MapsActivity.this, "Opción 2 pulsada", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 3: break;
                                    case 4: {
                                        finish();
                                        break;
                                    }
                                }

                                mDrawer.closeDrawer();

                                return true;
                            }
                        })
                .build();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(16000);///
        locationRequest.setFastestInterval(8000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        Toast.makeText(MapsActivity.this, "Lat:" + mLatitude + " Lon:" + mLongitude, Toast.LENGTH_LONG).show();
                    }
                }
            }
        };




        //Toast.makeText(MapsActivity.this, "Títulos:"+titulos, Toast.LENGTH_SHORT).show();
    }


    private void requestLocations() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void removeLocations() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }


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

        //Toast.makeText(this, "Zoommin:" + mMap.getMinZoomLevel(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Zoommax:" + mMap.getMaxZoomLevel(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Zoomactual:" + mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();


        //LISTENER PARA SABER CUANDO CAMBIAMOS ZOOM
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //Toast.makeText(MapsActivity.this, "Zoom cambiado a:" + mMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();
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
        requestLocations();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


        //String titulos=" ";
        List<Localizacion>mLocalizaciones;
        mLocalizaciones=leerLocalizaciones();
        LatLng l;
        Marker marker2;
        for (Localizacion localizacion :mLocalizaciones){
            l=null;
            marker2=null;

            l = new LatLng(localizacion.getLatitud(), localizacion.getLongitud() );

            marker2 = mMap.addMarker(new MarkerOptions()
                    .position(l)
                    .title(localizacion.getTitulo())
                    //.snippet(localizacion.getEtiqueta()) //AÑADIR INFORMACION
                    //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel))
            );

            if (localizacion.getEtiqueta().equals("hotel") )
                marker2.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel));
            else if (localizacion.getEtiqueta().equals("ciudad") )
                marker2.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ciudad));
            marker2.setTag(localizacion.getEtiqueta());
        }

    }

    @Override
    public void onInfoWindowClick(Marker m){
        Toast.makeText(this,
                "Has pulsado en: "+m.getPosition().latitude+" "+m.getPosition().longitude+" "+m.getTag(),
                Toast.LENGTH_LONG).show();
    }






    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones=new ArrayList<>();
        try{
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc=builder.parse(getResources().openRawResource(R.raw.localizaciones));
            Element raiz=doc.getDocumentElement();
            NodeList items=raiz.getElementsByTagName("localizacion");

            for(int i=0; i<items.getLength(); i++){
                //recorretodosloselementos
                Node nodoLocalizacion=items.item(i);
                Localizacion localizacion= new Localizacion();
                for(int j =0; j <nodoLocalizacion.getChildNodes().getLength(); j++){
                    //recorrehijos
                    Node nodoActual=nodoLocalizacion.getChildNodes().item(j);//compruebasiesunelemento
                    if( nodoActual.getNodeType() ==Node.ELEMENT_NODE) {
                        if( nodoActual.getNodeName().equalsIgnoreCase("titulo") )
                            localizacion.setTitulo(nodoActual.getChildNodes().item(0).getNodeValue());

                        else if( nodoActual.getNodeName().equalsIgnoreCase("fragmento") )
                            localizacion.setFragmento(nodoActual.getChildNodes().item(0).getNodeValue());
                        else if( nodoActual.getNodeName().equalsIgnoreCase("etiqueta") )
                            localizacion.setEtiqueta(nodoActual.getChildNodes().item(0).getNodeValue());
                        else if( nodoActual.getNodeName().equalsIgnoreCase("latitud") ){
                            String latitud=nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLatitud(Double.parseDouble(latitud));
                        }
                        else if( nodoActual.getNodeName().equalsIgnoreCase("longitud") ) {
                            String longitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLongitud(Double.parseDouble(longitud));
                     }

                }
            }//fin for2 (hijos)

            localizaciones.add(localizacion);}//finfor1 (elementos)

            } catch(ParserConfigurationException e) {
                e.printStackTrace();
            } catch(IOException e) { e.printStackTrace(); }
            catch(SAXException e) { e.printStackTrace(); }


        return localizaciones;
    }//fin leer Localizaciones




}