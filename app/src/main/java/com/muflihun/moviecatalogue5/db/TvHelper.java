package com.muflihun.moviecatalogue5.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muflihun.moviecatalogue5.models.Item;

import java.util.ArrayList;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns._ID;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TABLE_TV;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.BACKDROP;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.LANGUAGE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.OVERVIEW;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POPULARITY;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POSTER;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.RELEASE_DATE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.TITLE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.VOTE;

public class TvHelper {
    private static final String DATABASE_TABLE = TABLE_TV;
    private static DatabaseHelper databaseHelper;
    private static TvHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

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
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Item tv;
        if (cursor.getCount() > 0) {
            do {
                tv = new Item();
                tv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tv.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tv.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                tv.setRelease(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                tv.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                tv.setVote(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));
                tv.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                tv.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                tv.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));

                arrayList.add(tv);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Item tv) {
        ContentValues args = new ContentValues();
        args.put(_ID, tv.getId());
        args.put(TITLE, tv.getTitle());
        args.put(OVERVIEW, tv.getOverview());
        args.put(RELEASE_DATE, tv.getRelease());
        args.put(LANGUAGE, tv.getLanguage());
        args.put(POPULARITY, tv.getPopularity());
        args.put(VOTE, tv.getVote());
        args.put(POSTER, tv.getPoster());
        args.put(BACKDROP, tv.getBackdrop());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public boolean isExist(int id){
        String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+_ID+" =?";
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
