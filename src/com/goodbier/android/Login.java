package com.goodbier.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void buildLoginRequest(View view) {
        EditText user_input = (EditText) findViewById(R.id.username);
        EditText password_input = (EditText) findViewById(R.id.password);

        String username = user_input.getText().toString();
        String password = password_input.getText().toString();

        RestClient client = new RestClient(this);
        boolean response = client.loginUser(username, password, this);

        if (response) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();

            Toast toast = Toast.makeText(this, "Successfully logged in.", 400);
            toast.show();

            startActivity(new Intent(this, com.goodbier.android.Beers.class));
        }
        else {
            Toast toast = Toast.makeText(this, "Username or password incorrect.", 400);
            toast.show();
        }
    }
}
