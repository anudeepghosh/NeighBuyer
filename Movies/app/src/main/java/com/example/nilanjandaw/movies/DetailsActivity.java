package com.example.nilanjandaw.movies;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends Activity {

    @Bind(R.id.movieTitle)
    TextView movieTitle;
    @Bind(R.id.releaseDate)
    TextView releaseDate;
    @Bind(R.id.movieRatingText)
    TextView averageRating;
    @Bind(R.id.movieOverview)
    TextView movieOverview;
    @Bind(R.id.favouriteText)
    TextView favouriteText;
    @Bind(R.id.movieRating)
    RatingBar rating;
    @Bind(R.id.moviePoster)
    ImageView moviePoster;
    @Bind(R.id.favouriteIcon)
    ImageView favouriteIcon;
    @Bind(R.id.backgroundLayout)
    RelativeLayout backgroundLayout;
    @Bind(R.id.favourite_layout)
    LinearLayout favouriteLayout;
    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        try {
            Bundle info = getIntent().getExtras();

            // if the info bundle sent from the MainActivity is not null then the different visual
            // elements are set to their respective information
            final JSONObject movieInfo = setUpUI(info);
            favouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavourite) {
                        favouriteIcon.setImageResource(android.R.drawable.star_big_off);
                        favouriteText.setText("Set as favourite");
                        try {
                            getBaseContext().getContentResolver().delete(
                                    MovieProvider.Movies.CONTENT_URI,
                                    Columns.TITLE + "=?", new String[]{movieInfo.getString("original_title")});
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Cursor cursor = null;
                        try {
                            favouriteIcon.setImageResource(android.R.drawable.star_big_on);
                            favouriteText.setText("Favourite");

                            ContentValues values = new ContentValues();
                            values.put(Columns.POSTER_PATH, movieInfo.getString("poster_path"));
                            values.put(Columns.OVERVIEW, movieInfo.getString("overview"));
                            values.put(Columns.RELEASE_DATE, movieInfo.getString("release_date"));
                            values.put(Columns.TITLE, movieInfo.getString("original_title"));
                            values.put(Columns.BACKDROP_PATH, movieInfo.getString("backdrop_path"));
                            values.put(Columns.VOTE_AVERAGE, movieInfo.getString("vote_average"));
                            values.put(Columns.VOTE_COUNT, movieInfo.getString("vote_count"));

                            Uri uri = getBaseContext().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, values);

                            Log.d("fav", uri.toString());

                            cursor = getBaseContext().getContentResolver().query(
                                    MovieProvider.Movies.CONTENT_URI, null, null, null, null
                            );

                            cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {
                                Log.d("Cursor", cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
                                cursor.moveToNext();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null)
                                cursor.close();
                        }

                    }
                    isFavourite = !isFavourite;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in parsing Movie Info", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public JSONObject setUpUI(Bundle info) throws JSONException {
        if (info != null) {
            String movieInfoString = info.getString("movie_details");
            final JSONObject movieInfo = new JSONObject(movieInfoString);

            movieTitle.setText(movieInfo.getString("original_title"));
            releaseDate.setText(movieInfo.getString("release_date"));
            averageRating.setText("Average Rating: "
                    + movieInfo.getString("vote_average")
                    + " (" + movieInfo.getString("vote_count") + " votes)");
            movieOverview.setText(movieInfo.getString("overview"));
            rating.setRating(Float.parseFloat(movieInfo.getString("vote_average")) / 2.0f);
            if (info.getBoolean("is_favorite")) {
                favouriteIcon.setImageResource(android.R.drawable.star_big_on);
                favouriteText.setText("Favorite");
                isFavourite = true;
            } else {
                favouriteIcon.setImageResource(android.R.drawable.star_big_off);
                favouriteText.setText("Set as Favorite");
                isFavourite = false;
            }

            //creating the URL for poster
            String posterURL = GridAdapter.picassoPosterURL + "/"
                    + GridAdapter.picassoPosterSize + "/"
                    + movieInfo.getString("poster_path");
            Picasso.with(this)
                    .load(posterURL)
                    .placeholder(android.R.drawable.ic_media_play)
                    .into(moviePoster);

            final String backdropURL = GridAdapter.picassoPosterURL + "/"
                    + GridAdapter.picassoPosterSize + "/"
                    + movieInfo.getString("backdrop_path");

            // Using picasso to load the backdrop image to be set as watermark
            Picasso.with(this).load(backdropURL).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable background = new BitmapDrawable(getResources(), bitmap);
                    background.setAlpha(20);
                    backgroundLayout.setBackground(background);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

            return movieInfo;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
            MainActivity.sortBy = "popularity.desc";
        } else if (id == R.id.action_sort_rating) {
            MainActivity.sortBy = "vote_average.desc";
        } else if (id == R.id.action_sort_favourite) {
            MainActivity.sortBy = "favourite_movies";
        }

        return super.onOptionsItemSelected(item);
    }
}
