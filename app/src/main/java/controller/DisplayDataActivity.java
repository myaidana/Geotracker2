package controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import eva_aidana.geotracker.R;
import model.LocationData;
import model.LocationLog;

/**
 * Created by erevear on 5/16/15.
 */
public class DisplayDataActivity extends Activity implements OnMapReadyCallback {

    private LocationLog mLocationLog;
    private GoogleMap mGoogleMap;
    /**
     * On create method to initialize instances 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_data);

        mLocationLog = (LocationLog) getIntent().getParcelableExtra("locations");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mGoogleMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
    }

    private void print(LocationLog log) {
        List<LocationData> list = mLocationLog.getLocationList();

        for(int i = 0; i < list.size(); i++){
            Log.d("DISPLAY", "loc " + list.get(i).getMyLongitude());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(mLocationLog != null) {
            List<LocationData> locations = mLocationLog.getLocationList();

            LocationData location = locations.get(0);
            LatLng firstLatLng = new LatLng(location.getMyLatitude(), location.getMyLongitude());
            for (int i=0; i<locations.size(); i++) {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locations.get(i).getMyLatitude()
                                , locations.get(i).getMyLongitude()))
                        .title("My Locations"));
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15));
        }

    }
}
