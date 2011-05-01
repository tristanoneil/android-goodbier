package com.goodbier.android;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditBeer extends Activity {
    RestClient client;
    int position;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_form);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        id = extras.getString("id");

        client = new RestClient(this);
        client.newSession(this);
        JSONArray response = client.getBeers();

        try {
            EditText beer = (EditText) findViewById(R.id.beer);
            EditText style = (EditText) findViewById(R.id.style);
            EditText price = (EditText) findViewById(R.id.price);

            beer.setText(response.getJSONObject(position).get("beer").toString());
            style.setText(response.getJSONObject(position).get("style").toString());
            price.setText(response.getJSONObject(position).get("price").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buildBeer(View view) {
        EditText beer_input = (EditText) findViewById(R.id.beer);
        String beer = beer_input.getText().toString();

        EditText style_input = (EditText) findViewById(R.id.style);
        String style = style_input.getText().toString();

        EditText price_input = (EditText) findViewById(R.id.price);
        String price = price_input.getText().toString();

        RestClient client = new RestClient(this);
        client.newSession(this);
        boolean response = client.editBeer(id, beer, style, price);

        if (response) {
            Toast toast = Toast.makeText(this, "Successfully saved beer.", 400);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), com.goodbier.android.Beer.class);
            intent.putExtra("position", position);
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(this, "Error occured when saving beer.", 400);
            toast.show();
        }
    }
}
