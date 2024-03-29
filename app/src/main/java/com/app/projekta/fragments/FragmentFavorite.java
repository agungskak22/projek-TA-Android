package com.app.projekta.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.projekta.Config;
import com.app.projekta.activities.MainActivity;
import com.app.projekta.adapters.AdapterFavorite;
import com.app.projekta.databases.DatabaseHandlerFavorite;
import com.app.projekta.models.Channel;
import com.app.projekta.utils.Constant;
import com.app.projekta.utils.SpacesItemDecoration;
import com.app.projekta.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {

    private MainActivity mainActivity;
    private Toolbar toolbar;
    private List<Channel> data = new ArrayList<Channel>();
    View root_view, parent_view;
    AdapterFavorite adapterPostList;
    DatabaseHandlerFavorite databaseHandler;
    RecyclerView recyclerView;
    LinearLayout linearLayout;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_favorite, null);
        parent_view = getActivity().findViewById(R.id.lyt_content);

        toolbar = (Toolbar) root_view.findViewById(R.id.toolbar);
        setupToolbar();

        linearLayout = (LinearLayout) root_view.findViewById(R.id.lyt_no_favorite);
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);

        if (Config.ENABLE_GRID_MODE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Config.GRID_SPAN_COUNT));
            recyclerView.addItemDecoration(new SpacesItemDecoration(Config.GRID_SPAN_COUNT, Constant.SPACE_ITEM_DECORATION, true));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        databaseHandler = new DatabaseHandlerFavorite(getActivity());
        data = databaseHandler.getAllData();

        adapterPostList = new AdapterFavorite(getActivity(), recyclerView, data);
        recyclerView.setAdapter(adapterPostList);

        if (data.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
        }

        return root_view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.tab_favorite));
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {

        super.onResume();

        data = databaseHandler.getAllData();
        adapterPostList = new AdapterFavorite(getActivity(), recyclerView, data);
        recyclerView.setAdapter(adapterPostList);

        if (data.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

}
