package com.eren.everydayrecommend.girl;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.base.BaseAdapter;
import com.eren.everydayrecommend.base.BaseFragment;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.showpic.ShowPicActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

/**
 * Created by smartOrange_5 on 2017/12/15.
 */

public class GirlFragment extends BaseFragment implements View.OnClickListener {
    private FloatingActionButton mItemLinearlayout;
    private FloatingActionButton mItemGridlayout;
    private FloatingActionButton mItemStaggeredlayout;
    private FloatingActionMenu mActionMenu;

    private int mCurrentType = 2;
    private final int TYPE_LINEARLAYOUT = 1;
    private final int TYPE_GRIDLAYOUT = 2;
    private final int TYPE_STAGGEREDLAYOUT = 3;

    @Override
    protected String getApiCateGory() {
        return "福利";
    }

    @Override
    protected void initOptions(View view) {
        mItemLinearlayout = (FloatingActionButton) view.findViewById(R.id.menu_item_linearlayout);
        mItemGridlayout = (FloatingActionButton) view.findViewById(R.id.menu_item_gridlayout);
        mItemStaggeredlayout = (FloatingActionButton) view.findViewById(R.id.menu_item_staggeredlayout);
        mActionMenu = (FloatingActionMenu) view.findViewById(R.id.actionmenu);

        mActionMenu.setVisibility(View.VISIBLE);
        mItemLinearlayout.setOnClickListener(this);
        mItemGridlayout.setOnClickListener(this);
        mItemStaggeredlayout.setOnClickListener(this);
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManager() {
        return new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected int initItemType() {
        return Constant.ITEM_TYPE_IMAGE;
    }

    @Override
    protected void initItemListener() {
        mBaseAdapter.addOnClickListener(new BaseAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity resultsBean) {
                //跳转界面，显示大图
                Intent intent = new Intent(mContext, ShowPicActivity.class);
                //传入参数，图片URL地址
                ArrayList<String> listPics = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    listPics.add(mList.get(i).getUrl());
                }
                intent.putStringArrayListExtra("picList", listPics);
                intent.putExtra("position", position);
                intent.putExtra("page", mPage);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item_linearlayout:
                if (mCurrentType == TYPE_LINEARLAYOUT) {
                    mActionMenu.close(true);
                    return;
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mActionMenu.close(true);
                mCurrentType = TYPE_LINEARLAYOUT;
                break;
            case R.id.menu_item_gridlayout:
                if (mCurrentType == TYPE_GRIDLAYOUT) {
                    mActionMenu.close(true);
                    return;
                }
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
                mActionMenu.close(true);
                mCurrentType = TYPE_GRIDLAYOUT;
                break;
            case R.id.menu_item_staggeredlayout:
                mActionMenu.close(true);
                break;
        }
    }
}
