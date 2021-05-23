package com.example.final_proyect.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.final_proyect.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class Mapa_Fragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, LocationListener {

    private View rootView;
    private MapView mapView;
    private GoogleMap gMap;
    private FloatingActionButton fab;
    private LocationManager locationManager;
    private Location currentLocation;
    private Marker marker;
    private CameraPosition camera;
    private SearchView searchView;
    private Address address;

    public Mapa_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
        getActivity().setTitle("Mapa");

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        searchView = rootView.findViewById(R.id.buscar);
        mapView = (MapView) rootView.findViewById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Bundle parametros = new Bundle();
            String datos = parametros.getString("datos");

            @Override
            public boolean onQueryTextSubmit(String s) {
                //recoge los datos introducido en el buscador
                String locacion = searchView.getQuery().toString();
                //lista de los lugares
                List<Address> addressList=null;

                //verifica si hay datos introducidos
                if (locacion != null || !locacion.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(locacion, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList.size()>0){
                        address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        gMap.addMarker(new MarkerOptions().position(latLng).title(locacion));
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapView.getMapAsync(this);
        return rootView;
    }//Fin del onCreate

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // gMap.setMyLocationEnabled(true);
        // gMap.getUiSettings().setMyLocationButtonEnabled(false);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }

    private boolean isGPSEnabled() {
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (gpsSignal == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showInfoAlert() {
        new AlertDialog.Builder(getContext())
                .setTitle("Señal GPS")
                .setMessage("Tú no tienes activado el GPS. ¿Puedes tú activar el GPS ahora?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCELAR", null)
                .show();
    }

    @Override
    public void onClick(View view) {
        if (!this.isGPSEnabled()) {
            showInfoAlert();
        } else {
            if (CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                currentLocation = location;

                if (currentLocation != null) {
                    createOrUpdateMarkerByLocation(location);
                    zoomToLocation(location);
                }

            } else {
                // Ha denegado o es la primera vez que se le pregunta
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // No se le ha preguntado aún
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    currentLocation = location;

                    if (currentLocation != null) {
                        createOrUpdateMarkerByLocation(location);
                        zoomToLocation(location);
                    }
                } else {
                    // Ha denegado
                    Toast.makeText(getActivity(), "Por favor, habilita los permisos", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }
        }
    }

    private void createOrUpdateMarkerByLocation(Location location) {
        if (marker == null) {
            marker = gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true));
        } else {
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
        marker.setTitle("Posición actual");
        marker.setDraggable(true);
        marker.setSnippet("Latitud: "+location.getLatitude()+" Longitud "+location.getLongitude());


    }
    private void zoomToLocation(Location location) {
        camera = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)           // limit -> 21
                .bearing(0)         // 0 - 365º
                .tilt(30)           // limit -> 90
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

    }
    @Override
    public void onLocationChanged(Location location) {
        createOrUpdateMarkerByLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private boolean CheckPermission (String permission){
        int result= getActivity().checkCallingOrSelfPermission(permission);
        return  result == PackageManager.PERMISSION_GRANTED;
    }


}


