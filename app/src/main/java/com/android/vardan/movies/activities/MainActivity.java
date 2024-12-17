package com.android.vardan.movies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.vardan.movies.R;
import com.android.vardan.movies.data.MovieAdapter;
import com.android.vardan.movies.model.Movie;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.android.vardan.movies.utils.Constants;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getMovies();
    }

    private void getMovies() {
        String url = "https://www.omdbapi.com/?apikey=7fd21c39&s=supermen";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleResponse, this::handleError);

        requestQueue.add(request);
    }

    // Handles the API response and extracts movie data
    @SuppressLint("LongLogTag")
    private void handleResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Movie movie = parseMovie(jsonObject);
                movies.add(movie);
            }

            // Set up the adapter
            MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, movies);
            movieAdapter.setOnItemClickListener(MainActivity.this);
            recyclerView.setAdapter(movieAdapter);
        } catch (JSONException e) {
            Log.e("Error parsing movie data", e+"");
        }
    }

    // Handles errors from the API request
    @SuppressLint("LongLogTag")
    private void handleError(VolleyError error) {
        Log.e("Error parsing movie data", error + "");
    }

    // Extracts and returns a Movie object from a JSON object
    private Movie parseMovie(JSONObject jsonObject) {
        Movie movie = new Movie();

        movie.setTitle(getStringFromJson(jsonObject, "Title"));
        movie.setYear(getStringFromJson(jsonObject, "Year"));
        movie.setPosterUrl(getStringFromJson(jsonObject, "Poster"));
        movie.setDirector(getStringFromJson(jsonObject, "Director"));
        movie.setPlot(getStringFromJson(jsonObject, "Plot"));
        movie.setRuntime(getStringFromJson(jsonObject, "Runtime"));
        movie.setType(getStringFromJson(jsonObject, "Type"));

        return movie;
    }

    // Helper method to retrieve a string from JSON or return a default value if key is missing or null
    @SuppressLint("LongLogTag")
    private String getStringFromJson(JSONObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException e) {
                Log.e("Error parsing movie data", e + "");
            }
        }
        return "N/A";
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Movie clickedMovie = movies.get(position);

        intent.putExtra(Constants.KEY_TITLE, clickedMovie.getTitle());
        intent.putExtra(Constants.KEY_POSTER_URL, clickedMovie.getPosterUrl());
        intent.putExtra(Constants.KEY_YEAR, clickedMovie.getYear());
        intent.putExtra(Constants.KEY_DIRECTOR, clickedMovie.getDirector());
        intent.putExtra(Constants.KEY_PLOT, clickedMovie.getPlot());
        intent.putExtra(Constants.KEY_RUNTIME, clickedMovie.getRuntime());
        intent.putExtra(Constants.KEY_TYPE, clickedMovie.getType());

        startActivity(intent);
    }
}

