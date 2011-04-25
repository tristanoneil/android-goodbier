package com.goodbier.android;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Beer extends Activity {
    RestClient client;
    int position;
    String id;
    String revision;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");

        client = new RestClient(this);
        client.newSession(this);
        JSONArray response = client.getBeers();

        try {
            TextView beer = (TextView) findViewById(R.id.beer);
            TextView style = (TextView) findViewById(R.id.style);
            TextView price = (TextView) findViewById(R.id.price);
            RatingBar rating = (RatingBar) findViewById(R.id.rating);

            beer.setText(response.getJSONObject(position).get("beer").toString());
            style.setText(response.getJSONObject(position).get("style").toString());
            price.setText("Price: $" + response.getJSONObject(position).get("price").toString());

            id = response.getJSONObject(position).get("_id").toString();
            revision = response.getJSONObject(position).get("_rev").toString();

            if (response.getJSONObject(position).has("rating")) {
                rating.setRating(Float.parseFloat(response.getJSONObject(position).get("rating").toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buildRating(this);
    }

    public void buildRating(final Context context) {
        final RatingBar rating = (RatingBar) findViewById(R.id.rating);
        
        rating.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                boolean response = client.saveRating(Float.toString(rating.getRating()), id);
                if (response) {
                    Toast toast = Toast.makeText(context, "Rating saved.", 400);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Error occured when saving rating.", 400);
                    toast.show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.beer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("Edit Beer")) {
            Intent edit_beer = new Intent(getApplicationContext(), com.goodbier.android.EditBeer.class);
            edit_beer.putExtra("position", position);
            edit_beer.putExtra("id", id);
            startActivity(edit_beer);
        }
        else if (item.getTitle().toString().equals("Delete Beer")) {
            boolean response = client.deleteBeer(id, revision);

            if (response) {
                Toast toast = Toast.makeText(this, "Successfully deleted beer.", 400);
                toast.show();

                startActivity(new Intent(this, com.goodbier.android.Beers.class));
            }
            else {
                Toast toast = Toast.makeText(this, "Error occured when deleting beer.", 400);
                toast.show();
            }
        }
        return true;
    }
}
