package com.example.anudeep.neighbuyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {
    Button search = (Button) findViewById(R.id.search);
    ImageView im = (ImageView) findViewById(R.id.imageView);
    ImageView im2 = (ImageView) findViewById(R.id.imageView2);
    ImageView im3 = (ImageView) findViewById(R.id.imageView3);
    ImageView im4 = (ImageView) findViewById(R.id.imageView4);
    ImageView im5 = (ImageView) findViewById(R.id.imageView5);
    TextView tv3 = (TextView) findViewById(R.id.textView3);
    TextView tv4 = (TextView) findViewById(R.id.textView4);
    TextView tv5 = (TextView) findViewById(R.id.textView5);
    TextView tv6 = (TextView) findViewById(R.id.textView6);
    TextView tv7 = (TextView) findViewById(R.id.textView7);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
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
