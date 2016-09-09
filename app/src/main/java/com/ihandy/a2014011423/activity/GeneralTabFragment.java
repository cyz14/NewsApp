package com.ihandy.a2014011423.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.adapter.SwipeListAdapter;
import com.ihandy.a2014011423.content.CategoryFetcher;
import com.ihandy.a2014011423.model.CategoryLabel;
import com.ihandy.a2014011423.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyz14 on 2016/9/5.
 * Template fragment to create tabs
 */
public class GeneralTabFragment extends Fragment {
    private static List<CategoryLabel> watched = null;
    private int index;
    private CategoryLabel mCategoryLabel;
    private List<NewsItem> newsItems = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout layout;
    private SwipeListAdapter mAdapter;
    private RecyclerView recyclerView;

    public GeneralTabFragment() {
    }

    public static GeneralTabFragment getInstance(int index) {
        GeneralTabFragment tabFragment = new GeneralTabFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    public static List<CategoryLabel> getWatched() {
        return watched;
    }

    public static void setWatched(List<CategoryLabel> _labels) {
        watched = _labels;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = (getArguments() != null ? getArguments().getInt("index") : 0);
        Log.d("Category", String.valueOf(index));
        mCategoryLabel = watched.get(index);
        Log.d("Category", mCategoryLabel.getTitle());
        newsItems = getNews(mCategoryLabel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_tab, container, false);
        // Need to setup adapter and show list of news
        layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SwipeListAdapter(this.getActivity(), newsItems);
        recyclerView.setAdapter(mAdapter);

        // handle pull to refresh
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setEnabled(false);
                mCategoryLabel = watched.get(index);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<NewsItem> temp = CategoryFetcher
                                .getNewsOfCategory(mCategoryLabel.getLabel());
                        newsItems = temp;
                        mAdapter.notifyDataSetChanged();
                        layout.setEnabled(true);
                        layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // handle load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 3 == mAdapter.getItemCount()) {
                    mAdapter.setLoadMoreStatus(SwipeListAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newsItems = CategoryFetcher
                                    .getMoreNewsOfCategoryBefore(
                                            mCategoryLabel.getLabel(),
                                            newsItems.get(newsItems.size()-1));
                            if (newsItems.size() == 0) {
                                return;
                            }
                            mAdapter.setLoadMoreStatus(SwipeListAdapter.PULLUP_LOAD_MORE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
            }
        });
        return view;
    }

    private List<NewsItem> getNews(CategoryLabel categoryLabel) {
        return CategoryFetcher.getNewsOfCategory(categoryLabel.getLabel());
    }

    public void updateView() {
        mAdapter.notifyDataSetChanged();
    }
}
