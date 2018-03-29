package com.arcsoft.irobot.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.ViewGroup;

import java.util.List;

/**
 * 底部导航对应的Main ViewPager的适配器
 * Created by yj2595 on 2018/3/6.
 */

public class MainAdapter extends MyPagerAdapter {
    private List<Fragment> mFragmentList;
    private FragmentManager fragmentManager;

    public MainAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm, mFragmentList);
        this.fragmentManager = fm;
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

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        Fragment fragment = mFragmentList.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
