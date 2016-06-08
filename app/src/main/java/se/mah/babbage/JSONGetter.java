package se.mah.babbage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DennisW on 2016-06-08.
 */
public class JSONGetter {
    public static JSONArray getEntities(String type, double latitude, double longitude, int range) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://build.dia.mah.se/" + type + "?" + "latitude="
                + latitude + "&longitude=" + longitude + "&within=" + range);
        JSONArray array = null;
        try {
            array = new JSONArray(httpGet(sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public static JSONObject getEntitiyByID(String type, String id) throws JSONException{
        StringBuilder sb = new StringBuilder();
        sb.append("http://build.dia.mah.se/" + type + "/" + id);
        JSONArray array = null;
        JSONObject object = null;
        try {
            array = new JSONArray(httpGet(sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(array != null) {
            object = array.getJSONObject(0);
        }
        return object;
    }

    public static void putEntity(JSONObject object, String id) {

    }

    public static String httpGet(String urlStr) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }

        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        return sb.toString();
    }

    public static void httpPut() {

    }
}
