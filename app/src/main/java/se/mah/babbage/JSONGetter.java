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
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public static void postComment(String ugcId, String title, String comment, String username, String realname) throws IOException {
        String[] params = {"title", "comment", "username", "realname"};
        String[] data = {title, comment, username, realname};
        String url = "http://build.dia.mah.se/ugc/" + ugcId + "/comments";
        httpPost(url, params, data);
    }

    public static void postRating(String ugcId, int rating, String username) throws IOException {
        String[] params = {"rating", "username" };
        String[] data = {"" + rating, username};
        httpPost("http://build.dia.mah.se/ugc/" + ugcId + "/ratings", params, data);
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

    public static double getMeanRating(String id) throws JSONException {
        JSONObject object = getEntityByID("ugc", id);
        if(object != null) {
            Log.d("BAJS", "Object is not null");
            if (object.get("ratings") != null) {
                JSONArray ratings = object.getJSONArray("ratings");
                int sum = 0;
                for (int i = 0; i < ratings.length(); i++) {
                    JSONObject rating = (JSONObject) ratings.get(i);
                    sum += rating.getInt("rating");
                }
                Log.d("BAJS", "WORKS!!!");
                return (double) sum / ratings.length();
            }
        }
        Log.d("BAJS", "RETURN 0000000");
        return 0.0;
    }

    public static void httpPost(String urlStr, String[] params, String[] data) throws IOException {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i]);
            sb.append("=");
            sb.append(data[i]);
            sb.append("&");
        }

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, sb.toString() );
        Request request = new Request.Builder()
                .url(urlStr)
                .post(body)
                .addHeader("cache-control", "no-cache")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200) {
            throw new IOException(response.message());
        }
    }
}