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
import com.android.vardan.movies.repository.MovieRepository;
import com.android.vardan.movies.utils.MovieParser;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.android.vardan.movies.utils.Constants;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;
    private MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        movieRepository = new MovieRepository(this);

        fetchMovies();
    }

    private void fetchMovies() {
        String url = "https://www.omdbapi.com/?apikey=7fd21c39&s=supermen";
        movieRepository.fetchMovies(url, this::handleResponse, this::handleError);
    }

    @SuppressLint("LongLogTag")
    private void handleResponse(JSONObject response) {
        try {
            movies = MovieParser.parseMovies(response);
            setupRecyclerView();
        } catch (JSONException e) {
            Log.e("Error parsing movie data", e + "");
        }
    }

    @SuppressLint("LongLogTag")
    private void handleError(VolleyError error) {
        Log.e("Error fetching movie data", error.toString());
    }

    private void setupRecyclerView() {
        MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, movies);
        movieAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(movieAdapter);
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


