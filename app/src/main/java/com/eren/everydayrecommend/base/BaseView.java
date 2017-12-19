package com.eren.everydayrecommend.base;

/**
 * Created by smartOrange_5 on 2017/12/14.
 */

public interface BaseView {

    /**
     * 开启加载动画
     */
    void showLoading();

    /**
     * 关闭加载动画
     */
    void hideLoading();

    /**
     * 开启下拉刷新
     */
    //void startRefush();

    /**
     * 关闭下拉刷新
     */
    void stopRefresh();

    /**
     * 开启加载更多
     */
    void startLoadingMore();

    /**
     * 关闭加载更多
     */
    void stopLoadingMore();
}
