package com.goodbier.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RestClient {
    String username;
    String password;
    DefaultHttpClient httpclient;
    SharedPreferences preferences;

    public RestClient(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.username = preferences.getString("username", null);
        this.password = preferences.getString("password", null);
        this.httpclient = new DefaultHttpClient();
    }

    public boolean loginUser(String username, String password, Context context) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/login");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("password", password));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean logoutUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(username);
        editor.remove("password");
        editor.commit();
        return true;
    }

    public void newSession(Context context) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/login");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("password", password));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getBeers() {
        HttpGet request = new HttpGet("http://@10.0.2.2:3000/beers");

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            JSONArray beers = new JSONArray(httpclient.execute(request, handler));
            return beers;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createBeer(String beer, String style, String price) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/beers/create");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("beer", beer));
        params.add(new BasicNameValuePair("style", style));
        params.add(new BasicNameValuePair("price", price));
        params.add(new BasicNameValuePair("user", username));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editBeer(String id, String beer, String style, String price) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/beer/edit/" + id);

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("beer", beer));
        params.add(new BasicNameValuePair("style", style));
        params.add(new BasicNameValuePair("price", price));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBeer(String id, String revision) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/beer/delete/" + id);

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("revision", revision));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveRating(String rating, String id) {
        HttpPost request = new HttpPost("http://@10.0.2.2:3000/beer/" + id);

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("rating", rating));

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ResponseHandler <String> handler = new BasicResponseHandler();

        try {
            httpclient.execute(request, handler);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
