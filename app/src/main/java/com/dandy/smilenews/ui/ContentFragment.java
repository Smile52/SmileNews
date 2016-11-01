package com.dandy.smilenews.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandy.smilenews.R;
import com.dandy.smilenews.adapter.NewsAdapter;
import com.dandy.smilenews.config.Config;
import com.dandy.smilenews.entity.News;
import com.dandy.smilenews.net.RetrofitUtil;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Dandy on 2016/10/28.
 */

public class ContentFragment extends Fragment implements Config{

    private int mType;
    private List<News.ResultBean.DataBean> mData;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mShowNews;
    private NewsAdapter mAdapter;

    public static Fragment instance(int postion){
        ContentFragment fragment=new ContentFragment();
        Bundle bundle = new Bundle() ;
        bundle.putInt(KEY_POSTION,postion);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_content,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments() ;
        Log.e("dandy","pos "+bundle.getInt(KEY_POSTION));
        initViews(view);
        mType=bundle.getInt(KEY_POSTION);

        getData();
    }

    private void initViews(View view) {
        mRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mShowNews= (RecyclerView) view.findViewById(R.id.news_recyclerview);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.tab_select_text_color,R.color.refresh_color,R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData(){
        mRefreshLayout.setRefreshing(true);
        Subscriber<News> subscriber=new Subscriber<News>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("smile","获取失败");
            }

            @Override
            public void onNext(News news) {
                Log.e("smile","获取出来的"+news.getResult().getData().size());
                //mData=news.getResult().getData();
                setData(news);
            }
        };
        RetrofitUtil.getInstance().getNews(subscriber,mType);
    }

    private void setData(News data){
        mRefreshLayout.setRefreshing(false);
        mAdapter=new NewsAdapter(getContext(),data);
        mShowNews.setLayoutManager(new LinearLayoutManager(getContext()));
        mShowNews.setAdapter(mAdapter);
    }


}
