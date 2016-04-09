package towfi.towfi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static Method getWifiApState;
    private static Method isWifiApEnabled;
    private static Method setWifiApEnabled;
    private static Method getWifiApConfiguration;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ImageView connectGuestButton;
    private Button hostButton;
    private TextView connectText;
    private boolean dataAvailable = false;
    private  WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private String[] wifiListString;
    private BroadcastReceiver mWifiScanReceiver;
    private boolean isConnected = false;

    static {
        // lookup methods and fields not defined publicly in the SDK.
        Class<?> cls = WifiManager.class;
        for (Method method : cls.getDeclaredMethods()) {
            String methodName = method.getName();
            if (methodName.equals("getWifiApState")) {
                getWifiApState = method;
            } else if (methodName.equals("isWifiApEnabled")) {
                isWifiApEnabled = method;
            } else if (methodName.equals("setWifiApEnabled")) {
                setWifiApEnabled = method;
            } else if (methodName.equals("getWifiApConfiguration")) {
                getWifiApConfiguration = method;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Set up text box and connection button
        connectText = (TextView) findViewById(R.id.textView);
        connectText.setText("Tap to Connect");
        connectGuestButton = (ImageView) findViewById(R.id.imageView);
        connectGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectToHost().execute();
            }


        });


        mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.d("WifiReceiver: ", "Inside");
                ScanResult scanResult=null;
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> mScanResults = wifiManager.getScanResults();
                    Log.d("No of Devices: ", Integer.toString(mScanResults.size()));
                    boolean foundTowfi=false;
                    for(ScanResult result : mScanResults)   {
                        if (result.SSID.toUpperCase().contains("TOWFI")) {
                            scanResult = result;
                            foundTowfi = true;
                            break;
                        }
                    }

                    if(foundTowfi){
                        String networkSSID=scanResult.SSID;
                        String networkPass="";
                        WifiConfiguration conf = new WifiConfiguration();
                        conf.SSID = "\"" + networkSSID + "\"";
                        conf.preSharedKey = "\""+ networkPass +"\"";
                        wifiManager.addNetwork(conf);

                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        for( WifiConfiguration i : list ) {
                            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                                wifiManager.disconnect();
                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();

                                break;
                            }
                        }
                    }
                    // add your logic here
                    for (ScanResult result : mScanResults) {
                        Log.d("Device: ", result.SSID);
                    }
                }
            }
        };

        hostButton = (Button) findViewById(R.id.hostButton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnected) {
                    ConnectivityManager connectivityManager = (ConnectivityManager)
                            getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    try {

                        dataAvailable = isDataEnabled(connectivityManager);
                        if (!dataAvailable) {
                            // Be the client
                            new SetUpHotspot().execute();
                            Log.d("Device", "Inside");
                            //broadcast receiver for incoming connections
                            mWifiScanReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context c, Intent intent) {
                                    Log.d("Testing intent", intent.getAction());
                                    ScanResult scanResult = null;
                                    List<ScanResult> mScanResults = wifiManager.getScanResults();
                                    Log.d("No of Devices: ", Integer.toString(mScanResults.size()));
                                    boolean foundTowfi=false;
                                    for(ScanResult result : mScanResults)   {
                                        if (result.SSID.toUpperCase().contains("TOWFI")) {
                                            scanResult = result;
                                            foundTowfi = true;
                                            break;
                                        }
                                    }
                                }
                            };

                        } else {

                            Toast.makeText(getApplicationContext(), "Data is ON!", Toast.LENGTH_SHORT).show();
                            /**
                             // Be the host
                             wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                             wifiManager.setWifiEnabled(true);

                             wifiManager.startScan();
                             wifiList = wifiManager.getScanResults();
                             wifiListString = new String[wifiList.size()];
                             for (int i=0; i<wifiList.size(); i++){

                             wifiListString[i] = (wifiList.get(i)).toString();
                             Log.d("Wifi ScanList: ", wifiListString[i]);
                             }

                             registerReceiver(mWifiScanReceiver,
                             new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                             wifiManager.startScan();
                             Log.d("Wifi", "hey Ya");
                             wifiList = wifiManager.getScanResults();
                             wifiListString = new String[wifiList.size()];
                             for (int i=0; i<wifiList.size(); i++){

                             wifiListString[i] = (wifiList.get(i)).toString();
                             Log.d("Wifi ScanList: ", wifiListString[i]);
                             }
                             **/

                        }
                    } catch (Exception e) {
                        // Some problem accessible private API
                        // TODO do whatever error handling you want here

                    }
                    connectGuestButton.setImageResource(R.drawable.towfi_active);
                    connectText.setText("Tap to Disconnect");
                    isConnected = true;
                } else {
                    connectGuestButton.setImageResource(R.drawable.towfi_inactive);
                    connectText.setText("Tap to Connect");
                    isConnected = false;
                }
            }
        });
    }

    private boolean isDataEnabled(ConnectivityManager connectivityManager) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> c = Class.forName(connectivityManager.getClass().getName());
        Method m = c.getDeclaredMethod("getMobileDataEnabled");
        m.setAccessible(true);
        boolean isDataAvailable = (Boolean)m.invoke(connectivityManager);
        Log.d("Data available", Boolean.toString(isDataAvailable));
        return isDataAvailable;
    }

    private void createHotspot() {
        Context context = getBaseContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        try {
            if(isHotspotOn()) {
                Log.d("Hotspot", "On");
                try {
                    wifiManager.setWifiEnabled(false);
                }catch (Exception e) {
                    Log.d("HotSpot Error", e.getMessage());
                }
            }
            //wifiConfiguration = new WifiConfiguration();
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiConfiguration wc = new WifiConfiguration();
            wc.SSID = "TowFi";
            wc.preSharedKey  = "\"password\"";
            wc.hiddenSSID = true;
            wc.status = WifiConfiguration.Status.ENABLED;
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            int res = wifi.addNetwork(wc);
            Log.d("WifiPreference", "add Network returned " + res );
            boolean b = wifi.enableNetwork(res, true);
            Log.d("WifiPreference", "enableNetwork returned " + b );
            setWifiApEnabled = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            setWifiApEnabled.invoke(wifiManager, wc, true);
            Log.d("Wifi", Boolean.toString(isHotspotOn()));
        } catch (Exception e) {
            Log.e("Error", "Error 126");
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks if the Hotspot is ON
     * @return true if Hotspot is ON else false
     */
    public boolean isHotspotOn() {
        Context context = getBaseContext();
        WifiManager wifimanager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    class SetUpHotspot extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Log.d("Async", "Done");
        }

        @Override
        protected Void doInBackground(Void... params) {
            createHotspot();
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mWifiScanReceiver);
        }
        catch (Exception e){

        }
    }

    public class ConnectToHost extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            connectToHost();
            return null;
        }
    }

    private void connectToHost() {

        String networkSSID = "Connectify-Nilanjan";
        String networkPass = "killzone24";
        Log.d("Client", "Called");
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = "\"" + networkSSID + "\"";
        configuration.wepKeys[0] = "\"" + networkPass + "\"";
        configuration.wepTxKeyIndex = 0;

        configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        configuration.preSharedKey = "\"" + networkPass + "\"";

        configuration.priority = 9999;
        WifiManager manager = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
        if (!manager.isWifiEnabled()) {
            manager.setWifiEnabled(true);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int networkID = manager.addNetwork(configuration);
        Log.d("Network ID", "" + networkID);
        Log.d("Network State", "" + manager.isWifiEnabled());
        List<ScanResult> deviceList = manager.getScanResults();
        for (ScanResult device: deviceList) {
            Log.d("Device Found", device.SSID + " " +device.level);
        }
        manager.disconnect();
        boolean status = manager.enableNetwork(networkID, true);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (status)
            manager.saveConfiguration();
        Log.d("status", "" + status);
        manager.reconnect();

    }
}
