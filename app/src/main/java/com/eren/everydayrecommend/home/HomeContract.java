package com.eren.everydayrecommend.home;

/**
 * Home界面契约接口
 */

public interface HomeContract {

    interface View {
        //开启加载动画
        void showLoading();

        //关闭加载动画
        void hideLoading();

        //开启下拉刷新
        //void startRefush();

        //关闭下拉刷新
        void stopRefresh();

        //开启加载更多
        void startLoadingMore();

        //关闭加载更多
        void stopLoadingMore();
    }
}
