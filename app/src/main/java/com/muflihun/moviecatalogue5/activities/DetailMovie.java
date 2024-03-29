package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.widgets.FavoriteMovieWidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.BACKDROP;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_MOVIE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.LANGUAGE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.OVERVIEW;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POPULARITY;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POSTER;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.RELEASE_DATE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.TITLE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.VOTE;

public class DetailMovie extends AppCompatActivity {
    public static final String EXTRA_ITEM = "extraItem";
    private Item item;
    private MovieHelper movieHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvTitle = findViewById(R.id.tv_title_detail);
        TextView tvDesc = findViewById(R.id.tv_description_detail);
        TextView tvPopularity = findViewById(R.id.tv_popularity);
        TextView tvRating = findViewById(R.id.tv_rating);
        TextView tvRelease = findViewById(R.id.tv_release);
        TextView tvLanguage = findViewById(R.id.tv_language);
        ImageView ivPoster = findViewById(R.id.iv_poster_detail);
        ImageView ivBackdrop = findViewById(R.id.iv_backdrop);

        item = getIntent().getParcelableExtra(EXTRA_ITEM);

        movieHelper = MovieHelper.getInstance(this);

        tvTitle.setText(item.getTitle());
        tvDesc.setText(item.getOverview());
        tvRating.setText(String.valueOf(item.getVote()));
        tvPopularity.setText(String.valueOf(item.getPopularity()));
        tvRelease.setText(item.getRelease());
        tvLanguage.setText(item.getLanguage());
        Glide.with(this).load("https://image.tmdb.org/t/p/w185/"+item.getPoster()).into(ivPoster);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+item.getBackdrop()).into(ivBackdrop);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        } else if (item.getItemId() == R.id.menu_favorite){
            movieHelper.open();
            if (!movieHelper.isExist(this.item.getId())){
                item.setIcon(R.drawable.ic_favorite_on);
                addToFavorite();
            }
            else {
                item.setIcon(R.drawable.ic_favorite_off);
                removeFromFavorite();
            }
            movieHelper.close();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, FavoriteMovieWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite(){
        ContentValues args = new ContentValues();
        args.put(_ID, item.getId());
        args.put(TITLE, item.getTitle());
        args.put(OVERVIEW, item.getOverview());
        args.put(RELEASE_DATE, item.getRelease());
        args.put(LANGUAGE, item.getLanguage());
        args.put(POPULARITY, item.getPopularity());
        args.put(VOTE, item.getVote());
        args.put(POSTER, item.getPoster());
        args.put(BACKDROP, item.getBackdrop());

        getContentResolver().insert(CONTENT_URI_MOVIE, args);
        Toast.makeText(this, getResources().getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite(){
        Uri uri = getIntent().getData();
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, getResources().getString(R.string.remove_favorite), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_movie_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        movieHelper.open();
        if (movieHelper.isExist(item.getId())){
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.ic_favorite_on);
        }
        movieHelper.close();
        return super.onPrepareOptionsMenu(menu);
    }
}
