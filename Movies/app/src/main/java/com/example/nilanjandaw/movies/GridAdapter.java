package com.example.nilanjandaw.movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nilanjan Daw on 31/01/2016.
 * Project: Movies
 */
public class GridAdapter extends BaseAdapter {
    public static final String picassoPosterURL = "http://image.tmdb.org/t/p/";
    public static final String picassoPosterSize = "w185";
    Context context;
    JSONArray movies;

    public GridAdapter(Context context, JSONArray movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null || getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.layout_item, parent, false);
            }
            ImageView moviePoster = (ImageView) convertView.findViewById(R.id.item_image);
            JSONObject movie = movies.getJSONObject(position);
            String posterName = movie.getString("poster_path");
            String posterURL = picassoPosterURL + "/" + picassoPosterSize + "/" + posterName;
            Log.d("PosterURL", posterURL);
            Picasso.with(context).load(posterURL).into(moviePoster);
            return convertView;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
