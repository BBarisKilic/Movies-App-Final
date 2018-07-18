package com.example.baris.moviesappfinal.Adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baris.moviesappfinal.R;
import com.example.baris.moviesappfinal.Database.FavContract;
import com.example.baris.moviesappfinal.Movie.Movie;
import com.squareup.picasso.Picasso;


public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavouriteHolder> {

    private Cursor cursor;
    private Context context;
    private final FavouriteAdapterOnClickHandler favouriteClickHandler;

    public FavAdapter(Context context, FavouriteAdapterOnClickHandler favouriteClickHandler)
    {
        this.context = context;
        this.favouriteClickHandler = favouriteClickHandler;
    }

    @Override
    public void onBindViewHolder(FavouriteHolder favouriteViewHolder, int pos) {
        cursor.moveToPosition(pos);
        String voteString = cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_AVERAGE));
        favouriteViewHolder.tv_average.setText("Rating: "+ voteString + "/10");
        Picasso picasso = Picasso.with(context);
        picasso.load(Movie.POSTER_PATH
                + cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_POSTER)))
                .placeholder(R.drawable.poster_holder)
                .error(R.drawable.poster_holder)
                .into(favouriteViewHolder.movieImageView);
    }

    @Override
    public FavouriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForFavouriteItem = R.layout.movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mItemView = inflater.inflate(layoutIdForFavouriteItem, parent, false);
        return new FavouriteHolder(mItemView);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public void changeCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public interface FavouriteAdapterOnClickHandler {
        void onClick(Movie currentFavouriteMovie);
    }


    public class FavouriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieImageView;
        public final TextView tv_average;

        public FavouriteHolder(View v) {
            super(v);
            movieImageView = (ImageView) v.findViewById(R.id.iv_moviePoster);
            tv_average = (TextView) v.findViewById(R.id.tv_average);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            cursor.moveToPosition(adapterPos);
            favouriteClickHandler.onClick(queryFav());
        }

        private Movie queryFav() {
            Movie currentFavourite = new Movie();
            currentFavourite.setId(cursor.getInt(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_ID)));
            currentFavourite.setOrgTitle(cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_TITLE)));
            currentFavourite.setImg(cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_POSTER)));
            currentFavourite.setBackdrop(cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_BACKDROP)));
            currentFavourite.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_RELEASE_DATE)));
            currentFavourite.setOverview(cursor.getString(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_OVERVIEW)));
            currentFavourite.setAverage(cursor.getDouble(cursor.getColumnIndex(FavContract.FavouritesEntry.COLUMN_AVERAGE)));
            return currentFavourite;
        }
    }
}