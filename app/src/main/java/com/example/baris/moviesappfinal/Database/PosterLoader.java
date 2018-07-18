package com.example.baris.moviesappfinal.Database;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.example.baris.moviesappfinal.Movie.Movie;
import com.example.baris.moviesappfinal.Utils.JSONParser;
import com.example.baris.moviesappfinal.Utils.API;
import com.example.baris.moviesappfinal.Utils.QueryUtils;
import static com.example.baris.moviesappfinal.MainActivity.POPULAR;
import static com.example.baris.moviesappfinal.MainActivity.TOP_RATED;


public class PosterLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    Bundle args;
    ArrayList<Movie> movieDatabase;
    public static final int QUERY_LOADER = 10;
    public static final String QUERY_SORT = "query_sort";

    public PosterLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (movieDatabase != null) {
            deliverResult(movieDatabase);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<Movie> loadInBackground() {

        String movieSort = args.getString(QUERY_SORT);
        if (movieSort != null && movieSort.equals("")) {
            return null;
        }
        URL movieRequestUrl = API.buildMovUrl(movieSort);

        try {
            switch (movieSort) {
                case TOP_RATED:
                    movieRequestUrl = API.buildMovUrl(TOP_RATED);
                    break;
                case POPULAR:
                    movieRequestUrl = API.buildMovUrl(POPULAR);
                    break;
                default:
                    throw new UnsupportedOperationException("Url problem: " + movieRequestUrl);
            }
            String JSONResponse = QueryUtils.httpConnect(movieRequestUrl);
            return JSONParser.parseMovJson(JSONResponse);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
