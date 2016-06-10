package se.mah.babbage;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class Controller {
    public ArrayList<CustomMarker> customMarkerArrayList;
    int range = 1000;
    MapsActivity mapsActivity;
    CommunicationForm communicationForm;

    public Controller(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
        customMarkerArrayList = new ArrayList<>();
    }

    public Controller(CommunicationForm communicationForm) {
        this.communicationForm = communicationForm;
    }

    public void fetchData(){
        new GetPOISClass().execute();
    }

    public void sendUGC(String id, String comment, int rating) {
        new SendUGCClass(id, comment, rating).execute();
    }

    public class GetPOISClass extends AsyncTask<Void, Void, Void> {
        public JSONArray entities;
        public JSONArray places;
        @Override
        protected Void doInBackground(Void... params) {
            entities = JSONGetter.getEntities("pois", mapsActivity.myLocation.latitude, mapsActivity.myLocation.longitude, range);
            places = JSONGetter.getEntities("places", mapsActivity.myLocation.latitude, mapsActivity.myLocation.longitude, range);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < entities.length(); i++) {
                try {
                    JSONObject row = entities.getJSONObject(i);
                    JSONObject category = row.getJSONObject("category");
                    JSONObject geometry = row.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    String name = row.optString("name");
                    String description = row.optString("description");
                    String main = category.optString("main");
                    String sub = category.optString("sub");
                    if(name.equals("")){
                        JSONObject extras = row.getJSONObject("extras");
                        name = extras.getJSONObject("info").getString("name");
                        description = extras.getJSONObject("info").optString("description", "Ingen beskrivning");
                        main = "";
                        sub = "";
                    }
                    CustomMarker marker = new CustomMarker(row.getString("id"), name, description, main, sub, coordinates.getDouble(1), coordinates.getDouble(0), true);
                    customMarkerArrayList.add(marker);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < places.length(); i++) {
                try {
                    JSONObject row = places.getJSONObject(i);
                    JSONObject category = row.getJSONObject("category");
                    JSONObject geometry = row.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);

                    double minx = 180.0, miny = 180.0, maxx = 0.0, maxy = 0.0;
                    for(int j = 0; j < coordinates.length(); j++){
                        JSONArray temp = coordinates.getJSONArray(j);
                        for(int k = 0; k < temp.length(); k++){
                            if(temp.getDouble(0) > maxx){
                                maxx = temp.getDouble(0);
                            }
                            if(temp.getDouble(0) < minx){
                                minx = temp.getDouble(0);
                            }
                            if(temp.getDouble(1) > maxy){
                                maxy = temp.getDouble(1);
                            }
                            if (temp.getDouble(1) < miny){
                                miny = temp.getDouble(1);
                            }
                        }
                    }
                    double centerx = minx + ((maxx - minx) / 2);
                    double centery = miny + ((maxy - miny) / 2);

                    CustomMarker marker = new CustomMarker(row.getString("id"), row.getString("name"), "Stadsdel", "-", "-", centery, centerx, false);
                    customMarkerArrayList.add(marker);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mapsActivity.updateMarkers("all");
        }
    }

    private class SendUGCClass extends AsyncTask<Void, Void, Void> {
        String id, comment;
        int rating;
        public SendUGCClass(String id, String comment, int rating){
            this.id = id;
            this.comment = comment;
            this.rating = rating;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONGetter.postComment(id, "Felanmälan", comment, "", "");
                JSONGetter.postRating(id, rating, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
