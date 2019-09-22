package com.muflihun.moviecatalogue5.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.activities.DetailMovie;
import com.muflihun.moviecatalogue5.adapters.ListItemAdapter;
import com.muflihun.moviecatalogue5.db.MovieHelper;
import com.muflihun.moviecatalogue5.helpers.MappingHelper;
import com.muflihun.moviecatalogue5.models.Item;

import java.util.ArrayList;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements ListItemAdapter.OnItemClickCallback {
    private RecyclerView rvMovie;
    private MovieHelper movieHelper;
    private static final String TAG = "FavoriteMovieFragment";

    private ListItemAdapter adapter;
    private ArrayList<Item> listItem;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie = view.findViewById(R.id.rv_fav_movie);

        movieHelper = MovieHelper.getInstance(getContext());
        listItem = new ArrayList<>();
        adapter = new ListItemAdapter(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        listItem.clear();
        listItem.addAll(MappingHelper.mapCursorToArrayList(cursor));
        adapter.setOnClickCallback(this);
        adapter.setData(listItem);
        adapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setAdapter(adapter);
    }

    @Override
    public void onClicked(View v, Item item, int position) {
        Intent intent = new Intent(getContext(), DetailMovie.class);
        Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + item.getId());
        intent.setData(uri);
        intent.putExtra(DetailMovie.EXTRA_ITEM, item);
        startActivity(intent);
    }
}
