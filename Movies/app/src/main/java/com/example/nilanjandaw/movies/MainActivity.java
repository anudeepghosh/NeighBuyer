package com.example.nilanjandaw.movies;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    public static final String movieURL = "http://api.themoviedb.org/3/discover/movie?";
    public static String sortBy = "popularity.desc";
    public static String apiKey = "8378a35f9b9693560120205b5cb6dab5";
    GridView movieGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieGrid = (GridView) findViewById(R.id.grid_movie);

    }

    public void getMovies() {
        if (!sortBy.equalsIgnoreCase("favourite_movies")) {
            String httpMovieURL = movieURL + "sort_by=" + sortBy + "&api_key=" + apiKey;
            new AsyncHttpNetworkHandler().execute(httpMovieURL);
        } else {
            Cursor cursor = null;
            JSONArray favoriteArray = new JSONArray();
            try {
                cursor = getBaseContext().getContentResolver().query(
                        MovieProvider.Movies.CONTENT_URI, null, null, null, null
                );

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.d("Cursor", cursor.getString(cursor.getColumnIndex(Columns.TITLE)));

                    JSONObject favoriteMovie = new JSONObject();
                    favoriteMovie.put("id", cursor.getString(cursor.getColumnIndex(Columns._ID)));
                    favoriteMovie.put("original_title", cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
                    favoriteMovie.put("poster_path", cursor.getString(cursor.getColumnIndex(Columns.POSTER_PATH)));
                    favoriteMovie.put("overview", cursor.getString(cursor.getColumnIndex(Columns.OVERVIEW)));
                    favoriteMovie.put("release_date", cursor.getString(cursor.getColumnIndex(Columns.RELEASE_DATE)));
                    favoriteMovie.put("backdrop_path", cursor.getString(cursor.getColumnIndex(Columns.BACKDROP_PATH)));
                    favoriteMovie.put("vote_average", cursor.getString(cursor.getColumnIndex(Columns.VOTE_AVERAGE)));
                    favoriteMovie.put("vote_count", cursor.getString(cursor.getColumnIndex(Columns.VOTE_COUNT)));

                    favoriteArray.put(favoriteMovie);
                    cursor.moveToNext();
                }
                setUpAdapter(favoriteArray);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMovies();
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
        if (id == R.id.action_sort_popularity) {
            sortBy = "popularity.desc";
            getMovies();
        } else if (id == R.id.action_sort_rating) {
            sortBy = "vote_average.desc";
            getMovies();
        } else if (id == R.id.action_sort_favourite) {
            sortBy = "favourite_movies";
            getMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpAdapter(final JSONArray array) {

        movieGrid.setAdapter(new GridAdapter(getBaseContext(), array));
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = null;
                try {
                    cursor = getBaseContext().getContentResolver().query(
                            MovieProvider.Movies.CONTENT_URI,
                            null,
                            Columns.TITLE + "=?",
                            new String[] {array.getJSONObject(position).getString("original_title")},
                            null
                    );
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("movie_details", array.getJSONObject(position).toString());
                    intent.putExtra("is_favorite", cursor.getCount() > 0);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        });
    }

    public class AsyncHttpNetworkHandler extends AsyncTask<String, Void, Integer> {

        JSONArray jsonResponseArray = null;

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String httpMovieURL = params[0];
            Log.d("URL", httpMovieURL);
            try {
                URL url = new URL(httpMovieURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    Log.d("Request", "Successful");
                    String result = readResponse(inputStream);
                    Log.d("Response", result);
                } else {
                    Log.d("Request", "Failed");
                }
                return statusCode;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    urlConnection.disconnect();
                }
            }
            Log.d("URL", httpMovieURL);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setUpAdapter(jsonResponseArray);

        }

        private String readResponse(InputStream inputStream) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line, result = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonResponse = new JSONObject(result);
                jsonResponseArray = jsonResponse.optJSONArray("results");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
