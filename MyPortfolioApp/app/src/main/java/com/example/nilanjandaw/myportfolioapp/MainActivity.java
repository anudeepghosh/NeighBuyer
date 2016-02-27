package com.example.nilanjandaw.myportfolioapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button buttonSpotify, buttonScore, buttonLibrary, buttonBuild, buttonBacon, buttonCapstone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSpotify = (Button) findViewById(R.id.button);
        buttonScore = (Button) findViewById(R.id.button2);
        buttonLibrary = (Button) findViewById(R.id.button3);
        buttonBuild = (Button) findViewById(R.id.button4);
        buttonBacon = (Button) findViewById(R.id.button5);
        buttonCapstone = (Button) findViewById(R.id.button6);

        buttonSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Spotify Streamer App!", Toast.LENGTH_LONG).show();
            }
        });

        buttonScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Scores App!", Toast.LENGTH_LONG).show();
            }
        });

        buttonLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Library App!", Toast.LENGTH_LONG).show();
            }
        });

        buttonBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Build It Bigger App!", Toast.LENGTH_LONG).show();
            }
        });

        buttonBacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Bacon Reader App!", Toast.LENGTH_LONG).show();
            }
        });

        buttonCapstone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "This button will launch my Capstone App!", Toast.LENGTH_LONG).show();
            }
        });
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
