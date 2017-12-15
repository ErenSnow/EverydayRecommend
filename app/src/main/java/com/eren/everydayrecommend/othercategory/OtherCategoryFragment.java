package com.eren.everydayrecommend.othercategory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.eren.everydayrecommend.base.BaseAdapter;
import com.eren.everydayrecommend.base.BaseFragment;
import com.eren.everydayrecommend.detail.DetailActivity;
import com.eren.everydayrecommend.home.model.GankModel;


/**
 * @author Leon
 * @date
 */

public class OtherCategoryFragment extends BaseFragment {
    private static final String CATEGORY = "category";

    public static Fragment newInstence(String tag) {
        OtherCategoryFragment otherCategoryFragment = new OtherCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, tag);
        otherCategoryFragment.setArguments(bundle);
        return otherCategoryFragment;
    }

    @Override
    public String getApiCateGory() {
        return getArguments().getString(CATEGORY);
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
