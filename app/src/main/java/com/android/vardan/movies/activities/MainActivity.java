package com.android.vardan.movies.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

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

        fetchMovies(); // Fetch the default movies
    }

    private void fetchMovies() {
        String url = "https://www.omdbapi.com/?apikey=7fd21c39&s=supermen";
        movieRepository.fetchMovies(url, this::handleResponse, this::handleError);
    }

    private void searchMovies(String query) {
        String url = "https://www.omdbapi.com/?apikey=7fd21c39&s=" + query;
        movieRepository.fetchMovies(url, this::handleSearchResponse, this::handleError);
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(MainActivity.this, movies);
        movieAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(movieAdapter);
    }

    @SuppressLint("LongLogTag")
    private void handleResponse(JSONObject response) {
        try {
            movies = MovieParser.parseMovies(response);
            setupRecyclerView();
        } catch (JSONException e) {
            Log.e("Error parsing movie data", e.toString());
        }
    }

    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
    private void handleSearchResponse(JSONObject response) {
        try {
            ArrayList<Movie> searchedMovies = MovieParser.parseMovies(response);
            if (!searchedMovies.isEmpty()) {
                movies.clear();
                movies.addAll(searchedMovies);
                movieAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No movies found for the search query!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("Error parsing search data", e.toString());
        }
    }

    @SuppressLint("LongLogTag")
    private void handleError(VolleyError error) {
        Log.e("Error fetching movie data", error.toString());
        Toast.makeText(this, "Error fetching data. Please try again later.", Toast.LENGTH_SHORT).show();
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

    // Inflate the menu in the activity's toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconifiedByDefault(false); // Ensure search field is always visible
        searchView.setQueryHint("Search movies...");

        // Listener to handle search item expand
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                openKeyboardExplicitly(searchView); // Force open the keyboard
                return true; // Allow expansion
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                closeKeyboard(); // Close the keyboard when search view collapses
                return true; // Allow collapse
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query); // Perform the search
                closeKeyboard(); // Close the keyboard after the search
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    // Method to close the keyboard
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (imm != null && currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }


    private void openKeyboardExplicitly(View view) {
        view.requestFocus(); // Request focus on the view
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // Force open the keyboard
        }
    }




}
