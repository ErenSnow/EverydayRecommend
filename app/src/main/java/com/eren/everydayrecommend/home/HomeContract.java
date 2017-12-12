package com.eren.everydayrecommend.home;

/**
 * @author Leon
 * @date
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

        //开启加载更多
        void stopLoadingMore();
    }
}
