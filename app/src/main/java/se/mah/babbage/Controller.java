package se.mah.babbage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class Controller {
    public Controller(){
        getPOIs();
    }

    public void getPOIs(){
        JSONArray entities = JSONGetter.getEntities("poi", latitude, longtidut, range);
        for(int i = 0; i < entities.length(); i++){
            try {
                JSONObject row = entities.getJSONObject(i);
                JSONObject category = row.getJSONObject("category");
                JSONObject geometry = row.getJSONObject("geometry");
                JSONObject coordinates = geometry.getJSONObject("coordinates");
                Marker marker = new Marker(row.getString("id"),row.getString("name"), row.getString("description"), category.getString("main"), category.getString("sub"), coordinates.getDouble("0"), coordinates.getDouble("1"));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
