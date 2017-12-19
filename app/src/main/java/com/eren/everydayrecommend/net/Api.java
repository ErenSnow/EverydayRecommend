package com.eren.everydayrecommend.net;

import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.read.model.ReadModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by smartOrange_5 on 2017/11/23.
 * <p>
 * API接口
 */

public interface Api {
    /**
     * 获取分类数据
     * 示例：http://gank.io/api/data/Android/10/1
     *
     * @param category 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param pageSize 请求个数： 数字，大于0
     * @param page     第几页：数字，大于0
     * @return
     */
    @GET("data/{category}/{pageSize}/{page}")
    Observable<GankModel> getCategoryData(@Path("category") String category,
                                          @Path("pageSize") int pageSize,
                                          @Path("page") int page);

    /**
     * Read数据
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET("api/v1/info/{page}/{pageSize}")
    Observable<ReadModel> getReadData(@Path("page") int page,
                                      @Path("pageSize") int pageSize);

    /**
     * 搜索数据
     *
     * @param searchkeyword
     * @param page
     * @return
     */
    @GET("search/query/{searchkeyword}/category/all/count/10/page/{page} ")
    Observable<GankModel> getSearchData(@Path("searchkeyword") String searchkeyword,
                                        @Path("page") int page);

}
