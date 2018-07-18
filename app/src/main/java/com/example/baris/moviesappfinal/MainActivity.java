package com.example.baris.moviesappfinal;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.baris.moviesappfinal.Adapters.FavAdapter;
import com.example.baris.moviesappfinal.Adapters.MovieAdapter;
import com.example.baris.moviesappfinal.Database.FavContract;
import com.example.baris.moviesappfinal.Database.PosterLoader;
import com.example.baris.moviesappfinal.Movie.Movie;
import static com.example.baris.moviesappfinal.Database.PosterLoader.QUERY_LOADER;
import static com.example.baris.moviesappfinal.Database.PosterLoader.QUERY_SORT;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        FavAdapter.FavouriteAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>,
        NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private TextView connectionErrorMessageDisplay;
    private int columns;
    ArrayList<Movie> movies;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_DISPLAY = "display selected";
    private static final String DISPLAY_LOADED = "display loaded";
    private static final String LAYOUT_MANAGER_STATE = "layout manager state";
    private Context context;
    private FavAdapter favAdapter;
    private int displaySelected;
    private String loadedDisplay;
    private GridLayoutManager gridLayoutManager;
    private Parcelable savedRecyclerLayoutState;
    public static final int FAVOURITES_LOADER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout)
                findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.navigateView);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            setTitle(R.string.my_app_title);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        columns = (int) getResources().getInteger(R.integer.num_of_columns);
        gridLayoutManager = new GridLayoutManager(this, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this, movies, this);
        favAdapter = new FavAdapter(this, this);
        recyclerView.setAdapter(movieAdapter);
        connectionErrorMessageDisplay = (TextView) findViewById(R.id.tv_connectionProblem);

        Bundle popBundle = new Bundle();
        popBundle.putString(QUERY_SORT, POPULAR);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> movPosterLoader = loaderManager.getLoader(QUERY_LOADER);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DISPLAY) && savedInstanceState.containsKey(DISPLAY_LOADED)) {
                displaySelected = savedInstanceState.getInt(STATE_DISPLAY);
                loadedDisplay = savedInstanceState.getString(DISPLAY_LOADED);
            }
        } else {
            displaySelected = R.id.menuPopular;
            loadedDisplay = POPULAR;
        }
        loadMovDatabase(loadedDisplay);
    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new PosterLoader(MainActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        if (movies != null) {
            movieAdapter.setMovieDatabase(movies);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        } else {
            showConnectErrorMessage();
            Log.i(TAG, "Problem showing movies");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
    }

    @Override
    public void onClick(Movie clickedMovie) {
        context = this;
        Class finalClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, finalClass);
        intentToStartDetailActivity.putExtra("movie", clickedMovie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        context = MainActivity.this;
        displaySelected = item.getItemId();
        switch (displaySelected) {
            case R.id.menuPopular:
                drawer.closeDrawer(GravityCompat.START);
                loadedDisplay = POPULAR;
                loadMovDatabase(loadedDisplay);
                return true;
            case R.id.menuTopRated:
                drawer.closeDrawer(GravityCompat.START);
                loadedDisplay = TOP_RATED;
                loadMovDatabase(loadedDisplay);
                return true;
            case R.id.menuFavourite:
                drawer.closeDrawer(GravityCompat.START);
                loadedDisplay = "favourite";
                loadMovDatabase(loadedDisplay);
                return true;
            default:
                return false;
        }
    }

    private void loadMovDatabase(String sortMode) {
        showMovDataView();
        if (sortMode!= null && (sortMode.equals(POPULAR) || sortMode.equals(TOP_RATED))) {
            Bundle sortModeBundle = new Bundle();
            sortModeBundle.putString(QUERY_SORT, sortMode);
            getSupportLoaderManager().restartLoader(QUERY_LOADER, sortModeBundle, MainActivity.this);

            recyclerView.setAdapter(movieAdapter);
            if (sortMode.equals(POPULAR)) {
                setTitle(R.string.popular_movies);
            } else {
                setTitle(R.string.top_rated_movies);
            }
        } else {
            getSupportLoaderManager().restartLoader(FAVOURITES_LOADER, null, FavouritesCursorLoader);
            recyclerView.setAdapter(favAdapter);
            setTitle(R.string.favourite_movies);
        }
    }

    private void showMovDataView() {
        connectionErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showConnectErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        connectionErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    protected void onSaveInstanceState(Bundle selectedState) {
        super.onSaveInstanceState(selectedState);

        selectedState.putInt(STATE_DISPLAY, displaySelected);
        selectedState.putString(DISPLAY_LOADED, loadedDisplay);

        selectedState.putParcelable(LAYOUT_MANAGER_STATE, gridLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    private LoaderManager.LoaderCallbacks FavouritesCursorLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
                    switch (loaderId) {
                        case FAVOURITES_LOADER:
                            String[] projection = {
                                    FavContract.FavouritesEntry.COLUMN_ID,
                                    FavContract.FavouritesEntry.COLUMN_POSTER,
                                    FavContract.FavouritesEntry.COLUMN_BACKDROP,
                                    FavContract.FavouritesEntry.COLUMN_TITLE,
                                    FavContract.FavouritesEntry.COLUMN_OVERVIEW,
                                    FavContract.FavouritesEntry.COLUMN_RELEASE_DATE,
                                    FavContract.FavouritesEntry.COLUMN_AVERAGE
                            };
                            return new CursorLoader(MainActivity.this,
                                    FavContract.FavouritesEntry.CONTENT_URI,
                                    projection,
                                    null,
                                    null,
                                    null);
                        default:
                            throw new RuntimeException("Not implemented loader: " + loaderId);
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    favAdapter.changeCursor(data);
                    recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                }

                @Override
                public void onLoaderReset(Loader loader) {
                    favAdapter.changeCursor(null);
                }
            };
}

