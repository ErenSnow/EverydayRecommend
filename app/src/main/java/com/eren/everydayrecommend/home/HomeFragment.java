package com.eren.everydayrecommend.home;


import android.content.Intent;

import com.eren.everydayrecommend.base.BaseAdapter;
import com.eren.everydayrecommend.base.BaseFragment;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.detail.DetailActivity;
import com.eren.everydayrecommend.home.model.GankModel;

/**
 * Home界面
 */
public class HomeFragment extends BaseFragment {

    @Override
    protected String getApiCateGory() {
        return Constant.CATEGORY_Android;
    }

    @Override
    protected void initItemListener() {
        mBaseAdapter.addOnClickListener(new BaseAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity entity) {
                //跳转到详情界面
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {

            }
        });
    }
}
