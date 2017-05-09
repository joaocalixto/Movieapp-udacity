package app.com.exemple.android.movieapp.model;

import java.util.List;

/**
 * Created by macroot on 17/04/17.
 */

public class ResponseMovieApi {

    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
