package com.ihandy.a2014011423.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.adapter.FragmentTabAdapter;
import com.ihandy.a2014011423.content.CategoryFetcher;
import com.ihandy.a2014011423.content.CategoryReadWrite;
import com.ihandy.a2014011423.model.CategoryLabel;

import java.util.List;

/**
 * Created by cyz14 on 2016/9/4.
 * Fragment to handle scrollable tabs
 */
public class FragmentTab extends Fragment{

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private static List<CategoryLabel> labels;
    private static FragmentTabAdapter adapter;

    public FragmentTab() {
    }

    @Nullable
    public List<CategoryLabel> getData() {
        // this is in another thread, may use some time
        labels = CategoryReadWrite.getWatchedList();
        if (labels == null)
            labels = CategoryFetcher.fetchCategory();
        return labels;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        labels = getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate tab_layout and set up views
        final View view = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        // Set an Adapter for the view pager
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new FragmentTabAdapter(getChildFragmentManager(), labels);
        viewPager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return view;
    }

    public void update() {
        labels = CategoryReadWrite.getWatchedList();
        for (int i = 0; i< labels.size(); i++) {
            Log.d("tabLabel", labels.get(i).getLabel());
        }
        adapter.notifyDataSetChanged();
        viewPager.getAdapter().notifyDataSetChanged();
    }
}
