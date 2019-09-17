package com.muflihun.moviecatalogue5.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.adapters.PagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        final ViewPager viewPager = view.findViewById(R.id.vp_fav);
        TabLayout tabFav = view.findViewById(R.id.tl_fav);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), 2, getContext());
        Fragment movieFavFragment = new FavoriteMovieFragment();
        Fragment tvShowFavFragment = new FavoriteTvFragment();
        pagerAdapter.attach(movieFavFragment, getResources().getString(R.string.movie));
        pagerAdapter.attach(tvShowFavFragment, getResources().getString(R.string.tv_show));
        viewPager.setAdapter(pagerAdapter);
        tabFav.setupWithViewPager(viewPager);
        tabFav.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
