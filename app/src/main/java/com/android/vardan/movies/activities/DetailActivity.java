package com.android.vardan.movies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vardan.movies.R;
import com.android.vardan.movies.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView textViewTitle = findViewById(R.id.titleTextView);
        TextView textViewYear = findViewById(R.id.yearTextView);
        TextView textViewDirector = findViewById(R.id.directorTextView);
        TextView textViewPlot = findViewById(R.id.plotTextView);
        TextView textViewRuntime = findViewById(R.id.runTimeTextView);
        TextView textViewType = findViewById(R.id.typeTextView);
        ImageView imageViewPoster = findViewById(R.id.posterImageView);

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.KEY_TITLE);
        String posterUrl = intent.getStringExtra(Constants.KEY_POSTER_URL);
        String year = intent.getStringExtra(Constants.KEY_YEAR);
        String director = intent.getStringExtra(Constants.KEY_DIRECTOR);
        String plot = intent.getStringExtra(Constants.KEY_PLOT);
        String runtime = intent.getStringExtra(Constants.KEY_RUNTIME);
        String type = intent.getStringExtra(Constants.KEY_TYPE);
        textViewTitle.setText(title);
        textViewYear.setText("Year: " + year);
        textViewDirector.setText("Director: " + director);
        textViewPlot.setText("Plot: " + plot);
        textViewRuntime.setText("Runtime: " + runtime);
        textViewType.setText("Type: " + type);
        Picasso.get().load(posterUrl).fit().centerInside()
                .into(imageViewPoster);
    }
}
