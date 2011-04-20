package com.goodbier.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class GoodBier extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);

        if (username != null && password != null) {
            startActivity(new Intent(this, com.goodbier.android.Beers.class));
        }
        else {
            startActivity(new Intent(this, com.goodbier.android.Login.class));
        }
    }
}
