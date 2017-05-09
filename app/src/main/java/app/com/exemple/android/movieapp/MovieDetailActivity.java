package app.com.exemple.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.com.exemple.android.movieapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView movieTittle;
    private TextView movieYear;
    private TextView movieDuration;
    private TextView movieOverview;
    private TextView movieAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView miniPoster  = (ImageView) findViewById(R.id.img_mini_poster);

        movieTittle   = (TextView) findViewById(R.id.tv_movie_title);
        movieYear     = (TextView) findViewById(R.id.tv_movie_year);
        movieDuration = (TextView) findViewById(R.id.tv_movie_duration);
        movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        movieAvg = (TextView) findViewById(R.id.tv_movie_average);

        Intent intent = getIntent();

        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

            String imgUrl = "https://image.tmdb.org/t/p/w500"+movie.getPoster_path();
            Log.i(TAG, imgUrl);

            Picasso.with(this).load(imgUrl).into(miniPoster);

            movieTittle.setText(movie.getOriginal_title());
            movieOverview.setText(movie.getOverview());
            movieYear.setText(movie.getRelease_date());
            movieAvg.setText(movie.getVote_average() + "/10");
        }
    }
}
