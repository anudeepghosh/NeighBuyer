package com.example.nilanjandaw.movies;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    public static final String picassoPosterURL = "http://image.tmdb.org/t/p/";
    public static final String picassoPosterSize = "w185";
    public static final String movieURL = "http://api.themoviedb.org/3/discover/movie?";
    public static String sortBy = "popularity.desc";
    public static String apiKey = "8378a35f9b9693560120205b5cb6dab5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String httpMovieURL = movieURL + "sort_by=" + sortBy + "&api_key=" + apiKey;
        new AsyncHttpNetworkHandler().execute(httpMovieURL);

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
