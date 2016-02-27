package towfi.towfi;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.push.IBMPush;

import bolts.Continuation;
import bolts.Task;

public class MapsActivity extends FragmentActivity {

    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TelephonyManager TelephonManager;
    static int signalStrength = 0;
    myPhoneStateListener pslistener;
    static LocationManager mlocManager;
    static double latitude,longitude;
    String applicationID, applicationRoute, applicationSecret;
    public static String operator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        try {
            applicationRoute="http://towfi.mybluemix.net";
            applicationID="d650f359-8d80-4663-9a65-a4138b6ca532";
            applicationSecret="936130b89dba1a497f1424ba7499e6a455c7275a";
            IBMBluemix.initialize(this, applicationID, applicationSecret, applicationRoute);
            IBMCloudCode.initializeService();
            IBMData.initializeService();
            IBMPush.initializeService();
            mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            pslistener = new myPhoneStateListener();
            TelephonManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            operator=TelephonManager.getNetworkOperatorName();
            TelephonManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            MyLocationListener mlocListener = new MyLocationListener(this);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

        }
        catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static  void draw(int color,double latitude, double longitude,int x) {

        //Color c=Color.parseColor((ColorOperations.hsvToRgb(100, 100, 57)))
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .fillColor(x)
                .radius(100);
        Circle circle = mMap.addCircle(circleOptions);

    }
    public static int getStrength() {
        return signalStrength;
    }

    public static void setSignalStrength(int x) {

        Log.d("setSignaStrength", "called");
        signalStrength=x;
        Log.d("Signal Strength", Integer.toString(signalStrength));
        Location location=mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d("Location", location.getLatitude() + " " + location.getLongitude());
            Item item = new Item();
            item.setSignalStrength(signalStrength);
            item.setLatitude(location.getLatitude());
            item.setLongitude(location.getLongitude());
            item.setOperator(operator);
            item.setZoom(mMap.getCameraPosition().zoom);
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
            if(Math.abs(MapsActivity.signalStrength)<80) {
                MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Bad Signal"));
                MapsActivity.draw((int) Math.random() * 10, latitude, longitude, Color.RED);
            }
            else {
                MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Good Signal"));
                MapsActivity.draw((int) Math.random() * 10, latitude, longitude, Color.GREEN);
            }
        }
    }

    public static void setLatitudeLongitude(double a,double b) {
        latitude=a;
        longitude=b;
        String x=Double.toString(a);
        String y=Double.toString(b);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }



}
