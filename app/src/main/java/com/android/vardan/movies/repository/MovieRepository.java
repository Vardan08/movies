package com.android.vardan.movies.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MovieRepository {
    private final RequestQueue requestQueue;

    public MovieRepository(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchMovies(String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, onResponse, onError);
        requestQueue.add(request);
    }
}

