package com.android.vardan.movies.utils;

import com.android.vardan.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieParser {
    public static ArrayList<Movie> parseMovies(JSONObject response) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        JSONArray jsonArray = response.getJSONArray("Search");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            movies.add(parseMovie(jsonObject));
        }
        return movies;
    }

    private static Movie parseMovie(JSONObject jsonObject) {
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

    private static String getStringFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) && !jsonObject.isNull(key) ? jsonObject.getString(key) : "N/A";
        } catch (JSONException e) {
            return "N/A";
        }
    }
}

