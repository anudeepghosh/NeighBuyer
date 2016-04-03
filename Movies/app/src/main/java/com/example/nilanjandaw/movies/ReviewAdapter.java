package com.example.nilanjandaw.movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Nilanjan Daw on 28/03/2016.
 * Project: Movies
 */
public class ReviewAdapter extends BaseAdapter {

    Context context;
    JSONArray reviewArray;
    public ReviewAdapter(Context context, JSONArray reviewArray) {
        this.context = context;
        this.reviewArray = reviewArray;
    }

    @Override
    public int getCount() {
        return reviewArray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null || getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.review_layout, parent, false);
            }
            JSONObject review = reviewArray.getJSONObject(position);
            Log.d("Review", position + " " + review.getString("author"));
            TextView reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
            TextView reviewText = (TextView) convertView.findViewById(R.id.review_text);
            reviewAuthor.setText(review.getString("author"));
            reviewText.setText(review.getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
