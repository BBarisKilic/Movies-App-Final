package com.example.baris.moviesappfinal;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;

import com.example.baris.moviesappfinal.Movie.Movie;
import com.example.baris.moviesappfinal.Models.Review;
import com.example.baris.moviesappfinal.Models.Video;
import com.example.baris.moviesappfinal.Adapters.RevAdapter;
import com.example.baris.moviesappfinal.Adapters.VidAdapter;
import com.example.baris.moviesappfinal.Database.FavContract.FavouritesEntry;
import com.example.baris.moviesappfinal.Database.FavDbHelper;
import com.example.baris.moviesappfinal.Utils.JSONParser;
import com.example.baris.moviesappfinal.Utils.API;
import com.example.baris.moviesappfinal.Utils.QueryUtils;

import static com.example.baris.moviesappfinal.Utils.API.review;
import static com.example.baris.moviesappfinal.Utils.API.video;


public class DetailActivity extends AppCompatActivity
        implements VidAdapter.VideoAdapterOnClickHandler,
        RevAdapter.ReviewAdapterOnClickHandler {

    private Toolbar toolbar;
    private ImageView backdropView;
    private ImageView posterView;
    private TextView titleView;
    private TextView overview;
    private TextView releaseDate;
    private TextView voteAverage;
    private ImageView arrow_right;
    private VidAdapter vidAdapter;
    private RevAdapter revAdapter;
    private RecyclerView vidRecyclerView;
    private RecyclerView revRecyclerView;
    int id;
    public Movie currentMovie;
    private int video_num;
    private Uri currentMovUri;
    private FavDbHelper dbHelper;
    private TextView downloadErrorMessageDisplay;
    private FloatingActionButton fabutton;
    private boolean is_it_favourite;
    private static final int QUERY_LOADER_VID = 20;
    private static final int QUERY_LOADER_REV = 30;
    private static final int QUERY_LOADER_FAV = 40;
    public static final String MOVIE_ID = "movie_id";
    public static final String VID_REVIEW_SORT = "vid_review_sort";
    private static final String TAG = DetailActivity.class.getSimpleName();

    ArrayList<Video> videos;
    ArrayList<Review> reviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.tb_detailsToolbar);
        setSupportActionBar(toolbar);

        posterView = (ImageView) findViewById(R.id.iv_poster);
        backdropView = (ImageView) findViewById(R.id.iv_backdrop);
        titleView = (TextView) findViewById(R.id.tv_originalTitle);
        overview = (TextView) findViewById(R.id.overview);
        releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
        voteAverage = (TextView) findViewById(R.id.voteAverage);
        arrow_right = (ImageView) findViewById(R.id.iv_arrowRight);

        final Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                currentMovie = intentThatStartedThisActivity.getParcelableExtra("movie");
                Picasso backdropPicasso = Picasso.with(DetailActivity.this);
                backdropPicasso.load(Movie.backdropPath(currentMovie))
                        .placeholder(R.drawable.backdrop_holder)
                        .error(R.drawable.backdrop_holder)
                        .into(backdropView);
                titleView.setText(currentMovie.getOrgTitle());
                overview.setText(currentMovie.getOverview());
                releaseDate.setText(currentMovie.getReleaseDate());
                voteAverage.setText(String.valueOf(currentMovie.getAverage()));
                setTitle(currentMovie.getOrgTitle());

                Picasso posterPicasso = Picasso.with(DetailActivity.this);
                posterPicasso.load(Movie.posterPath(currentMovie))
                        .placeholder(R.drawable.poster_holder)
                        .error(R.drawable.poster_holder)
                        .into(posterView);
            }
        }


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        revRecyclerView = (RecyclerView) findViewById(R.id.rv_review);
        revRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        revRecyclerView.setHasFixedSize(true);
        revAdapter = new RevAdapter(this, reviews, this);
        revRecyclerView.setAdapter(revAdapter);

        vidRecyclerView = (RecyclerView) findViewById(R.id.rv_video);
        vidRecyclerView.setLayoutManager(new LinearLayoutManager
                (DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        vidRecyclerView.setHasFixedSize(true);
        vidAdapter = new VidAdapter(DetailActivity.this, videos, this);
        vidRecyclerView.setAdapter(vidAdapter);

        String movId = String.valueOf(currentMovie.getId());

        Bundle revBundle = new Bundle();
        revBundle.putString(MOVIE_ID, movId);
        revBundle.putString(VID_REVIEW_SORT, review);
        getSupportLoaderManager().initLoader(QUERY_LOADER_REV, revBundle, RevLoaderListener);

        Bundle vidBundle = new Bundle();
        vidBundle.putString(MOVIE_ID, movId);
        vidBundle.putString(VID_REVIEW_SORT, video);
        getSupportLoaderManager().initLoader(QUERY_LOADER_VID, vidBundle, VidLoaderListener);

        dbHelper = new FavDbHelper(this);
        fabutton = (FloatingActionButton) findViewById(R.id.favourite);

        getSupportLoaderManager()
                .restartLoader(QUERY_LOADER_FAV, null, isMovieFavCursorLoader);
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_it_favourite) {
                    remove_from_favourite();
                    fabutton.setImageResource(R.drawable.not_favourite);
                } else {
                    add_to_favourite();
                    fabutton.setImageResource(R.drawable.favourite);
                }
            }
        });
    }

    @Override
    public void onClickVideo(Video currentVideo) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Video.buildYouTubePath(currentVideo));
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onClickReview(Review currentReview) {
    }

    private void add_to_favourite()
    {
        ContentValues values = new ContentValues();
        values.put(FavouritesEntry.COLUMN_ID, currentMovie.getId());
        values.put(FavouritesEntry.COLUMN_POSTER, currentMovie.getImg());
        values.put(FavouritesEntry.COLUMN_BACKDROP, currentMovie.getBackdrop());
        values.put(FavouritesEntry.COLUMN_TITLE, currentMovie.getOrgTitle());
        values.put(FavouritesEntry.COLUMN_OVERVIEW, currentMovie.getOverview());
        values.put(FavouritesEntry.COLUMN_RELEASE_DATE, currentMovie.getReleaseDate());
        values.put(FavouritesEntry.COLUMN_AVERAGE, currentMovie.getAverage());
        try {
            currentMovUri = getContentResolver().insert(FavouritesEntry.CONTENT_URI, values);
            is_it_favourite = true;
            Toast.makeText(DetailActivity.this,currentMovie.getOrgTitle() + " is added to your Favourites List.", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            currentMovUri = null;
        }
    }

    private void remove_from_favourite() {
        currentMovUri = FavouritesEntry.CONTENT_URI;
        String selection = FavouritesEntry.COLUMN_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(currentMovie.getId())};
        if (currentMovUri != null) {
            int moviesDeleted = getContentResolver().delete(currentMovUri, selection, selectionArgs);
            if (moviesDeleted != 0) {
                is_it_favourite = false;
                Toast.makeText(DetailActivity.this, currentMovie.getOrgTitle() + " is removed from your Favourites List.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks VidLoaderListener =
            new LoaderManager.LoaderCallbacks<ArrayList<Video>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public Loader<ArrayList<Video>> onCreateLoader(int id, final Bundle args) {
                    return (Loader<ArrayList<Video>>)
                            new AsyncTaskLoader<ArrayList<Video>>(DetailActivity.this) {

                        ArrayList<Video> videoDatabase;
                        Bundle mArgs = args;

                        @Override
                        protected void onStartLoading() {
                            if (videoDatabase != null) {
                                deliverResult(videoDatabase);
                            } else {
                                forceLoad();
                            }
                        }

                        @Override
                        public ArrayList<Video> loadInBackground() {
                            if (mArgs == null) {
                                return null;
                            }

                            String vidReviewSort = mArgs.getString(VID_REVIEW_SORT);
                            String movieId = mArgs.getString(MOVIE_ID);

                            URL vidRequestUrl;
                            if ((movieId != null && movieId.equals(""))
                                    || (vidReviewSort != null && vidReviewSort.equals(""))) {
                                return null;
                            }
                            try {
                                vidRequestUrl = API.buildVidRevUrl(movieId, vidReviewSort);
                                String jsonResponse = QueryUtils.httpConnect(vidRequestUrl);
                                return JSONParser.parseVidJson(jsonResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<Video>> loader) {
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<Video>> loader, ArrayList<Video> videos) {
                    if (videos != null) {
                        vidAdapter.setVideoDatabase(videos);

                        video_num = (int) getResources().getInteger(R.integer.num_of_videos);
                        if (videos.size() > video_num) {
                            arrow_right.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Problem showing videos");
                    }
                }
            };

    private LoaderManager.LoaderCallbacks RevLoaderListener =
            new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
                    return (Loader<ArrayList<Review>>)
                            new AsyncTaskLoader<ArrayList<Review>>(DetailActivity.this) {

                        Bundle mArgs = args;
                        ArrayList<Review> reviewDatabase;

                        @Override
                        protected void onStartLoading() {
                            if (reviewDatabase != null) {
                                deliverResult(reviewDatabase);
                            } else {
                                forceLoad();
                            }
                        }
                        @Override
                        public ArrayList<Review> loadInBackground() {
                            if (mArgs == null) {
                                return null;
                            }
                            String movieId = mArgs.getString(MOVIE_ID);
                            String vidReviewSort = mArgs.getString(VID_REVIEW_SORT);
                            URL reviewRequestUrl;
                            if ((movieId != null && movieId.equals(""))
                                    || (vidReviewSort != null && vidReviewSort.equals(""))) {
                                return null;
                            }
                            try {
                                reviewRequestUrl = API.buildVidRevUrl(movieId, vidReviewSort);
                                String jsonResponse = QueryUtils.httpConnect(reviewRequestUrl);
                                return JSONParser.parseRevJson(jsonResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<Review>> loader) {
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {
                    if (reviews != null) {
                        revAdapter.setReviewDatabase(reviews);
                    } else {
                        Log.e(TAG, "Problem showing reviews");
                    }
                }
            };

    private LoaderManager.LoaderCallbacks isMovieFavCursorLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
                    switch (loaderId) {
                        case QUERY_LOADER_FAV:
                            String[] projection = {
                                    FavouritesEntry.COLUMN_ID
                            };
                            String selection = FavouritesEntry.COLUMN_ID + " = ? ";
                            String[] selectionArgs = {String.valueOf(currentMovie.getId())};

                            return new CursorLoader(DetailActivity.this,
                                    FavouritesEntry.CONTENT_URI,
                                    projection,
                                    selection,
                                    selectionArgs,
                                    null);
                        default:
                            throw new RuntimeException("Not implemented loader: " + loaderId);
                    }
                }

                @Override
                public void onLoadFinished(Loader loader, Cursor cursorData) {
                    try {
                        if (cursorData.getCount() < 1) {
                            is_it_favourite = false;
                            fabutton.setImageResource(R.drawable.not_favourite);
                            cursorData.close();
                        } else {
                            is_it_favourite = true;
                            fabutton.setImageResource((R.drawable.favourite));
                        }
                    }
                    catch(Exception e) {
                        Log.e(TAG, "Returns No Data");
                    }
                }

                @Override
                public void onLoaderReset(Loader loader) {
                }
            };
}
