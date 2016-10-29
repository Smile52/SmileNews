package com.dandy.smilenews.net;

import com.dandy.smilenews.entity.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dandy on 2016/10/27.
 */

public class RetrofitUtil {
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private NewService newService;

    private RetrofitUtil(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                //配置Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        retrofit=new Retrofit.Builder()
                .baseUrl(NewService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        newService=retrofit.create(NewService.class);

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RetrofitUtil INSTANCE = new RetrofitUtil();
    }

    public static RetrofitUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getNews(Subscriber<News> newsSubscriber){
        newService.getNews(getParams())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsSubscriber);

    }


    private Map<String,String> getParams(){
        Map<String,String> map=new HashMap<>();
        map.put("type","shishang");
        map.put("key","0489bcea378ce792facda791d0f1e188");
        return map;
    }
}

