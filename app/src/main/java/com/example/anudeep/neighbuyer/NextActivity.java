package com.example.anudeep.neighbuyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {
    Button search;
    ImageView im,im2,im3,im4,im5;
    TextView tv3,tv4,tv5,tv6,tv7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        search = (Button) findViewById(R.id.search);
        im = (ImageView) findViewById(R.id.imageView);
        im2 = (ImageView) findViewById(R.id.imageView2);
        im3 = (ImageView) findViewById(R.id.imageView3);
        im4 = (ImageView) findViewById(R.id.imageView4);
        im5 = (ImageView) findViewById(R.id.imageView5);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);
        tv7 = (TextView) findViewById(R.id.textView7);
        im.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);
        im4.setVisibility(View.INVISIBLE);
        im5.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
            }
        });

    }
    public void visible() {
        im.setVisibility(View.VISIBLE);
        im2.setVisibility(View.VISIBLE);
        im3.setVisibility(View.VISIBLE);
        im4.setVisibility(View.VISIBLE);
        im5.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.VISIBLE);
        tv4.setVisibility(View.VISIBLE);
        tv5.setVisibility(View.VISIBLE);
        tv6.setVisibility(View.VISIBLE);
        tv7.setVisibility(View.VISIBLE);
    }
}
