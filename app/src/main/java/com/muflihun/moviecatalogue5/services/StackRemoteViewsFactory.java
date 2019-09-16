package com.muflihun.moviecatalogue5.services;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.db.TvHelper;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.widgets.FavoriteMovieWidget;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Item> listItem;
    private final Context mContext;
    public static final String TAG = "StackRemoteViewsFactory";

    StackRemoteViewsFactory(Context context) {
        mContext = context;
        listItem = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        MovieHelper movieHelper = MovieHelper.getInstance(mContext);
        TvHelper tvHelper = TvHelper.getInstance(mContext);
        movieHelper.open();
        tvHelper.open();
        listItem.clear();
        listItem.addAll(movieHelper.getAllMovies());
        listItem.addAll(tvHelper.getAllMovies());
        Log.d(TAG, "" + listItem.size());
        movieHelper.close();
        tvHelper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        InputStream inputStream = null;
        try {
            inputStream = new URL("https://image.tmdb.org/t/p/w185"+listItem.get(i).getBackdrop()).openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rv.setImageViewBitmap(R.id.iv_poster_widget, BitmapFactory.decodeStream(inputStream));
        rv.setTextViewText(R.id.tv_title_widget, listItem.get(i).getTitle());

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
