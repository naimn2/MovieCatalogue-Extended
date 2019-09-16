package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.db.DatabaseContract;
import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.widgets.FavoriteMovieWidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailMovie extends AppCompatActivity {
    private TextView tvTitle, tvDesc, tvPopularity, tvRating, tvRelease, tvLanguage;
    private ImageView ivPoster, ivBackdrop;

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

        tvTitle = findViewById(R.id.tv_title_detail);
        tvDesc = findViewById(R.id.tv_description_detail);
        tvPopularity = findViewById(R.id.tv_popularity);
        tvRating = findViewById(R.id.tv_rating);
        tvRelease = findViewById(R.id.tv_release);
        tvLanguage = findViewById(R.id.tv_language);
        ivPoster = findViewById(R.id.iv_poster_detail);
        ivBackdrop = findViewById(R.id.iv_backdrop);

        item = getIntent().getParcelableExtra(EXTRA_ITEM);

        movieHelper = MovieHelper.getInstance(this);
        movieHelper.open();

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
            if (!movieHelper.isExist(this.item.getId())){
                item.setIcon(R.drawable.ic_favorite_on);
                addToFavorite();
            }
            else {
                item.setIcon(R.drawable.ic_favorite_off);
                removeFromFavorite();
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, FavoriteMovieWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite(){
        long result = movieHelper.insert(this.item);
        if (result>0)
            Toast.makeText(this, getResources().getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getResources().getString(R.string.fail_favorite), Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite(){
        int result = movieHelper.delete(item.getId());
        if (result > 0){
            Toast.makeText(this, getResources().getString(R.string.remove_favorite), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.fail_remove), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_movie_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (movieHelper.isExist(item.getId())){
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.ic_favorite_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }
}
