package com.example.nilanjandaw.movies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends FragmentActivity implements MovieListFragment.Callback {

    public static boolean isTwoPane = false;
    public static String DETAILS_TAG = "DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_holder) != null) {
            isTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_holder, new DetailsFragment(), DETAILS_TAG)
                        .commit();

            }
        } else {
            isTwoPane = false;
        }

        //movieGrid = (GridView) findViewById(R.id.grid_movie);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        MovieListFragment list = (MovieListFragment)getFragmentManager().findFragmentById(R.id.fragment_list);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popularity) {
            Constants.sortBy = "popularity.desc";
            list.getMovies();
        } else if (id == R.id.action_sort_rating) {
            Constants.sortBy = "vote_average.desc";
            list.getMovies();
        } else if (id == R.id.action_sort_favourite) {
            Constants.sortBy = "favourite_movies";
            list.getMovies();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(JSONArray array, Cursor cursor, int position) {
        Log.d("MainActivity", "two_pane");
        try {
            if (isTwoPane) {
                DetailsFragment detailsFragment;
                Bundle bundle = new Bundle();
                bundle.putString("movie_details", array.getJSONObject(position).toString());
                bundle.putBoolean("is_favorite", cursor.getCount() > 0);
                detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
                if (detailsFragment != null)
                    detailsFragment.updateDetails(bundle);

            }else{
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("movie_details", array.getJSONObject(position).toString());
                intent.putExtra("is_favorite", cursor.getCount() > 0);
                startActivity(intent);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
