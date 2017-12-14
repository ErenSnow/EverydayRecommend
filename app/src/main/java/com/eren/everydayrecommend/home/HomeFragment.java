package com.eren.everydayrecommend.home;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.net.Api;
import com.eren.everydayrecommend.net.HttpManager;
import com.eren.everydayrecommend.widget.EmptyRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.NetworkUtils.isConnected;

/**
 * Home界面
 */
public class HomeFragment extends Fragment implements HomeContract.View {
    protected Activity mContext;
    /**
     * 整个布局
     */
    private View mView;
    protected EmptyRecyclerView mRecyclerView;
    private Disposable mDisposable;
    protected List<GankModel.ResultsEntity> mList = new ArrayList<>();
    //空布局对象
    protected View mEmptyView;
    protected HomeAdapter mHomeAdapter;
    //刷新布局
    private SwipeRefreshLayout mRefreshLayout;
    //加载中动画
    private AVLoadingIndicatorView mAvi;
    //加载更多动画
    private AVLoadingIndicatorView mAviLoadMore;
    protected int mPage = 1;
    //加载更多布局
    private LinearLayout mLayoutLoadMore;
    /**
     * 是否可以加载更多
     */
    protected boolean mIsLoadMore = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //设置布局类型
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //创建适配器
        mHomeAdapter = new HomeAdapter(mContext, mList);
        mRecyclerView.setAdapter(mHomeAdapter);
        //设置空布局展示
        mRecyclerView.setmEmptyView(mEmptyView);
        //初次进入隐藏空布局展示，显示加载动画
        mRecyclerView.hideEmptyView();

        if (isConnected()) {
            showLoading();
            getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
        }
    }

    /**
     * 初始化控件对象，并设置相关监听器和初始化参数
     *
     * @param view
     */
    private void initView(View view) {
        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.fragment_recyclerview);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        mAvi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mAviLoadMore = (AVLoadingIndicatorView) view.findViewById(R.id.avi_loadmore);

        mLayoutLoadMore = (LinearLayout) view.findViewById(R.id.layout_loadmore);
        //设置下拉刷新样式颜色
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        //下拉刷新事件
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mIsLoadMore = true;
                getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        //监听上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }

    /**
     * 从服务端请求数据
     */
    protected void getDataFromServer(final int type) {
        Api api = HttpManager.getInstance().getApiService();
        api.getCategoryData(Constant.CATEGORY_Android, Constant.PAGE_SIZE, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(GankModel value) {
                        if (value.getError()) {
                            //服务端请求数据发生错误
                            ToastUtils.showShortSafe("服务端异常，请稍后再试");
                            return;
                        }
                        //更新界面数据
                        if (Constant.GET_DATA_TYPE_NOMAL == type) {
                            //正常模式下，清空之前数据，重新加载
                            mList.clear();
                            mList = value.getResults();
                        } else {
                            //加载更多模式
                            mList.addAll(value.getResults());
                        }
                        //如果获取的数据不足一页，代表当前已经没有更过数据，关闭加载更多
                        if (value.getResults().size() < Constant.PAGE_SIZE) {
                            mIsLoadMore = false;
                        }
                        mHomeAdapter.setmListData(mList);
                        mHomeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopRefresh();
                        hideLoading();
                        stopLoadingMore();
                    }

                    @Override
                    public void onComplete() {
                        stopRefresh();
                        hideLoading();
                        stopLoadingMore();
                    }
                });
    }

    /**
     * 让列表回滚到顶端
     */
    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * 开启加载中动画
     */
    @Override
    public void showLoading() {
        mAvi.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    @Override
    public void hideLoading() {
        if (mAvi.isShown()) {
            mAvi.smoothToHide();
        }
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }


    /**
     * 开启加载更多动画
     */
    @Override
    public void startLoadingMore() {
        mLayoutLoadMore.setVisibility(View.VISIBLE);
        mAviLoadMore.smoothToShow();
    }

    /**
     * 停止显示加载更多
     */
    @Override
    public void stopLoadingMore() {
        mLayoutLoadMore.setVisibility(View.GONE);
        mAviLoadMore.smoothToHide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出的时候不在回调更新界面
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
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
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                //判断当前列表是否滑动到底部
                if (!mRecyclerView.canScrollVertically(1)) {
                    //滑动到底部，需要触发上拉加载更多操作
                    mRecyclerView.smoothScrollToPosition(lastPosition);
                    if (!mIsLoadMore) {
                        ToastUtils.showShort("没有更多数据了");
                        return;
                    }
                    startLoadingMore();
                    mPage++;
                    getDataFromServer(Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }
}
