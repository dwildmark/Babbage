package se.mah.babbage;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class CustomMarker {
    private String id;
    private String mainCategory;
    private String subCategory;
    private String name;
    private LatLng latLng;

    public boolean isPois() {
        return pois;
    }

    public void setPois(boolean pois) {
        this.pois = pois;
    }

    private boolean pois;

    public double getMeanRating() {
        return meanRating;
    }

    public void setMeanRating(double meanRating) {
        this.meanRating = meanRating;
    }

    private double meanRating;

    public CustomMarker(String id, String name, String description, String mainCategory, String subCategory, double latitude, double longitude, boolean pois){
        this.id = id;
        this.description = description;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.name = name;
        this.pois = pois;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
