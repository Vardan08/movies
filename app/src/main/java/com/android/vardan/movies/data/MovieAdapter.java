package com.android.vardan.movies.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vardan.movies.R;
import com.android.vardan.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private final ArrayList<Movie> movies;
    private OnItemClickListener listener;
    private final ArrayList<Movie> originalMovies; // Store the original list for search functionality

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.originalMovies = new ArrayList<>(movies);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie currentMovie = movies.get(i);

        String title = currentMovie.getTitle();
        String year = currentMovie.getYear();
        String posterUrl = currentMovie.getPosterUrl();
        String director = currentMovie.getDirector();
        String plot = currentMovie.getPlot();
        String runtime = currentMovie.getRuntime();
        String type = currentMovie.getType();

        movieViewHolder.titleTextView.setText(title);
        movieViewHolder.yearTextView.setText("Year: " + year);
        movieViewHolder.directorTextView.setText("Director: " + director);
        movieViewHolder.plotTextView.setText("Plot: " + plot);
        movieViewHolder.runTimeTextView.setText("Runtime: " + runtime);
        movieViewHolder.typeTextView.setText("Type: " + type);
        Picasso.get().load(posterUrl).fit().centerInside()
                .into(movieViewHolder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Add the method to filter the movie list based on a search query
    @SuppressLint("NotifyDataSetChanged")
    public void filterMovies(String query) {
        ArrayList<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : originalMovies) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.add(movie);
            }
        }

        movies.clear();
        movies.addAll(filteredMovies);
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;
        TextView titleTextView;
        TextView yearTextView;
        TextView directorTextView;
        TextView plotTextView;
        TextView runTimeTextView;
        TextView typeTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            directorTextView = itemView.findViewById(R.id.directorTextView);
            plotTextView = itemView.findViewById(R.id.plotTextView);
            runTimeTextView = itemView.findViewById(R.id.runtimeTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
