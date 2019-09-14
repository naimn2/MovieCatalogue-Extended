package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.fragments.FavoriteFragment;
import com.muflihun.moviecatalogue5.fragments.FavoriteMovieFragment;
import com.muflihun.moviecatalogue5.fragments.FavoriteTvFragment;
import com.muflihun.moviecatalogue5.fragments.MovieFragment;
import com.muflihun.moviecatalogue5.fragments.TVShowFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    public final static String STATE_ID_MENU_ITEM_BOTTOM_NAV = "idMenuItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

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