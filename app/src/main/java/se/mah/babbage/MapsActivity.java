package se.mah.babbage;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPoint;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.google.maps.android.geometry.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Controller controller;
    double latitude = 55.6077098;
    double longitude = 12.992013;
    int range = 500;
    public LatLng myLocation;

    public Map<Marker, CustomMarker> allMarkersMap = new HashMap<Marker, CustomMarker>();

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        controller = new Controller(this);
        float zoomLevel = (float) 14.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel));
    }

    /*public void addMarkers() {
        mMap.clear();
        for (int i = 0; i < controller.customMarkerArrayList.size(); i++) {
            CustomMarker customMarker = controller.customMarkerArrayList.get(i);
            Marker marker = mMap.addMarker(new MarkerOptions().position(customMarker.getLatLng()).draggable(false).title(customMarker.getName()));
            allMarkersMap.put(marker, customMarker);
        }
        MarkerAdapter adapter = new MarkerAdapter(this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.setOnMarkerClickListener(this);
    }*/

    public void updateMarkers(String filter){
        mMap.clear();
        allMarkersMap.clear();
        for(int i = 0; i < controller.customMarkerArrayList.size(); i++){
            CustomMarker customMarker = controller.customMarkerArrayList.get(i);
            if(filterMarkers(filter,customMarker.getSubCategory())){
                Marker marker = mMap.addMarker(new MarkerOptions().position(customMarker.getLatLng()).draggable(false).title(customMarker.getName()));
                allMarkersMap.put(marker, customMarker);
            }
        }
        MarkerAdapter adapter = new MarkerAdapter(this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.setOnMarkerClickListener(this);
    }

    public boolean filterMarkers(String filter, String category) {
        switch (filter) {
            case "toilets":
                return category.equals("toilets");
            case "playground":
                return category.equals("playground");
            case "sports":
                return category.equals("sports") || category.equals("swimming");
            case "bikepumps":
                return category.equals("bikepumps");
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.android_action_bar_spinner_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        switch (item.getItemId()) {
            case R.id.filter_toilets:
                updateMarkers("toilets");
                break;
            case R.id.filter_playgrounds:
                updateMarkers("playground");
                break;
            case R.id.filter_bikepumps:
                updateMarkers("bikepumps");
                break;
            case R.id.filter_all:
                updateMarkers("all");
                break;
            case R.id.filter_sports:
                updateMarkers("sports");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}
