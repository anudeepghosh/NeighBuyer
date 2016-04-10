package com.example.anudeep.neighbuyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.model.people.Person;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String FName = "fNameKey";
    public static final String LName = "lNameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String BASE_URL = "http://neighbuy1.mybluemix.net";

    SharedPreferences sharedpreferences;
    String city="New Delhi";
    int located = 0;
    final static Data data = new Data();
    String latLong[][] = {{"","22.36","28.37","13.08","18.55"},{"","88.24","77.17","80.19","72.50"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spinner cityChooser = (Spinner) findViewById(R.id.city);
        Button next =(Button) findViewById(R.id.next);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ImageButton location = (ImageButton) findViewById(R.id.location);
        ImageButton reset = (ImageButton) findViewById(R.id.reset);
        final EditText fstName = (EditText)findViewById(R.id.fstName);
        final EditText lstName = (EditText)findViewById(R.id.lstName);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText phone = (EditText)findViewById(R.id.phone);

        setSupportActionBar(toolbar);

        cityChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int position, long id) {
                city = adapter.getItemAtPosition(position).toString();
                if (position > 0) {
                    data.setCity(city);
                    data.setUserLatitude(Double.parseDouble(latLong[0][position]));
                    data.setUserLongitude(Double.parseDouble(latLong[1][position]));
                    Toast.makeText(getApplicationContext(), "" + data.getCity() + " " + data.getUserLatitude() + " " + data.getUserLongitude(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Select a city", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fstName.setText("");
                lstName.setText("");
                email.setText("");
                phone.setText("");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fstName!=null&&lstName!=null&&phone!=null&&email!=null) {
                    //to check if location is set or not
                    if(located==1) {
                        JSONObject data = createJson();
                        // post data to server
                        new PostData().execute(data);
                        Intent intent = new Intent(MainActivity.this, NextActivity.class);
                        startActivity(intent);


                    }else {
                        Toast.makeText(getApplicationContext(),"Locate Yourself!",Toast.LENGTH_SHORT).show();
                        fstName.requestFocus();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Fill ID Details",Toast.LENGTH_SHORT).show();
                    if (fstName != null) {
                        fstName.requestFocus();
                    }
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fstName!=null&&lstName!=null&&phone!=null&&email!=null) {
                    data.setFirstName(String.valueOf(fstName));
                    data.setLastName(String.valueOf(lstName));
                    data.setEmail(String.valueOf(email));
                    data.setPhone(String.valueOf(phone));

                    SharedPreferences.Editor editor = sharedpreferences.edit(); //saving input data
                    editor.putString(FName, String.valueOf(fstName));
                    editor.putString(LName, String.valueOf(lstName));
                    editor.putString(Phone, String.valueOf(phone));
                    editor.putString(Email, String.valueOf(email));
                    editor.apply();
                    located = 1;
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("City",city);
                    startActivityForResult(intent, 2);
                }else {
                    Toast.makeText(getApplicationContext(),"Fill ID Details",Toast.LENGTH_SHORT).show();
                    if (fstName != null) {
                        fstName.requestFocus();
                    }
                }
            }
        });
    }
    //Getting Map co-ordinates of current location

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iData) {
        super.onActivityResult(requestCode, resultCode, iData);
        switch(requestCode) {
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    Double usrLat = iData.getDoubleExtra("Latitude", 0.0);
                    Double usrLong = iData.getDoubleExtra("Longitude", 0.0);
                    data.setUserLatitude(usrLat);
                    data.setUserLongitude(usrLong);
                    located=1;
                    Toast.makeText(getApplicationContext(),""+usrLat+" "+usrLong,Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

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

    public JSONObject createJson() {
        InputStream inputStream = null;
        String result = "";
        JSONObject jsonObject = null;
        try {
            String json = "";

            // 3. build jsonObject
            jsonObject = new JSONObject();
            jsonObject.accumulate(FName, data.getFirstName());
            jsonObject.accumulate(LName, data.getLastName());
            jsonObject.accumulate(Phone, data.getPhone());
            jsonObject.accumulate(Email, data.getEmail());
            jsonObject.accumulate("City", data.getCity());
            jsonObject.accumulate("ULatitude", data.getUserLatitude());
            jsonObject.accumulate("ULongitude", data.getUserLongitude());
            //jsonObject.accumulate("UID", data.getID()); //to be generated on server side

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 11. return result
        return jsonObject;
    }

    //Async Task to ping server with new iser data
    class PostData extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url  = new URL(BASE_URL + "/newuser/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(params[0]));
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine).append("\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                return buffer.toString();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Server Response", s);
        }
    }
}
