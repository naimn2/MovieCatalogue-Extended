package com.muflihun.moviecatalogue5.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.widgets.FavoriteMovieWidget;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Item> listItem;
    private MovieHelper movieHelper;
    private final Context mContext;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
        listItem = new ArrayList<>();
        movieHelper = MovieHelper.getInstance(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        listItem.clear();
        listItem.addAll(movieHelper.getAllMovies());
    }

    @Override
    public void onDestroy() {
        movieHelper.close();
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
//        Uri uri = Uri.parse("https://image.tmdb.org/t/p/w185"+listItem.get(i).getPoster());
        Uri uri = Uri.parse("https://image.tmdb.org/").buildUpon()
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(listItem.get(i).getPoster())
                .build();
        rv.setImageViewUri(R.id.iv_poster_widget, uri);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.iv_poster_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
