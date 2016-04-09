package com.example.nilanjandaw.movies;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class MovieListFragment extends Fragment {
    GridView movieGrid;
    private Callback mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        movieGrid = (GridView) rootView.findViewById(R.id.grid_movie);
        return rootView;
    }

    public void getMovies() {
        if (!Constants.sortBy.equalsIgnoreCase("favourite_movies")) {
            String httpMovieURL = Constants.movieURL + "sort_by=" + Constants.sortBy + "&api_key=" + Constants.apiKey;
            new AsyncHttpNetworkHandler().execute(httpMovieURL);
        } else {
            Cursor cursor = null;
            JSONArray favoriteArray = new JSONArray();
            try {
                cursor = getActivity().getContentResolver().query(
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
    public void onResume() {
        super.onResume();
        getMovies();
    }


    private void setUpAdapter(final JSONArray array) {

        movieGrid.setAdapter(new GridAdapter(getActivity(), array));
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(
                            MovieProvider.Movies.CONTENT_URI,
                            null,
                            Columns.TITLE + "=?",
                            new String[]{array.getJSONObject(position).getString("original_title")},
                            null
                    );
                    mListener = (Callback) getActivity();
                    mListener.onItemSelected(array, cursor, position);
                    //showDetails(array, cursor, position);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        });
    }

    /**
    private void showDetails(JSONArray array, Cursor cursor, int position) throws JSONException {
        if (MainActivity.isTwoPane) {
            Log.d("DetailsFragment", "two_pane");
            DetailsFragment detailsFragment = DetailsFragment.newInstance(array.getJSONObject(position).toString(),
                    cursor.getCount() > 0
            );

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_container, detailsFragment)
                    .commit();

        } else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("movie_details", array.getJSONObject(position).toString());
            intent.putExtra("is_favorite", cursor.getCount() > 0);
            startActivity(intent);
        }
    }
     */

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

    public interface Callback {
        void onItemSelected(JSONArray array, Cursor cursor, int position);
    }
}
