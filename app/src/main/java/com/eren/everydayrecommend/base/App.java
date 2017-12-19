package com.eren.everydayrecommend.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import io.realm.Realm;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //工具类初始化
        Utils.init(this);
        //初始化数据库
        Realm.init(this);
    }
}