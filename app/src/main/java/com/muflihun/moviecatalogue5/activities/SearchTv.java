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

import static com.muflihun.moviecatalogue5.activities.SearchMovie.EXTRA_QUERY;

public class SearchTv extends AppCompatActivity implements ListItemAdapter.OnItemClickCallback {
    private RecyclerView rvTv;
    private ProgressBar pbTv;

    private ListItemAdapter adapter;
    private ItemViewModel tvViewModel;

    private Observer<ArrayList<Item>> tvObserver = new Observer<ArrayList<Item>>() {
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
        setContentView(R.layout.activity_search_tv);

        rvTv = findViewById(R.id.rv_search_tv);
        pbTv = findViewById(R.id.pb_search_tv);

        String query = getIntent().getStringExtra(EXTRA_QUERY);

        getSupportActionBar().setTitle(String.format("\"%s\"", query));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ListItemAdapter(this);
        adapter.setOnClickCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTv.setLayoutManager(layoutManager);
        rvTv.setAdapter(adapter);

        tvViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        tvViewModel.getListItem().observe(this, tvObserver);
        tvViewModel.setItem(ItemViewModel.LIST_TV_URL, ItemViewModel.ITEM_TVSHOW);
        showLoading(true);
    }

    @Override
    public void onClicked(View v, Item item, int position) {
        Intent intent = new Intent(this, DetailTv.class);
        intent.putExtra(DetailMovie.EXTRA_ITEM, item);
        startActivity(intent);
    }

    private void showLoading(boolean state){
        if (state){
            pbTv.setVisibility(View.VISIBLE);
        } else {
            pbTv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
