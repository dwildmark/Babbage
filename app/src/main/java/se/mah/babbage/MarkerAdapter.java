package se.mah.babbage;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private MapsActivity mapsActivity;

    public MarkerAdapter(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = mapsActivity.getLayoutInflater().inflate(R.layout.marker_adapter, null);
        TextView title = (TextView) v.findViewById(R.id.tvMarkerTitle);
        title.setText(mapsActivity.allMarkersMap.get(marker).getName());

        TextView rating = (TextView) v.findViewById(R.id.tvRating);

        if (Double.isNaN(mapsActivity.allMarkersMap.get(marker).getMeanRating())) {
            rating.setText("No ratings");
        } else {
            rating.setText(mapsActivity.allMarkersMap.get(marker).getMeanRating() + "/10");
        }

        TextView description = (TextView) v.findViewById(R.id.tvMarkerDescription);
        description.setText(mapsActivity.allMarkersMap.get(marker).getDescription());

        return v;
    }
}
