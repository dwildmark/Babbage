package se.mah.babbage;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by jonasremgard on 08/06/16.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private MapsActivity mapsActivity;
    private PopupWindow popupWindow;
    public MarkerAdapter(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = mapsActivity.getLayoutInflater().inflate(R.layout.marker_adapter, null);
        TextView title = (TextView) v.findViewById(R.id.tvMarkerTitle);
        title.setText(mapsActivity.allMarkersMap.get(marker).getName());

        TextView description = (TextView) v.findViewById(R.id.tvMarkerDescription);
        description.setText(mapsActivity.allMarkersMap.get(marker).getDescription());

        //CommunicationForm communicationForm = new CommunicationForm();
        //popupWindow = new PopupWindow();
        /*Button addComment = (Button) v.findViewById(R.id.btnMarkerAddComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SHIT", "NU SÅ!!!!");
                //popupWindow.showAtLocation(mapsActivity.getCurrentFocus(), Gravity.BOTTOM, 10, 10);
                //popupWindow.update(50, 50, 300, 80);
                Log.d("BAJS", "CLICKED!");
            }
        });*/

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
