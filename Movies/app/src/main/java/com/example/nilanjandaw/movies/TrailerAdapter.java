package com.example.nilanjandaw.movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nilanjan Daw on 23/03/2016.
 * Project: Movies
 */
public class TrailerAdapter extends BaseAdapter {

    Context context;
    JSONArray trailerArray;

    public TrailerAdapter(Context context, JSONArray trailerArray) {
        this.context = context;
        this.trailerArray = trailerArray;
    }

    @Override
    public int getCount() {
        return trailerArray.length();
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
                        R.layout.trailer_layout, parent, false);
            }
            ImageView trailerImage = (ImageView) convertView.findViewById(R.id.trailer_image);
            TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_text);
            JSONObject trailer = trailerArray.getJSONObject(position);

            String posterURL = "http://img.youtube.com/vi/" + trailer.getString("key") + "/0.jpg";
            Log.d("Trailer", posterURL);
            Picasso.with(context)
                    .load(posterURL)
                    .placeholder(android.R.drawable.ic_media_play)
                    .into(trailerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Trailer", "Success");
                        }

                        @Override
                        public void onError() {
                            Log.d("Trailer", "Failed");
                        }
                    });

            trailerText.setText(trailer.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
