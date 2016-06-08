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
    public ArrayList<Marker> markerArrayList;
    public JSONArray entities;
    double latitude = 55.6077098;
    double longitude = 12.992013;
    int range = 500;

    public Controller(){
        markerArrayList = new ArrayList<>();
        new GetPOISClass().execute();
    }

    public class GetPOISClass extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            entities = JSONGetter.getEntities("poi", latitude, longitude, range);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            while (entities==null){

            }
            for(int i = 0; i < entities.length(); i++){
                try {
                    JSONObject row = entities.getJSONObject(i);
                    JSONObject category = row.getJSONObject("category");
                    JSONObject geometry = row.getJSONObject("geometry");
                    JSONObject coordinates = geometry.getJSONObject("coordinates");
                    Marker marker = new Marker(row.getString("id"),row.getString("name"), row.getString("description"), category.getString("main"), category.getString("sub"), coordinates.getDouble("0"), coordinates.getDouble("1"));
                    markerArrayList.add(marker);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void getPOIs(){
    }
}
