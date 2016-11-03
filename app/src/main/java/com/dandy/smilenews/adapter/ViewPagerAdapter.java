package com.dandy.smilenews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dandy.smilenews.ui.ContentFragment;

import java.util.List;

/**
 * Created by Dandy on 2016/10/28.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles;


    public ViewPagerAdapter(FragmentManager fm, List<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;

    }

    @Override
    public Fragment getItem(int position) {

        return ContentFragment.instance(position);
    }

    @Override
    public int getCount(){
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
