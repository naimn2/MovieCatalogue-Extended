package com.muflihun.moviecatalogue5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.adapters.ListItemAdapter;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.viewmodels.ItemViewModel;

import java.util.ArrayList;

public class SearchMovie extends AppCompatActivity implements ListItemAdapter.OnItemClickCallback {
    public static final String EXTRA_QUERY = "extra_query";

    private RecyclerView rvMovie;
    private ProgressBar pbMovie;

    private ListItemAdapter adapter;
    private ItemViewModel movieViewModel;

    private Observer<ArrayList<Item>> movieObserver = new Observer<ArrayList<Item>>() {
        @Override
        public void onChanged(ArrayList<Item> items) {
            if (items != null){
                adapter.setData(items);
                showLoading(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        rvMovie = findViewById(R.id.rv_search_movie);
        pbMovie = findViewById(R.id.pb_search_movie);

        String query = getIntent().getStringExtra(EXTRA_QUERY);

        getSupportActionBar().setTitle(String.format("\"%s\"", query));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ListItemAdapter(this);
        adapter.setOnClickCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        movieViewModel.getListItem().observe(this, movieObserver);
        movieViewModel.setItem(String.format(ItemViewModel.FAVORITE_MOVIE_URL, query), ItemViewModel.ITEM_MOVIE);
        showLoading(true);
    }

    @Override
    public void onClicked(View v, Item item, int position) {
        Intent intent = new Intent(this, DetailMovie.class);
        intent.putExtra(DetailMovie.EXTRA_ITEM, item);
        startActivity(intent);
    }

    private void showLoading(boolean state){
        if (state){
            pbMovie.setVisibility(View.VISIBLE);
        } else {
            pbMovie.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
