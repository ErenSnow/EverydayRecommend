package com.eren.everydayrecommend.search;

import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.common.IPreference;
import com.eren.everydayrecommend.common.PreferenceImpl;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.net.Api;
import com.eren.everydayrecommend.net.HttpManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smartOrange_5 on 2017/12/18.
 * <p>
 * 搜索实现
 */

public class SearchPresenter implements SearchContract.Prestener {
    private PreferenceImpl mPreUtils;
    private SearchContract.View mView;
    private final String HISTORY_SEARCHA = "history_search";
    private List<String> mHistoryTitles;

    public SearchPresenter(SearchContract.View mView) {
        this.mView = mView;
    }

    @Override
    public List<String> loadHistoryData() {
        mPreUtils = (PreferenceImpl) IPreference.prefHolder.getPreference((Context) mView, HISTORY_SEARCHA);
        mHistoryTitles = new ArrayList<>();
        if (mPreUtils.getAll(HISTORY_SEARCHA) != null) {
            mHistoryTitles = mPreUtils.getAll(HISTORY_SEARCHA);
        }
        return mHistoryTitles;
    }

    @Override
    public List<String> loadHotTag() {
        List<String> mHotTitles = new ArrayList<>();
        mHotTitles.add("RxJava");
        mHotTitles.add("RxAndroid");
        mHotTitles.add("数据库");
        mHotTitles.add("自定义控件");
        mHotTitles.add("下拉刷新");
        mHotTitles.add("mvp");
        mHotTitles.add("直播");
        mHotTitles.add("权限管理");
        mHotTitles.add("Retrofit");
        mHotTitles.add("OkHttp");
        mHotTitles.add("WebView");
        mHotTitles.add("热修复");
        return mHotTitles;
    }

    @Override
    public void addHistorySearch(String keyword) {
        mHistoryTitles.add(keyword);
        mPreUtils.putAll(HISTORY_SEARCHA, mHistoryTitles);
    }

    @Override
    public void clearAllHistory(String tag) {
        mPreUtils.remove(tag);
    }

    @Override
    public void getDataFromService(String keyword, int page, final int type) {
        Api api = HttpManager.getInstance().getApiService();
        api.getSearchData(keyword, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GankModel gankModel) {
                        if (gankModel.getError()) {
                            mView.showErrorTip("服务端异常，请稍后再试");
                            return;
                        }
                        mView.updateShow(gankModel, type);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.stopLoadingMore();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.stopLoadingMore();
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void searchFromServer(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            ToastUtils.showShort("请输入搜索条件");
            return;
        }
        //添加到历史搜索记录里
        mHistoryTitles.add(keyword);
        mPreUtils.putAll(HISTORY_SEARCHA, mHistoryTitles);
        //显示搜索结果
        mView.showSearchResult(true);
        mView.showLoading();
        getDataFromService(keyword, 1, Constant.GET_DATA_TYPE_NOMAL);
    }

    @Override
    public void historyClick(String keyword) {
        //显示搜索结果
        mView.showSearchResult(true);
        mView.showLoading();
        mView.setEditText(keyword);
        getDataFromService(keyword, 1, Constant.GET_DATA_TYPE_NOMAL);
    }

    public void saveHistoryData(List<String> historyTitles) {
        mPreUtils.putAll(HISTORY_SEARCHA, historyTitles);
    }
}
