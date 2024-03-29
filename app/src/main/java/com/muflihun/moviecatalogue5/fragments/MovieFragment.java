package com.muflihun.moviecatalogue5.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.muflihun.moviecatalogue5.adapters.ListItemAdapter;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.viewmodels.ItemViewModel;

import java.util.ArrayList;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_MOVIE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements ListItemAdapter.OnItemClickCallback {

    private static final String TAG = MovieFragment.class.getSimpleName();
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

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMovie = view.findViewById(R.id.rv_movie);
        pbMovie = view.findViewById(R.id.pb_movie);

        adapter = new ListItemAdapter(getContext());
        adapter.setOnClickCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        movieViewModel.getListItem().observe(this, movieObserver);
        movieViewModel.setItem(ItemViewModel.LIST_MOVIE_URL, ItemViewModel.ITEM_MOVIE);
        showLoading(true);
    }

    @Override
    public void onClicked(View v, Item item, int position) {
        Intent intent = new Intent(getContext(), DetailMovie.class);
        Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + item.getId());
        intent.setData(uri);
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
}
