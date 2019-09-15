package com.muflihun.moviecatalogue5.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.models.Item;

import java.util.ArrayList;

public class StackWidgetService extends RemoteViewsService {
    private MovieHelper movieHelper;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        movieHelper = MovieHelper.getInstance(this.getApplicationContext());
        ArrayList<Item> listItem = new ArrayList<>();
        listItem.addAll(movieHelper.getAllMovies());
        return new StackRemoteViewsFactory(this.getApplicationContext(), listItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }
}
