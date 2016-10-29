package com.dandy.smilenews.net;

import com.dandy.smilenews.entity.News;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Dandy on 2016/10/27.
 */

public interface NewService {
    String BASE_URL="http://v.juhe.cn/";

    @GET("toutiao/index?")
    Observable<News> getNews(@QueryMap Map<String,String> map);
}
