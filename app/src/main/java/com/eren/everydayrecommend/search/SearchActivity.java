package com.eren.everydayrecommend.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.base.BaseAdapter;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.detail.DetailActivity;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.widget.EmptyRecyclerView;
import com.google.android.flexbox.FlexboxLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 搜索界面
 */
public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    private FlexboxLayout mFlexboxLayout;//流式标签云控件
    private EmptyRecyclerView mRecyclerViewHistory;

    private List<String> mHistoryTitles = new ArrayList<String>();//历史搜索数据
    private ImageView mIvDeleteAll;//清除所有按钮
    private ImageView mIvBack;//返回按钮
    private TextView mTvHotSearch;//热门搜索标签
    private RelativeLayout mLayoutHistory;//历史搜索标签
    private int mPage = 1;//当前页码
    private Disposable mDisposable;
    protected List<GankModel.ResultsEntity> mList = new ArrayList<>();//从服务端加载到的数据

    private AVLoadingIndicatorView mAvi;//加载中动画
    private AVLoadingIndicatorView mAviLoadMore;//加载更多动画
    private LinearLayout mLayoutLoadMore;//加载更多整体布局
    private String mKeywords;//搜索关键字
    private EditText mEtSearch;
    private TextView mTvSearch;//右侧搜索按钮
    private boolean mIsLoadMore = true;//是否可以加载更多
    private List<String> mHotTags;//热门标签数据
    private SearchPresenter mSearchPresenter;
    private HistorySearchAdapter mHistorySearchAdapter;
    private BaseAdapter mBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setPresenter();
        //加载历史搜索记录数据
        mHistoryTitles = mSearchPresenter.loadHistoryData();
        //加载热门标签数据
        mHotTags = mSearchPresenter.loadHotTag();
        //显示热门标签数据
        updateShowHotTag(mHotTags);
        //展示历史搜索效果
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //定义适配器
        mHistorySearchAdapter = new HistorySearchAdapter(this, mHistoryTitles);
        mRecyclerViewHistory.setAdapter(mHistorySearchAdapter);
        //设置空布局
        mRecyclerViewHistory.setmEmptyView(new TextView(this));
        mBaseAdapter = new BaseAdapter(this, mList, Constant.ITEM_TYPE_TEXT);
        hideLoading();
        initListener();
    }

    private void initListener() {
        //返回退出界面
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索按钮点击事件
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = mEtSearch.getText().toString().trim();
                mSearchPresenter.searchFromServer(keyword);
            }
        });
        //清除所有按钮事件
        mIvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryTitles.clear();
                mSearchPresenter.clearAllHistory("history_search");
                mHistorySearchAdapter.setmList(mHistoryTitles);
                mHistorySearchAdapter.notifyDataSetChanged();
            }
        });
        //历史搜索记录单击事件
        mHistorySearchAdapter.setmOnClickListener(new HistorySearchAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //发起搜索
                mKeywords = mHistoryTitles.get(position);
                mSearchPresenter.historyClick(mKeywords);
            }

            @Override
            public void onClearClick(int position) {
                //清除单个记录
                mHistoryTitles.remove(position);
                mSearchPresenter.saveHistoryData(mHistoryTitles);

            }
        });
        mBaseAdapter.addOnClickListener(new BaseAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity entity) {
                //跳转到详情界面
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {

            }
        });
        mRecyclerViewHistory.addOnScrollListener(new RecyclerViewScrollListener());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        mRecyclerViewHistory = (EmptyRecyclerView) findViewById(R.id.recyclerview_history);
        mIvDeleteAll = (ImageView) findViewById(R.id.iv_deleteall);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvHotSearch = (TextView) findViewById(R.id.tv_hotsearch);
        mLayoutHistory = (RelativeLayout) findViewById(R.id.layout_history);
        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mAviLoadMore = (AVLoadingIndicatorView) findViewById(R.id.avi_loadmore);
        mLayoutLoadMore = (LinearLayout) findViewById(R.id.layout_loadmore);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
    }

    /**
     * 展示热门标签云效果
     *
     * @param tags
     */
    private void updateShowHotTag(List<String> tags) {
        // 通过代码向FlexboxLayout添加View
        for (int i = 0; i < tags.size(); i++) {
            TextView textView = new TextView(this);
            textView.setBackground(getResources().getDrawable(R.drawable.flexbox_text_bg));
            textView.setText(tags.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(15, 15, 20, 20);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setTextColor(getResources().getColor(R.color.primary_text));
            mFlexboxLayout.addView(textView);
            //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
            ViewGroup.LayoutParams params = textView.getLayoutParams();
            if (params instanceof FlexboxLayout.LayoutParams) {
                FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
                //layoutParams.setFlexBasisPercent(0.5f);
                layoutParams.setMargins(10, 10, 20, 10);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    //得到搜索条件，首先屏蔽掉历史搜索和热门搜索
                    showSearchResult(true);
                    //更新界面，显示加载中
                    showLoading();
                    //保存搜索记录
                    mKeywords = tv.getText().toString().trim();
                    mSearchPresenter.addHistorySearch(mKeywords);
                    mEtSearch.setText(mKeywords);
                    //发起服务请求
                    mSearchPresenter.getDataFromService(mKeywords, mPage, Constant.GET_DATA_TYPE_NOMAL);
                }
            });
        }
    }

    @Override
    public void showLoading() {
        mAvi.setVisibility(VISIBLE);
        mRecyclerViewHistory.setVisibility(GONE);
        mAvi.smoothToShow();
    }

    @Override
    public void hideLoading() {
        mAvi.setVisibility(GONE);
        mRecyclerViewHistory.setVisibility(VISIBLE);
        mAvi.smoothToHide();
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void startLoadingMore() {
        hideLoading();
        mAviLoadMore.smoothToShow();
        mLayoutLoadMore.setVisibility(VISIBLE);
    }

    @Override
    public void stopLoadingMore() {
        mAviLoadMore.smoothToHide();
        mLayoutLoadMore.setVisibility(GONE);
    }

    @Override
    public void setEditText(String msg) {
        mEtSearch.setText(msg);
    }

    @Override
    public void showSearchResult(boolean flag) {
        if (flag) {
            mTvHotSearch.setVisibility(GONE);
            mLayoutHistory.setVisibility(GONE);
            mFlexboxLayout.setVisibility(GONE);
        } else {
            mTvHotSearch.setVisibility(VISIBLE);
            mLayoutHistory.setVisibility(VISIBLE);
            mFlexboxLayout.setVisibility(VISIBLE);
        }
    }

    @Override
    public void updateShow(GankModel gankModel, int type) {
        if (Constant.GET_DATA_TYPE_NOMAL == type) {
            //正常模式，清空数据，重新加载
            mList.clear();
            mList = gankModel.getResults();
        } else {
            //加载更多模式
            mList.addAll(gankModel.getResults());
        }
        //判断当前显示是哪个适配器
        if (mRecyclerViewHistory.getAdapter() instanceof HistorySearchAdapter) {
            mRecyclerViewHistory.setAdapter(mBaseAdapter);
        }
        mBaseAdapter.setmListData(mList);
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void setPresenter() {
        mSearchPresenter = new SearchPresenter(this);
    }

    /**
     * 滑动监听
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            int lastPosition = -1;
            //当前状态位停止状态
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = mRecyclerViewHistory.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                //判断当前列表是否滑动到底部
                if (!mRecyclerViewHistory.canScrollVertically(1)) {
                    //滑动到底部，需要触发上拉加载更多操作
                    mRecyclerViewHistory.smoothScrollToPosition(lastPosition);
                    if (!mIsLoadMore) {
                        ToastUtils.showShort("没有更多数据了");
                        return;
                    }
                    startLoadingMore();
                    mPage++;
                    mSearchPresenter.getDataFromService(mKeywords, mPage, Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }
}
