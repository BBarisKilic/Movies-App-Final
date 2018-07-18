package com.example.baris.moviesappfinal.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baris.moviesappfinal.Database.FavContract.FavouritesEntry;

public class FavDbHelper extends SQLiteOpenHelper {

    private static final String NAME_OF_DATABASE = "favourites.db";
    private static final int VER_OF_DATABASE = 1;

    public FavDbHelper(Context context) {
        super(context, NAME_OF_DATABASE, null, VER_OF_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouritesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                FavouritesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_AVERAGE + " DOUBLE NOT NULL, " +

                " UNIQUE (" + FavContract.FavouritesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + FavContract.FavouritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
