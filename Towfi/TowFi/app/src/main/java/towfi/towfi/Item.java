package towfi.towfi;

import android.location.Location;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * Created by Nilanjan2 on 22-Aug-15.
 */
@IBMDataObjectSpecialization("packet")
public class Item extends IBMDataObject {

    public double getLatitude() {
        return (double)getObject("latitude");
    }

    public void setLatitude(double latitude) {
        setObject("latitude", latitude);
    }

    public double getLongitude() {
        return (double)getObject("longitude");
    }

    public void setLongitude(double longitude) {
        setObject("longitude", longitude);
    }

    public int getSignalStrength() {
        return (int)getObject("signalStrength");
    }

    public void setSignalStrength(int signalStrength) {
        setObject("signalStrength", signalStrength);
    }
    public void setOperator(String operator) {
        setObject("operator",operator);
    }

    public void setZoom(float zoom) {
        setObject("zoom", zoom);
    }

    public void setLocation(Location location) {
        setObject("location", location);
    }
}
