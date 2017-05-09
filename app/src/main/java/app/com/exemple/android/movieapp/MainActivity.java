package app.com.exemple.android.movieapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URL;
import java.util.List;

import app.com.exemple.android.movieapp.model.Movie;
import app.com.exemple.android.movieapp.model.ResponseMovieApi;
import app.com.exemple.android.movieapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements PosterAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;

    private ProgressBar mLoadJsonResultProgressBar;
    private TextView mErroMessageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mLoadJsonResultProgressBar = (ProgressBar) findViewById(R.id.pb_movie_load);
        mErroMessageTextView = (TextView) findViewById(R.id.tv_erro_message);

        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter(getApplicationContext(), this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mPosterAdapter);


        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        loadMovieData(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_filter, menu);
        return true;
    }

    private void showWeatherDataView(){
        mErroMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErroMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();

        if(menuItemThatWasSelected == R.id.action_sort_popularity){
            loadMovieData(0);
        }else if(menuItemThatWasSelected == R.id.action_sort_rate){
            loadMovieData(1);
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadMovieData(int sortby) {
        showWeatherDataView();
        new FetchMovieTask().execute(sortby);
    }

    @Override
    public void onClick(Movie movieSelected) {
        gotoMovieDetailActivity(movieSelected);
    }

    private void gotoMovieDetailActivity(Movie movieSelected) {

        Intent it = new Intent(this, MovieDetailActivity.class);
        it.putExtra(Intent.EXTRA_TEXT, movieSelected);

        startActivity(it);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class FetchMovieTask extends AsyncTask<Integer, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "Carregando dados....");
            mLoadJsonResultProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Integer... params) {

            int sortBy = 0;
            /* If there's no zip code, there's nothing to look up. */
            if (params.length != 0) {
                sortBy = params[0];
            }

//            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(sortBy);

            Log.i(TAG, weatherRequestUrl.toString());

            try {

                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                Log.i(TAG, jsonMovieResponse);

                Gson gson = new Gson();

                ResponseMovieApi responseMovieApi = gson.fromJson(jsonMovieResponse, ResponseMovieApi.class);

                return responseMovieApi.getResults();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {

            mLoadJsonResultProgressBar.setVisibility(View.INVISIBLE);

            if (movieData != null) {
                showWeatherDataView();
                mPosterAdapter.setMovieData(movieData);
                Log.i(TAG, "Terminado de carregar os dados");
            }else{
               showErrorMessage();
            }

        }
    }
}
