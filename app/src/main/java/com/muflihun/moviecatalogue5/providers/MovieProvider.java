package com.muflihun.moviecatalogue5.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.db.TvHelper;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.AUTHORITY;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TABLE_MOVIE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TABLE_TV;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_MOVIE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_TV;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV = 3;
    private static final int TV_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper movieHelper;
    private TvHelper tvHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV + "/#", TV_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        tvHelper = TvHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor = null;
        if (uri.equals(CONTENT_URI_MOVIE)) {
            movieHelper.open();
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    cursor = movieHelper.queryProvider();
                    break;
                case MOVIE_ID:
                    cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                    break;
                default:
                    cursor = null;
                    break;
            }
        } else {
            tvHelper.open();
            switch (sUriMatcher.match(uri)) {
                case TV:
                    cursor = tvHelper.queryProvider();
                    break;
                case TV_ID:
                    cursor = tvHelper.queryByIdProvider(uri.getLastPathSegment());
                    break;
                default:
                    cursor = null;
                    break;
            }
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uri.equals(CONTENT_URI_MOVIE)) {
            movieHelper.open();
            long added;
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    added = movieHelper.insertProvider(contentValues);
                    break;
                default:
                    added = 0;
                    break;
            }

            getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
            return Uri.parse(CONTENT_URI_MOVIE + "/" + added);
        } else {
            tvHelper.open();
            long added;
            switch (sUriMatcher.match(uri)) {
                case TV:
                    added = tvHelper.insertProvider(contentValues);
                    break;
                default:
                    added = 0;
                    break;
            }

            getContext().getContentResolver().notifyChange(CONTENT_URI_TV, null);
            return Uri.parse(CONTENT_URI_TV + "/" + added);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        if (uri.toString().contains(CONTENT_URI_MOVIE.toString())) {
            movieHelper.open();
            int deleted;
            switch (sUriMatcher.match(uri)) {
                case MOVIE_ID:
                    deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                    break;
                default:
                    deleted = 0;
                    break;
            }

            getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
            return deleted;
        } else {
            tvHelper.open();
            int deleted;
            switch (sUriMatcher.match(uri)) {
                case TV_ID:
                    deleted = tvHelper.deleteProvider(uri.getLastPathSegment());
                    break;
                default:
                    deleted = 0;
                    break;
            }

            getContext().getContentResolver().notifyChange(CONTENT_URI_TV, null);
            return deleted;
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
