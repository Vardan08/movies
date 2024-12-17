package com.android.vardan.movies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.vardan.movies.R;
import com.android.vardan.movies.data.MovieAdapter;
import com.android.vardan.movies.model.Movie;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getMovies();
    }

    private void getMovies() {

        String url = "https://www.omdbapi.com/?apikey=7fd21c39&s=supermen";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Search");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String posterUrl = jsonObject.getString("Poster");

                        Movie movie = new Movie();
                        movie.setTitle(title);
                        movie.setYear(year);
                        movie.setPosterUrl(posterUrl);


                        movies.add(movie);
                    }

                    movieAdapter = new MovieAdapter(MainActivity.this,
                            movies);

                    movieAdapter.setOnItemClickListener(MainActivity.this);
                    recyclerView.setAdapter(movieAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this,
                DetailActivity.class);
        Movie clickedMovie = movies.get(position);

        intent.putExtra(Constants.KEY_TITLE, clickedMovie.getTitle());
        intent.putExtra(Constants.KEY_POSTER_URL, clickedMovie.getPosterUrl());
        intent.putExtra(Constants.KEY_YEAR, clickedMovie.getYear());
        intent.putExtra(Constants.KEY_DIRECTOR, clickedMovie.getDirector());
        intent.putExtra(Constants.KEY_PLOT, clickedMovie.getPlot());
        intent.putExtra(Constants.KEY_RUNTIME, clickedMovie.getRuntime());


        startActivity(intent);

    }
}
