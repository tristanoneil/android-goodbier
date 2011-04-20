package com.goodbier.android;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;
import android.widget.TextView;

public class Beer extends Activity {
    RestClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        client = new RestClient(this);
        client.newSession(this);
        JSONArray response = client.getBeers();

        try {
            TextView beer = (TextView) findViewById(R.id.beer);
            TextView style = (TextView) findViewById(R.id.style);
            TextView price = (TextView) findViewById(R.id.price);
            RatingBar rating = (RatingBar) findViewById(R.id.rating);

            beer.setText(response.getJSONObject(id).get("beer").toString());
            style.setText(response.getJSONObject(id).get("style").toString());
            price.setText("Price: $" + response.getJSONObject(id).get("price").toString());

            rating.setTag(response.getJSONObject(id).get("_id").toString());

            if (response.getJSONObject(id).has("rating")) {
                rating.setRating(Float.parseFloat(response.getJSONObject(id).get("rating").toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buildRating();
    }

    public void buildRating() {
        final RatingBar rating = (RatingBar) findViewById(R.id.rating);
        
        rating.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                client.saveRating(Float.toString(rating.getRating()), rating.getTag().toString());
                return false;
            }
        });
    }
}
