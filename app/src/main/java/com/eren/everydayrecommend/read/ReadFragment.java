package com.eren.everydayrecommend.read;

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
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.net.Api;
import com.eren.everydayrecommend.net.HttpManager;
import com.eren.everydayrecommend.read.model.ReadModel;
import com.eren.everydayrecommend.widget.EmptyRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Read界面
 */
public class ReadFragment extends Fragment {
    public View mView;
    private Context mContex;
    private EmptyRecyclerView mRecyclerView;
    private ReadAdapter mReadAdapter;
    private List<ReadModel.NewslistEntity> mReadList;
    private Disposable mDisposable;
    private TextView mTvNoNetwork;
    private View mEmptyView;
    private SwipeRefreshLayout mRefreshLayout;
    private AVLoadingIndicatorView mAvi;
    private AVLoadingIndicatorView mAviLoadMore;
    private int mPage = 1;
    private LinearLayout mLayoutLoadMore;
    private boolean mIsLoadMore = true;//是否可以加载更多

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContex = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_read, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContex, LinearLayoutManager.VERTICAL, false));
        mReadList = new ArrayList<>();
        mReadAdapter = new ReadAdapter(mContex, mReadList);
        mRecyclerView.setAdapter(mReadAdapter);
        mRecyclerView.setmEmptyView(mEmptyView);
        mRecyclerView.hideEmptyView();
        showLoading();
        getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);

    }

    private void initView(View view) {
        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.read_fragment_recyclerview);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        mAvi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mAviLoadMore = (AVLoadingIndicatorView) view.findViewById(R.id.avi_loadmore);
        mTvNoNetwork = (TextView) view.findViewById(R.id.tv_nonetwork);
        mLayoutLoadMore = (LinearLayout) view.findViewById(R.id.layout_loadmore);
        //设置下拉刷新样式
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新事件
                mPage = 1;
                mIsLoadMore = true;
                getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        //监听上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }

    public void getDataFromServer(final int type) {
        Api api = HttpManager.getInstance().getApiService(Constant.BASE_URL_Read);
        api.getReadData(mPage, Constant.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReadModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull ReadModel realmModel) {
                        //更新界面数据
                        if (Constant.GET_DATA_TYPE_NOMAL == type) {
                            //正常模式下，清空之前数据，重新加载
                            mReadList.clear();
                            mReadList = realmModel.getNewslist();
                        } else {
                            //加载更多模式
                            mReadList.addAll(realmModel.getNewslist());
                        }

                        //如果获取的数据不足一页，代表当前已经没有更过数据，关闭加载更多
                        if (realmModel.getNewslist().size() < Constant.PAGE_SIZE) {
                            mIsLoadMore = false;
                        }
                        mReadAdapter.setmList(mReadList);
                        mReadAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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
     * 开启加载中动画
     */
    public void showLoading() {
        mAvi.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */

    public void hideLoading() {
        if (mAvi.isShown()) {
            mAvi.smoothToHide();
        }
    }

    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public void stopLoadingMore() {
        mLayoutLoadMore.setVisibility(View.GONE);
        mAviLoadMore.smoothToHide();
    }

    /**
     * 开启加载更多动画
     */
    public void startLoadingMore() {
        mLayoutLoadMore.setVisibility(View.VISIBLE);
        mAviLoadMore.smoothToShow();
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
     * RecyclerView 滑动监听器
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mReadList.size() < 1) {
                return;
            }
            //如果正在下拉刷新则放弃监听状态
            if (mRefreshLayout.isRefreshing()) {
                return;
            }
            //当前RecyclerView显示出来的最后一个的item的position,默认为-1
            int lastPosition = -1;
            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //分别判断三种类型
                lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                // 判断是否可以向下滑动，如果不能就代表已经到底部
                if (!recyclerView.canScrollVertically(1)) {
                    recyclerView.smoothScrollToPosition(lastPosition);
                    if (!mIsLoadMore) {
                        ToastUtils.showShortSafe("木有更多数据了...");
                        return;
                    }
                    //此时需要请求更多数据，显示加载更多界面
                    mPage++;
                    startLoadingMore();
                    getDataFromServer(Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }
}
