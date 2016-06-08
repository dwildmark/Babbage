package se.mah.babbage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            String str = httpGet(sb.toString());
            JSONObject obj = new JSONObject(str);
            array = obj.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public static JSONObject getEntityByID(String type, String id) throws JSONException{
        StringBuilder sb = new StringBuilder();
        sb.append("http://build.dia.mah.se/" + type + "/" + id);
        JSONArray array = null;
        JSONObject object = null;
        try {
            String str = httpGet(sb.toString());
            if(type.equals("ugc")) {
                object = new JSONObject(str);
            } else {
                JSONObject obj = new JSONObject(str);
                array = obj.getJSONArray("results");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(array != null) {
            object = array.getJSONObject(0);
        }
        return object;
    }

    public static void postEntity(JSONObject object, String id) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("http://build.dia.mah.se/ugc");
        httpPost(sb.toString(), object.toString());
    }

    public static String httpGet(String urlStr) throws IOException{
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Accept", "application/json");
            try {
                // Buffer the result into a string
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                return sb.toString();

            } finally {
                conn.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public static String httpPost(String urlStr, String data) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Accept",
                "application/json");

        // Create the form content
        OutputStream out = conn.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(data);
        writer.close();
        out.close();

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
}