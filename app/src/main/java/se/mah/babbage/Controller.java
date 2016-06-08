package se.mah.babbage;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class Controller {
    public Controller(){
        getEntities();
    }

    public void getEntities(){
        JSONArray entities = JSONGetter.getEntry();

    }


}
