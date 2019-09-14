package com.muflihun.moviecatalogue5.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseTvHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbtv";
    private static final int DATABASE_VERSION = 3;
    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s REAL NOT NULL," +
                    " %s REAL NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_TV,
            DatabaseContract.TableColumns._ID,
            DatabaseContract.TableColumns.TITLE,
            DatabaseContract.TableColumns.OVERVIEW,
            DatabaseContract.TableColumns.RELEASE_DATE,
            DatabaseContract.TableColumns.LANGUAGE,
            DatabaseContract.TableColumns.POPULARITY,
            DatabaseContract.TableColumns.VOTE,
            DatabaseContract.TableColumns.POSTER,
            DatabaseContract.TableColumns.BACKDROP
    );

    public DatabaseTvHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TV);
        onCreate(db);
    }
}
