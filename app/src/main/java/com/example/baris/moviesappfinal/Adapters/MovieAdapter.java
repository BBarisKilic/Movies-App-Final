package com.example.baris.moviesappfinal.Adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;

import com.example.baris.moviesappfinal.R;
import com.example.baris.moviesappfinal.Movie.Movie;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private ArrayList<Movie> movies;
    private Context context;
    private final MovieAdapterOnClickHandler movieClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick(Movie currentMovie);
    }

    public MovieAdapter (Context context, ArrayList<Movie> movies, MovieAdapterOnClickHandler movieClickHandler){
        this.context = context;
        this.movies = movies;
        this.movieClickHandler = movieClickHandler;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int movie_layout = R.layout.movie;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(movie_layout, parent, false);
        return new MovieHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(MovieHolder movieViewHolder, int pos) {
        context = movieViewHolder.movieImageView.getContext();
        Movie currentMovie = movies.get(pos);
        String voteString = String.valueOf(currentMovie.getAverage());
        movieViewHolder.tv_average.setText("Rating: "+ voteString + "/10");
        Picasso mPicasso = Picasso.with(context);
        mPicasso.load(Movie.posterPath(currentMovie))
                .placeholder(R.drawable.poster_holder)
                .error(R.drawable.poster_holder)
                .into(movieViewHolder.movieImageView);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.size();
    }

    public void setMovieDatabase(ArrayList<Movie> movieDatabase) {
        movies = movieDatabase;
        notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieImageView;
        public final TextView tv_average;

        public MovieHolder(View v) {
            super(v);
            tv_average = (TextView) v.findViewById(R.id.tv_average);
            movieImageView = (ImageView)v.findViewById(R.id.iv_moviePoster);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           int adapterPos = getAdapterPosition();
           Movie currentMovie = movies.get(adapterPos);
           movieClickHandler.onClick(currentMovie);
        }
    }

}
