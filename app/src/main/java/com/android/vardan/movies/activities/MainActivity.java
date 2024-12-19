package com.android.vardan.movies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

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
    private MovieAdapter movieAdapter;

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
        movieAdapter = new MovieAdapter(MainActivity.this, movies);
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

    // Inflating the menu in the activity's toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        // Get the search item and initialize SearchView
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Make sure the search view is always expanded and set up query hint
        searchView.setIconifiedByDefault(false); // Makes the search field visible when the icon is clicked
        searchView.setQueryHint("Search movies...");

        // When search is expanded, automatically show the keyboard
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();

        // Listener to handle search query text changes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optionally trigger an action when search is submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the movies based on the new query
                movieAdapter.filterMovies(newText);  // Filter movies based on search query
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // Handle search icon click and automatically open the keyboard
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionSearch) {
            // Focus and show the keyboard when the search icon is clicked
            SearchView searchView = (SearchView) item.getActionView();
            searchView.requestFocus();
            searchView.setIconified(false); // Ensure it's expanded and ready for typing
        }

        return super.onOptionsItemSelected(item);
    }
}
