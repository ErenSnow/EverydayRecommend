package com.eren.everydayrecommend.girl;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.base.BaseActivity;

public class GirlActivity extends BaseActivity {
    private View mView;

    @Override
    protected void initOptions() {
        //实现自己的fragment
        GirlFragment girlFragment = new GirlFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, girlFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected View initContentView() {
        mView = View.inflate(this, R.layout.activity_girl, null);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return "福利";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
    }
}
