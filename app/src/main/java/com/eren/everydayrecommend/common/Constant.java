package com.eren.everydayrecommend.common;

/**
 * 常量
 */
public class Constant {
    public static String BASE_URL = "http://gank.io/api/";
    public static String BASE_URL_READ = "";

    public static String CATEGORY_ALL = "all";
    public static String CATEGORY_Android = "Android";
    public static String CATEGORY_VIDEO = "休息视频";
    public static String CATEGORY_EXPANDRESOURCE = "拓展资源";
    public static String CATEGORY_CLIENT = "前端";
    public static String CATEGROY_RECOMMEND = "瞎推荐";
    public static String CATEGORY_APP = "App";
    public static String CATEGORY_IOS = "iOS";
    public static String CATEGORY_GIRL = "福利";

    //每次请求大小
    public static final int PAGE_SIZE = 10;
    //更新数据类型 0:正常加载、下拉刷新   1: 加载更多
    public static final int GET_DATA_TYPE_NOMAL = 0;
    public static final int GET_DATA_TYPE_LOADMORE = 1;

}
