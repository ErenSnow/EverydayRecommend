package com.eren.everydayrecommend.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主页面FragmentPagerAdapter适配器
 */

public class MainAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mList;

    public MainAdapter(FragmentManager fm, Context mContext, List<Fragment> mList) {
        super(fm);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }


    public void setmList(List<Fragment> mList) {
        this.mList = mList;
    }
}
