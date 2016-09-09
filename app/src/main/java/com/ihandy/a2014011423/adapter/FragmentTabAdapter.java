package com.ihandy.a2014011423.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ihandy.a2014011423.activity.GeneralTabFragment;
import com.ihandy.a2014011423.model.CategoryLabel;

import java.util.List;

/**
 * Created by cyz14 on 2016/9/5.
 * Adapter for FragmentTab fragment class
 */
public class FragmentTabAdapter extends FragmentStatePagerAdapter {
    private List<CategoryLabel> labels;
    public FragmentTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentTabAdapter(FragmentManager fm, List<CategoryLabel> labels) {
        super(fm);
        this.labels = labels;
    }

    @Override
    public Fragment getItem(int position) {
        if (GeneralTabFragment.getWatched() == null)
            GeneralTabFragment.setWatched(labels);
        return GeneralTabFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return labels.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object) {
        GeneralTabFragment f = (GeneralTabFragment)object;
        if (f != null)
            f.updateView();
        return super.getItemPosition(object);
    }
}
