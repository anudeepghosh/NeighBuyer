package com.example.anudeep.neighbuyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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

public class MainActivity extends AppCompatActivity {
    String city="New Delhi";
    final Data data = new Data();
    String latLong[][] = {{"","22.36","28.37","13.08","18.55"},{"","88.24","77.17","80.19","72.50"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spinner cityChooser = (Spinner) findViewById(R.id.city);
        Button next =(Button) findViewById(R.id.next);
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
                data.setCity(city);
                if (position != 0) {
                    data.setUserLatitude(Double.parseDouble(latLong[0][position]));
                    data.setUserLongitude(Double.parseDouble(latLong[1][position]));
                }
                Toast.makeText(getApplicationContext(), "" + data.getCity() + " " + data.getUserLatitude() + " " + data.getUserLongitude(), Toast.LENGTH_SHORT).show();
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
                    Intent intent=new Intent(MainActivity.this, NextActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill ID Details",Toast.LENGTH_SHORT).show();
                    fstName.requestFocus();
                }
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MapActivity.class);
                startActivityForResult(intent,2);
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
}
