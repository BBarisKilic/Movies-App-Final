package com.example.baris.moviesappfinal.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavContract {


    private FavContract() {
    }

    public static final String FAV_PATH = "favourites";
    public static final String CONTENT_AUTHORITY = "com.example.baris.moviesappfinal";
    public static final Uri DATABASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class FavouritesEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(DATABASE_CONTENT_URI, FAV_PATH);
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_POSTER = "moviePoster";
        public static final String COLUMN_BACKDROP = "movieBackdrop";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_OVERVIEW = "movieSynopsis";
        public static final String COLUMN_AVERAGE = "movieRating";
    }
}
