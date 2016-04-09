package towfi.towfi;

import android.location.LocationManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

/**
 * Created by Nilanjan2 on 22-Aug-15.
 */
class myPhoneStateListener extends PhoneStateListener {
    int signalStrength;

    @Override
    public void onSignalStrengthsChanged(SignalStrength sig) {
        super.onSignalStrengthsChanged(sig);
        signalStrength = sig.getGsmSignalStrength();
        signalStrength = (2 * signalStrength) - 113; // -> dBm
        MapsActivity.setSignalStrength(signalStrength);

    }

}
