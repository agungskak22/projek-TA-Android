package com.app.projekta.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.projekta.Config;
import com.app.projekta.activities.ActivityDetailChannel;
import com.app.projekta.adapters.AdapterChannel;
import com.app.projekta.callbacks.CallbackChannel;
import com.app.projekta.models.Channel;
import com.app.projekta.rests.ApiInterface;
import com.app.projekta.rests.RestAdapter;
import com.app.projekta.utils.Constant;
import com.app.projekta.utils.NetworkCheck;
import com.app.projekta.utils.SpacesItemDecoration;
import com.app.projekta.R;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRecent extends Fragment {

    View root_view, parent_view;
    private RecyclerView recyclerView;
    private AdapterChannel adapterChannel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<CallbackChannel> callbackCall = null;
    private int post_total = 0;
    private int failed_page = 0;
    private InterstitialAd interstitialAd;
    int counter = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_recent, null);
        parent_view = getActivity().findViewById(R.id.lyt_content);


        swipeRefreshLayout = (SwipeRefreshLayout) root_view.findViewById(R.id.swipe_refresh_layout_home);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerViewHome);

        if (Config.ENABLE_GRID_MODE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Config.GRID_SPAN_COUNT));
            recyclerView.addItemDecoration(new SpacesItemDecoration(Config.GRID_SPAN_COUNT, Constant.SPACE_ITEM_DECORATION, true));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        adapterChannel = new AdapterChannel(getActivity(), recyclerView, new ArrayList<Channel>());
        recyclerView.setAdapter(adapterChannel);

        // on item list clicked
        adapterChannel.setOnItemClickListener(new AdapterChannel.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Channel obj, int position) {
                Intent intent = new Intent(getActivity(), ActivityDetailChannel.class);
                intent.putExtra(Constant.KEY_CHANNEL_CATEGORY, obj.category_name);
                intent.putExtra(Constant.KEY_CHANNEL_ID, obj.channel_id);
                intent.putExtra(Constant.KEY_CHANNEL_NAME, obj.channel_name);
                intent.putExtra(Constant.KEY_CHANNEL_IMAGE, obj.channel_image);
                intent.putExtra(Constant.KEY_CHANNEL_URL, obj.channel_url);
                intent.putExtra(Constant.KEY_CHANNEL_DESCRIPTION, obj.channel_description);
                intent.putExtra(Constant.KEY_CHANNEL_TYPE, obj.channel_type);
                intent.putExtra(Constant.KEY_VIDEO_ID, obj.video_id);
                startActivity(intent);

            }
        });

        // detect when scroll reach bottom
        adapterChannel.setOnLoadMoreListener(new AdapterChannel.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > adapterChannel.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(next_page);
                } else {
                    adapterChannel.setLoaded();
                }
            }
        });

        // on swipe list
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (callbackCall != null && callbackCall.isExecuted()) callbackCall.cancel();
                adapterChannel.resetListData();
                requestAction(1);
            }
        });

        requestAction(1);


        return root_view;
    }

    private void displayApiResult(final List<Channel> channels) {
        adapterChannel.insertData(channels);
        swipeProgress(false);
        if (channels.size() == 0) {
            showNoItemView(true);
        }
    }

    private void requestListPostApi(final int page_no) {
        ApiInterface apiInterface = RestAdapter.createAPI();
        callbackCall = apiInterface.getPostByPage(page_no, Config.LOAD_MORE);
        callbackCall.enqueue(new Callback<CallbackChannel>() {
            @Override
            public void onResponse(Call<CallbackChannel> call, Response<CallbackChannel> response) {
                CallbackChannel resp = response.body();
                if (resp != null && resp.status.equals("ok")) {
                    post_total = resp.count_total;
                    displayApiResult(resp.posts);
                } else {
                    onFailRequest(page_no);
                }
            }

            @Override
            public void onFailure(Call<CallbackChannel> call, Throwable t) {
                if (!call.isCanceled()) onFailRequest(page_no);
            }

        });
    }

    private void onFailRequest(int page_no) {
        failed_page = page_no;
        adapterChannel.setLoaded();
        swipeProgress(false);
        if (NetworkCheck.isConnect(getActivity())) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void requestAction(final int page_no) {
        showFailedView(false, "");
        showNoItemView(false);
        if (page_no == 1) {
            swipeProgress(true);
        } else {
            adapterChannel.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestListPostApi(page_no);
            }
        }, Constant.DELAY_TIME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
        if (callbackCall != null && callbackCall.isExecuted()) {
            callbackCall.cancel();
        }
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = (View) root_view.findViewById(R.id.lyt_failed_home);
        ((TextView) root_view.findViewById(R.id.failed_message)).setText(message);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) root_view.findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(failed_page);
            }
        });
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) root_view.findViewById(R.id.lyt_no_item_home);
        ((TextView) root_view.findViewById(R.id.no_item_message)).setText(R.string.no_post_found);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipeRefreshLayout.setRefreshing(show);
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

}
