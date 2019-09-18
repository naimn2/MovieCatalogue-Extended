package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.broadcasts.AlarmReceiver;
import com.muflihun.moviecatalogue5.fragments.FavoriteFragment;
import com.muflihun.moviecatalogue5.fragments.MovieFragment;
import com.muflihun.moviecatalogue5.fragments.TVShowFragment;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private MaterialSearchView msv;
    private AlarmReceiver alarmReceiver;

    public static final String STATE_ID_MENU_ITEM_BOTTOM_NAV = "idMenuItem";
    public static final String DAILY_REMINDER_TIME = "07:00";
    public static final String DAILY_REMINDER_MESSAGE = "BALIK LAGI DONG :)";
    public static final String RELEASE_REMINDER_TIME = "08:00";
    public static final String RELEASE_REMINDER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=%s&primary_release_date.gte=%s&primary_release_date.lte=%s";

    Observer<ArrayList<Item>> observer = new Observer<ArrayList<Item>>() {
        @Override
        public void onChanged(ArrayList<Item> items) {
            alarmReceiver.setUpReleaseAlarm(getApplicationContext(), RELEASE_REMINDER_TIME, items);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        bottomNav = findViewById(R.id.bottom_nav);
        msv = findViewById(R.id.msv_main);
        msv.setHint(getResources().getString(R.string.search));

        alarmReceiver = new AlarmReceiver();
        alarmReceiver.setUpAlarm(this, DAILY_REMINDER_TIME, DAILY_REMINDER_MESSAGE, AlarmReceiver.DAILY_REMINDER_TYPE);

        ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getListItem().observe(this, observer);
        Date d = new Date();
        CharSequence date = android.text.format.DateFormat.format(DATE_FORMAT, d.getTime());
        itemViewModel.setItem(String.format(date.toString(), ItemViewModel.API_KEY, date, date), ItemViewModel.ITEM_MOVIE);

        String title = null;

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, new MovieFragment()).commit();
            title = getResources().getString(R.string.movie);
        } else {
            switch (savedInstanceState.getInt(STATE_ID_MENU_ITEM_BOTTOM_NAV)){
                case R.id.menu_nav_movie:
                    title = getResources().getString(R.string.movie);
                    break;
                case R.id.menu_nav_tvShow:
                    title = getResources().getString(R.string.tv_show);
                    break;
                case R.id.menu_nav_favorite:
                    title = getResources().getString(R.string.favorite);
                    break;
                default:
                    title = getResources().getString(R.string.app_name);
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }

        msv.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = null;
                switch (bottomNav.getSelectedItemId()) {
                    case R.id.menu_nav_movie:
                        intent = new Intent(getApplicationContext(), SearchMovie.class);
                        break;
                    case R.id.menu_nav_tvShow:
                        intent = new Intent(getApplicationContext(), SearchTv.class);
                        break;
                }
                if (intent!=null) {
                    intent.putExtra(SearchMovie.EXTRA_QUERY, query);
                    startActivity(intent);
                    msv.closeSearch();
                    return true;
                } else return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                String title = null;
                switch (menuItem.getItemId()){
                    case R.id.menu_nav_movie:
                        selectedFragment = new MovieFragment();
                        title = getResources().getString(R.string.movie);
                        break;
                    case R.id.menu_nav_tvShow:
                        selectedFragment = new TVShowFragment();
                        title = getResources().getString(R.string.tv_show);
                        break;
                    case R.id.menu_nav_favorite:
                        selectedFragment = new FavoriteFragment();
                        title = getResources().getString(R.string.favorite);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fr_container, selectedFragment).commit();
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(title);
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ID_MENU_ITEM_BOTTOM_NAV, bottomNav.getSelectedItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        msv.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_change_language){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}