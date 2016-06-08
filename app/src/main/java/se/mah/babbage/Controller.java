package se.mah.babbage;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class Controller {
    public ArrayList<CustomMarker> customMarkerArrayList;
    public JSONArray entities;
    double latitude = 55.6077098;
    double longitude = 12.992013;
    int range = 500;
    MapsActivity mapsActivity;

    public Controller(MapsActivity mapsActivity){
        this.mapsActivity = mapsActivity;
        customMarkerArrayList = new ArrayList<>();
        new GetPOISClass().execute();
    }

    public class GetPOISClass extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            entities = JSONGetter.getEntities("pois", latitude, longitude, range);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0; i < entities.length(); i++){
                try {
                    JSONObject row = entities.getJSONObject(i);
                    JSONObject category = row.getJSONObject("category");
                    JSONObject geometry = row.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    CustomMarker marker = new CustomMarker(row.getString("id"),row.getString("name"), row.getString("description"), category.getString("main"), category.getString("sub"), coordinates.getDouble(1), coordinates.getDouble(0));
                    customMarkerArrayList.add(marker);
                    mapsActivity.addMarkers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mapsActivity.addMarkers();
        }
    }
    public void getPOIs(){

    }
}
