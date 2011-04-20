package com.goodbier.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewBeer extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_beer);

    }

    public void buildNewBeer(View view) {
        EditText beer_input = (EditText) findViewById(R.id.beer);
        String beer = beer_input.getText().toString();

        EditText style_input = (EditText) findViewById(R.id.style);
        String style = style_input.getText().toString();

        EditText price_input = (EditText) findViewById(R.id.price);
        String price = price_input.getText().toString();

        RestClient client = new RestClient(this);
        client.newSession(this);
        boolean response = client.createBeer(beer, style, price);

        if (response) {
            startActivity(new Intent(this, com.goodbier.android.Beers.class));
        }
        else {
            Toast toast = Toast.makeText(this, "Error occured when creating new beer.", 400);
            toast.show();
        }
    }
}
