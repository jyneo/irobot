package com.arcsoft.irobot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 底部导航对应的Main ViewPager的适配器
 * Created by yj2595 on 2018/3/6.
 */

public class MainAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public MainAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
