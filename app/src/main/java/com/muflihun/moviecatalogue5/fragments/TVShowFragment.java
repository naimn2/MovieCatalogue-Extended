package com.muflihun.moviecatalogue5.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.activities.DetailMovie;
import com.muflihun.moviecatalogue5.activities.DetailTv;
import com.muflihun.moviecatalogue5.adapters.ListItemAdapter;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.viewmodels.ItemViewModel;

import java.util.ArrayList;

public class TVShowFragment extends Fragment implements ListItemAdapter.OnItemClickCallback {
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

    public TVShowFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTv = view.findViewById(R.id.rv_tv);
        pbTv = view.findViewById(R.id.pb_tv);

        adapter = new ListItemAdapter(getContext());
        adapter.setOnClickCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTv.setLayoutManager(layoutManager);
        rvTv.setAdapter(adapter);

        tvViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        tvViewModel.getListItem().observe(this, tvObserver);
        tvViewModel.setItem(ItemViewModel.LIST_TV_URL, ItemViewModel.ITEM_TVSHOW);
        showLoading(true);
    }

    @Override
    public void onClicked(View v, Item item, int position) {
        Intent intent = new Intent(getContext(), DetailTv.class);
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
}
