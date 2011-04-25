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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Beers extends ListActivity {
    RestClient client;
    String[] beers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beers = buildBeerList();

        if (beers != null) {
            setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, beers));
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);

            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), com.goodbier.android.Beer.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
    }

    public void onResume(Bundle savedInstanceState) {
        beers = buildBeerList();
    }

    public String[] buildBeerList() {
        client = new RestClient(this);
        client.newSession(this);
        JSONArray response = client.getBeers();

        if (response != null) {
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
        else {
            Toast toast = Toast.makeText(this, "An error occurred, cannot fetch beers.", 400);
            toast.show();
        }
        return null;
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
            boolean response = client.logoutUser(this);
            client.httpclient.getConnectionManager().shutdown();

            if (response) {
                Toast toast = Toast.makeText(this, "Successfully logged out.", 400);
                toast.show();

                startActivity(new Intent(this, com.goodbier.android.Login.class));
            }
            else {
                Toast toast = Toast.makeText(this, "An error occurred while logging out.", 400);
                toast.show();
            }
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
