package com.example.mini_projet_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.QuickViewConstants;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class FavoriteQuotesDbOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    private static final String SQL_CREATE_FAVORITE_QUOTES = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY," +
                    "%s TEXT," +
                    "%s TEXT)",
            FavoriteQuotesContract.info.TABLE_NAME,
            FavoriteQuotesContract.info.COLUMN_NAME_ID,
            FavoriteQuotesContract.info.COLUMN_NAME_QUOTE,
            FavoriteQuotesContract.info.COLUMN_NAME_AUTHOR);
    private static final String SQL_DELETE_FAVORITEQUOTE = String.format("DROP TABLE IS EXISTS %s",
            FavoriteQuotesContract.info.TABLE_NAME);

    public FavoriteQuotesDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_FAVORITEQUOTE);
        onCreate(sqLiteDatabase);
    }

    public void add(int id, String quote, String author) {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesContract.info.COLUMN_NAME_ID, id);
        values.put(FavoriteQuotesContract.info.COLUMN_NAME_QUOTE, quote);
        values.put(FavoriteQuotesContract.info.COLUMN_NAME_AUTHOR, author);

        db.insert(FavoriteQuotesContract.info.TABLE_NAME, null, values);
    }

    public void getAll() {
        SQLiteDatabase db = FavoriteQuotesDbOpenHelper.this.getReadableDatabase();

        String[] projection = {
                FavoriteQuotesContract.info.COLUMN_NAME_ID,
                FavoriteQuotesContract.info.COLUMN_NAME_QUOTE,
                FavoriteQuotesContract.info.COLUMN_NAME_AUTHOR
        };

        Cursor cursor = db.query(
                FavoriteQuotesContract.info.TABLE_NAME,   // The table to query
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavoriteQuotesContract.info.COLUMN_NAME_ID));
            String quote = cursor.getString(
                    cursor.getColumnIndexOrThrow(FavoriteQuotesContract.info.COLUMN_NAME_QUOTE));
            String author = cursor.getString(
                    cursor.getColumnIndexOrThrow(FavoriteQuotesContract.info.COLUMN_NAME_AUTHOR));
            Log.e("SQLite", String.format("%d %s %s", id, quote, author));
        }
        cursor.close();
    }
}
