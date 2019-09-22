package com.muflihun.moviecatalogue5.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.muflihun.moviecatalogue5.models.Item;

import java.util.ArrayList;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.TABLE_MOVIE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.BACKDROP;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns._ID;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.LANGUAGE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.OVERVIEW;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POPULARITY;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POSTER;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.RELEASE_DATE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.TITLE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.VOTE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<Item> getAllMovies() {
        ArrayList<Item> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                BaseColumns._ID + " ASC",
                null);
        cursor.moveToFirst();
        Item movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Item();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setRelease(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                movie.setVote(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Item movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RELEASE_DATE, movie.getRelease());
        args.put(LANGUAGE, movie.getLanguage());
        args.put(POPULARITY, movie.getPopularity());
        args.put(VOTE, movie.getVote());
        args.put(POSTER, movie.getPoster());
        args.put(BACKDROP, movie.getBackdrop());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public boolean isExist(int id){
        String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+ BaseColumns._ID+" =?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        boolean exist = false;
        if (cursor.moveToFirst()){
            exist = true;
        }
        cursor.close();
        return exist;
    }

    public int delete(int id) {
        return database.delete(DATABASE_TABLE, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
