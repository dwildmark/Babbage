package se.mah.babbage;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class Marker {
    private String id;
    private String name;
    private LatLng latLng;

    public Marker(String id, String name, double latitude, double longitude){
        this.id = id;
        this.name = name;
        setLatLng(latitude,longitude);
    }

    private void setLatLng(double latitude, double longitude) {
        latLng = new LatLng(latitude,longitude);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
