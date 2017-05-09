package app.com.exemple.android.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.com.exemple.android.movieapp.model.Movie;

/**
 * Created by macroot on 07/04/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder>{

    private List<Movie> mMovieData;
    private Context context = null;

    private final MovieAdapterOnClickHandler mClickHandler;


    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie weatherForDay);
    }

    public PosterAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }


    @Override
    public PosterAdapter.PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapter.PosterAdapterViewHolder posterAdapterViewHolder, int position) {
        Movie movie = mMovieData.get(position);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(posterAdapterViewHolder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImageView;

        public PosterAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster_movie);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieSelected = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieSelected);
        }

    }

    public void setMovieData(List<Movie> movieData){

        this.mMovieData = movieData;
        notifyDataSetChanged();
    }

}
