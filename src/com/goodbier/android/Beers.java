package com.goodbier.android;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Beers extends ListActivity {
	String[] beers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beers = buildBeerList();

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, beers));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent beer = new Intent(getApplicationContext(), com.goodbier.android.Beer.class);
                beer.putExtra("id", position);
                startActivity(beer);
            }
        });
    }

    public void onResume(Bundle savedInstanceState) {
        beers = buildBeerList();
    }

    public String[] buildBeerList() {
        RestClient client = new RestClient(this);
        client.newSession(this);
        JSONArray response = client.getBeers();

        beers = new String[response.length()];

        for (int i = 0; i < response.length(); i++) {
            try {
                beers[i] = response.getJSONObject(i).get("beer").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("New Beer")) {
            startActivity(new Intent(this, com.goodbier.android.NewBeer.class));
        }
        else if (item.getTitle().toString().equals("Logout")) {
            // Logout
            startActivity(new Intent(this, com.goodbier.android.Login.class));
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
