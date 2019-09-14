package com.muflihun.moviecatalogue5.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_MOVIE = "movie";
    public static final String TABLE_TV = "tv";

    public static final class TableColumns implements BaseColumns{
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release";
        public static final String LANGUAGE = "language";
        public static final String POSTER = "poster";
        public static final String BACKDROP = "backdrop";
        public static final String VOTE = "vote";
        public static final String POPULARITY = "popularity";
    }
}
