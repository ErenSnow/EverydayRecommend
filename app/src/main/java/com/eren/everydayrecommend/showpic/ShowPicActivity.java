package com.eren.everydayrecommend.showpic;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.base.BaseActivity;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.image.ImageManager;
import com.eren.everydayrecommend.net.Api;
import com.eren.everydayrecommend.net.HttpManager;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ShowPicActivity extends BaseActivity {
    private View mView;
    private List<String> mListPics;
    private int mPosition;
    private ViewPager mViewPager;
    private int mPage;
    private PicAdapter mPicAdapter;

    @Override
    protected void initOptions() {
        //获取传递的参数
        mListPics = getIntent().getStringArrayListExtra("picList");
        mPosition = getIntent().getIntExtra("position", 0);
        mPage = getIntent().getIntExtra("page", 0);
        mViewPager = (ViewPager) mView.findViewById(R.id.pic_viewpager);
        //给viewpager设置适配器
        mPicAdapter = new PicAdapter(this, mListPics);
        mViewPager.setAdapter(mPicAdapter);
        //设置当前选中项
        mViewPager.setCurrentItem(mPosition);
        //给VIewpage添加监听器，当viewpager滑动到最后一个界面的时候自动加载更多数据
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mListPics.size() - 1) {
                    //加载更多数据
                    mPage++;
                    loadMoreData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 当前展示到最后自动加载更多数据
     */
    private void loadMoreData() {
        Api api = HttpManager.getInstance().getApiService();
        api.getCategoryData(Constant.CATEGORY_GIRL, Constant.PAGE_SIZE, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GankModel gankModel) {
                        if (gankModel.getError()) {
                            ToastUtils.showShort("服务端异常，请稍后再试");
                            return;
                        }
                        //加载更多模式
                        for (int i = 0; i < gankModel.getResults().size(); i++) {
                            mListPics.add(gankModel.getResults().get(i).getUrl());
                        }
                        mPicAdapter.setmList(mListPics);
                        mPicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected View initContentView() {
        mView = View.inflate(this, R.layout.activity_show_pic, null);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return "图片详情";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_save).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                startShareIntent("text/plain", "分享福利啦：" + mListPics.get(mViewPager.getCurrentItem()));
                break;
            case R.id.action_download:
                downLoadImg();
                break;
        }
        return true;
    }

    /**
     * 下载图片到本地，使用glide实现
     */
    private void downLoadImg() {
        if (!SDCardUtils.isSDCardEnable()) {
            ToastUtils.showShort("当前sd卡不可用，取消保存");
            return;
        }
        Observable
                .create(new ObservableOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                        Bitmap bitmap = ImageManager.getInstance()
                                .loadImage(ShowPicActivity.this, mListPics.get(mViewPager.getCurrentItem()));
                        e.onNext(bitmap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Bitmap, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Bitmap bitmap) throws Exception {
                        return saveImg(bitmap);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ToastUtils.showShort("图片保存成功");
                        } else {
                            ToastUtils.showShort("图片保存失败");
                        }
                    }
                });
    }

    /**
     * 保存bitmap为本地图片
     *
     * @param bitmap
     * @return
     */
    public boolean saveImg(Bitmap bitmap) {
        //保存路径
        String directoryPath = SDCardUtils.getSDCardPath() + "meiriyijian";
        FileUtils.createOrExistsDir(directoryPath);
        String temp = mListPics.get(mViewPager.getCurrentItem());
        String fileName = temp.substring(temp.lastIndexOf("/"));
        String savePath = directoryPath + File.separator + fileName;
        return ImageUtils.save(bitmap, savePath, Bitmap.CompressFormat.JPEG);
    }
}
