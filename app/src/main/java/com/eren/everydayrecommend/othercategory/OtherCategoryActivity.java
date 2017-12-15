package com.eren.everydayrecommend.othercategory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.base.BaseActivity;

public class OtherCategoryActivity extends BaseActivity {

    private View mView;
    private String mTitleCategory;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_category);
    }

    @Override
    protected void initOptions() {
        mTitleCategory = getIntent().getStringExtra("category");
        //展示具体的数据
        mFragment = OtherCategoryFragment.newInstence(mTitleCategory);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, mFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected View initContentView() {
        mView = View.inflate(this, R.layout.activity_other_category, null);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return mTitleCategory;
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
    }
}
