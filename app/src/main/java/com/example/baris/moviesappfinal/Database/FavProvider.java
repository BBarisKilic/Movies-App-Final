package com.example.baris.moviesappfinal.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.content.UriMatcher;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.net.Uri;

public class FavProvider extends ContentProvider{

    private static final UriMatcher urimatcher = buildUriMatcher();
    private FavDbHelper dbHelper;
    private static final int FAV = 10;
    private static final int FAV_ID = 11;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavContract.CONTENT_AUTHORITY,
                FavContract.FAV_PATH, FAV);
        uriMatcher.addURI(FavContract.CONTENT_AUTHORITY,
                FavContract.FAV_PATH + "/#", FAV_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        int match = urimatcher.match(uri);

        Cursor retCursor;
        switch (match) {
            case FAV:
                retCursor = database.query(FavContract.FavouritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = urimatcher.match(uri);
        Uri returnUri;

        switch(match) {
            case FAV:
                long id = sqLiteDatabase.insert(FavContract.FavouritesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavContract.FavouritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = urimatcher.match(uri);
        int moviesDeleted;

        switch(match) {
            case FAV:
                moviesDeleted = sqLiteDatabase.delete(FavContract.FavouritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case FAV_ID:
                selection = FavContract.FavouritesEntry._ID + " = ? ";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                moviesDeleted = sqLiteDatabase.delete(FavContract.FavouritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete: " + uri);
        }
        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
