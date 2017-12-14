package com.eren.everydayrecommend.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.home.HomeFragment;
import com.eren.everydayrecommend.home.model.DrawModel;
import com.eren.everydayrecommend.me.MeFragment;
import com.eren.everydayrecommend.read.ReadFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private final int NAVIGATION_HOME = 0;
    private final int NAVIGATION_READ = 1;
    private final int NAVIGATION_ME = 2;
    private ViewPager mVpMain;//主界面ViewPager
    private List<Fragment> mList;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;//侧滑菜单
    private BottomNavigationView mBottomNavigation;//底部导航
    private Menu mMenu;
    private RecyclerView mRvDrawList;
    private List<DrawModel> mListDrawModel;
    private MainDrawListAdapter mDrawListAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHome();
    }

    private void initHome() {
        initView();
        initToolbar();
        initNavigation();
        initFragment();
        initViewPager();
        initRecycler();
        initDraw();
    }

    /**
     * 底部导航栏
     */
    private void initNavigation() {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mVpMain.setCurrentItem(NAVIGATION_HOME);
                        return true;
                    case R.id.navigation_read:
                        mVpMain.setCurrentItem(NAVIGATION_READ);
                        return true;
                    case R.id.navigation_me:
                        mVpMain.setCurrentItem(NAVIGATION_ME);
                        return true;
                }
                return false;
            }
        };
    }

    /**
     * 初始化Toolbar信息
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //通过反射获取主标题控件，添加单机事件
        try {
            Field field = toolbar.getClass().getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            TextView textView = (TextView) field.get(toolbar);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                long firstTime = 0;

                @Override
                public void onClick(View v) {
                    //第一次按压的时间
                    long pressTime = System.currentTimeMillis();
                    //比较两次时间差
                    if (pressTime - firstTime > 500) {
                        firstTime = pressTime;
                        return;
                    }
                    //处理双击事件，让列表回滚到顶端
                    if (mVpMain.getCurrentItem() == 0) {
                        HomeFragment homeFragment = (HomeFragment) mList.get(0);
                        //HomeFragment homeFragment = new HomeFragment();  不能够直接调用，Fragment中的坑
                        homeFragment.scrollToTop();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mDrawer = (DrawerLayout) findViewById(R.id.drawlayout);
        mRvDrawList = (RecyclerView) findViewById(R.id.recyclerview_drawerlist);
        mDrawer.setScrimColor(Color.TRANSPARENT);
        mDrawer.setDrawerElevation(0);
    }

    /**
     * 初始化Fragment对象
     */
    private void initFragment() {
        mList = new ArrayList<>();
        mList.add(new HomeFragment());
        mList.add(new ReadFragment());
        mList.add(new MeFragment());
    }

    /**
     * 侧滑菜单栏的RecyclerView操作
     */
    private void initRecycler() {
        mRvDrawList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDrawListAdapter = new MainDrawListAdapter(this, initData());
        mRvDrawList.setAdapter(mDrawListAdapter);
        mDrawListAdapter.setmOnMainDrawClickListener(new MainDrawListAdapter.OnMainDrawClickListener() {
            @Override
            public void onClick(int position) {
                String categroy = Constant.CATEGORY_ALL;
                switch (mListDrawModel.get(position).getTitle()) {
                    case "iOS":
                        categroy = Constant.CATEGORY_IOS;
                        showCategoryInfo(categroy);
                        break;
                    case "前端":
                        categroy = Constant.CATEGORY_CLIENT;
                        showCategoryInfo(categroy);
                        break;
                    case "瞎推荐":
                        categroy = Constant.CATEGROY_RECOMMEND;
                        showCategoryInfo(categroy);
                        break;
                    case "App":
                        categroy = Constant.CATEGORY_APP;
                        showCategoryInfo(categroy);
                        break;
                    case "拓展资源":
                        categroy = Constant.CATEGORY_EXPANDRESOURCE;
                        showCategoryInfo(categroy);
                        break;
                    case "休息视频":
                        categroy = Constant.CATEGORY_VIDEO;
                        showCategoryInfo(categroy);
                        break;
                    case "福利":
                        categroy = Constant.CATEGORY_GIRL;
                        showCategoryInfo(categroy);
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * ViewPager操作
     */
    private void initViewPager() {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), MainActivity.this, mList);
        mVpMain.setAdapter(adapter);
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case NAVIGATION_HOME:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case NAVIGATION_READ:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_read);
                        break;
                    case NAVIGATION_ME:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * DrawLayout操作
     */
    private void initDraw() {
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // 得到contentView
                View content = mDrawer.getChildAt(0);
                int offset = (int) (drawerView.getWidth() * slideOffset);
                content.setTranslationX(offset);
//                content.setScaleX(1 - slideOffset * 0.2f);
//                content.setScaleY(1 - slideOffset * 0.2f);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 侧边栏的设置集合
     *
     * @return
     */
    private List<DrawModel> initData() {
        mListDrawModel = new ArrayList<>();
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_ios, Constant.CATEGORY_IOS));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_girl, Constant.CATEGORY_GIRL));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_client, Constant.CATEGORY_CLIENT));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_recommend, Constant.CATEGROY_RECOMMEND));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_app, Constant.CATEGORY_APP));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_resource, Constant.CATEGORY_EXPANDRESOURCE));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_video, Constant.CATEGORY_VIDEO));
        return mListDrawModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * 跳转到分类展示界面
     *
     * @param categroy
     */
    private void showCategoryInfo(String categroy) {
        Toast.makeText(this, "跳转分类：" + categroy, Toast.LENGTH_SHORT).show();
    }


}
