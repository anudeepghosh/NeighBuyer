package towfi.towfi;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ibm.mobile.services.data.IBMDataObject;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Nilanjan2 on 22-Aug-15.
 */
public class MyLocationListener implements LocationListener{

    Context context;

    MyLocationListener(Context c) {
        context=c;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        //Toast.makeText(context,Double.toString(latitude),Toast.LENGTH_LONG).show();
        //Toast.makeText(context,Double.toString(longitude),Toast.LENGTH_LONG).show();
        MapsActivity.mMap.clear();
        if(Math.abs(MapsActivity.signalStrength)<80) {
            MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Bad Signal"));
            MapsActivity.draw((int) Math.random() * 10, latitude, longitude, Color.RED);
        }
        else {
            MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Good Signal"));
            MapsActivity.draw((int) Math.random() * 10, latitude, longitude, Color.GREEN);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longitude))
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        MapsActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //Toast.makeText(context,MapsActivity.operator,Toast.LENGTH_LONG).show();
        Item item = new Item();
        item.setSignalStrength(MapsActivity.signalStrength);
        item.setLatitude(location.getLatitude());
        item.setLongitude(location.getLongitude());
        item.setOperator(MapsActivity.operator);
        item.setZoom(MapsActivity.mMap.getCameraPosition().zoom);
        item.setLocation(location);
        item.save().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                if (task.isFaulted()) {
                    // Handle errors
                    Log.d("Bla bLa bla", "error");
                } else {
                    Item myItem = (Item) task.getResult();
                    Log.d("hfvshsfsf", "shfhsfhsfvs");
                    // Do more work
                }
                return null;
            }
        });

        //  Log.d("Longitude", Double.toString(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
