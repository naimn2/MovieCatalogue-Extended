package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.db.TvHelper;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.widgets.FavoriteMovieWidget;

import static android.provider.BaseColumns._ID;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.BACKDROP;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_TV;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.LANGUAGE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.OVERVIEW;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POPULARITY;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.POSTER;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.RELEASE_DATE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.TITLE;
import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.VOTE;

public class DetailTv extends AppCompatActivity {
    private TextView tvTitle, tvDesc, tvPopularity, tvRating, tvRelease, tvLanguage;
    private ImageView ivPoster, ivBackdrop;

    public static final String EXTRA_ITEM = "extraItem";
    private Item item;
    private TvHelper tvHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvTitle = findViewById(R.id.tv_title_detail_tv);
        tvDesc = findViewById(R.id.tv_description_detail_tv);
        tvPopularity = findViewById(R.id.tv_popularity_tv);
        tvRating = findViewById(R.id.tv_rating_tv);
        tvRelease = findViewById(R.id.tv_release_tv);
        tvLanguage = findViewById(R.id.tv_language_tv);
        ivPoster = findViewById(R.id.iv_poster_detail_tv);
        ivBackdrop = findViewById(R.id.iv_backdrop_tv);

        item = getIntent().getParcelableExtra(EXTRA_ITEM);

        tvHelper = tvHelper.getInstance(this);

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
            tvHelper.open();
            if (!tvHelper.isExist(this.item.getId())){
                item.setIcon(R.drawable.ic_favorite_on);
                addToFavorite();
            }
            else {
                item.setIcon(R.drawable.ic_favorite_off);
                removeFromFavorite();
            }
            tvHelper.close();
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

        Uri uri = getContentResolver().insert(CONTENT_URI_TV, args);
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
        tvHelper.open();
        if (tvHelper.isExist(item.getId())){
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.ic_favorite_on);
        }
        tvHelper.close();
        return super.onPrepareOptionsMenu(menu);
    }
}
