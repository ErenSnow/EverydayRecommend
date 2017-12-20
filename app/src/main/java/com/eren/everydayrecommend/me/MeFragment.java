package com.eren.everydayrecommend.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.collect.CollectActivity;
import com.eren.everydayrecommend.me.model.VideoModel;
import com.leon.lib.settingview.LSettingItem;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment {

    private LSettingItem mLSettingItemVersion;
    private LSettingItem mLSettingItemSave;
    private RecyclerView mRecyclerViewVideos;
    private List<VideoModel> mList;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLSettingItemVersion = (LSettingItem) view.findViewById(R.id.setting_version);
        mLSettingItemSave = (LSettingItem) view.findViewById(R.id.setting_save);
        mRecyclerViewVideos = (RecyclerView) view.findViewById(R.id.me_list_vedio);
        initData();
        //创建适配器
        MeAdapter meAdapter = new MeAdapter(mContext, mList);
        mRecyclerViewVideos.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerViewVideos.setAdapter(meAdapter);
        mRecyclerViewVideos.setNestedScrollingEnabled(false);
        initListener();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new VideoModel(R.drawable.classes_01, "http://www.kgc.cn/android/29965.shtml"));
        mList.add(new VideoModel(R.drawable.classes_02, "http://www.kgc.cn/android/29917.shtml"));
        mList.add(new VideoModel(R.drawable.classes_03, "http://www.kgc.cn/android/29673.shtml"));
        mList.add(new VideoModel(R.drawable.classes_04, "http://www.kgc.cn/android/29661.shtml"));
        mList.add(new VideoModel(R.drawable.classes_05, "http://www.kgc.cn/android/29545.shtml"));
        mList.add(new VideoModel(R.drawable.classes_06, "http://www.kgc.cn/android/29317.shtml"));
        mList.add(new VideoModel(R.drawable.classes_07, "http://www.kgc.cn/android/29105.shtml"));
        mList.add(new VideoModel(R.drawable.classes_08, "http://www.kgc.cn/android/28881.shtml"));
        mList.add(new VideoModel(R.drawable.classes_09, "http://www.kgc.cn/android/28737.shtml"));
        mList.add(new VideoModel(R.drawable.classes_10, "http://www.kgc.cn/android/28337.shtml"));
        mList.add(new VideoModel(R.drawable.classes_11, "http://www.kgc.cn/android/27899.shtml"));
        mList.add(new VideoModel(R.drawable.classes_12, "http://www.kgc.cn/android/27785.shtml"));
        mList.add(new VideoModel(R.drawable.classes_13, "http://www.kgc.cn/android/22900.shtml"));
    }

    private void initListener() {
        mLSettingItemVersion.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                ToastUtils.showShort("当前已经是最新版本");
            }
        });
        mLSettingItemSave.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                startActivity(new Intent(mContext, CollectActivity.class));
            }
        });
    }
}
