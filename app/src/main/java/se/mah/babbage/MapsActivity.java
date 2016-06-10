package se.mah.babbage;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Controller controller;
    public LatLng myLocation;
    double rating;


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
        controller.fetchData();
        float zoomLevel = (float) 14.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel));
    }

    public void updateMarkers(String filter) {
        mMap.clear();
        allMarkersMap.clear();
        if(controller == null){
            return;
        }
        for (int i = 0; i < controller.customMarkerArrayList.size(); i++) {
            CustomMarker customMarker = controller.customMarkerArrayList.get(i);
            if (filterMarkers(filter, customMarker.getSubCategory())) {
                Marker marker;
                if(customMarker.isPois()){
                   marker = mMap.addMarker(new MarkerOptions().position(customMarker.getLatLng()).draggable(false).title(customMarker.getName()));
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(customMarker.getLatLng()).draggable(false).title(customMarker.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
                allMarkersMap.put(marker, customMarker);
            }
        }
        MarkerAdapter adapter = new MarkerAdapter(this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
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
        getMenuInflater().inflate(R.menu.spinner_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        updateMarkers("all");
                        break;
                    case 1:
                        updateMarkers("toilets");
                        break;
                    case 2:
                        updateMarkers("bikepumps");
                        break;
                    case 3:
                        updateMarkers("playground");
                        break;
                    case 4:
                        updateMarkers("sports");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        new GetRating(marker).execute();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, CommunicationForm.class);
        intent.putExtra("id", allMarkersMap.get(marker).getId());
        intent.putExtra("name", allMarkersMap.get(marker).getName());
        Log.d("BAJS", "" + allMarkersMap.get(marker).getId());
        startActivity(intent);
    }

    private class GetRating extends AsyncTask<Void, Void, Void> {

        Marker marker;

        public GetRating(Marker marker) {
            this.marker = marker;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                rating = JSONGetter.getMeanRating(allMarkersMap.get(marker).getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            allMarkersMap.get(marker).setMeanRating(rating);
            marker.showInfoWindow();
        }
    }
}
