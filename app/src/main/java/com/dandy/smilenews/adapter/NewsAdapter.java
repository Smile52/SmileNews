package com.dandy.smilenews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandy.smilenews.R;
import com.dandy.smilenews.entity.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dandy on 2016/10/28.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context mContext;
    private News mNews;
    private LayoutInflater mInflater;
    private List<News.ResultBean.DataBean> mData;
    public NewsAdapter(Context mContext, News mNews) {
        this.mContext = mContext;
        this.mNews = mNews;
        mData=mNews.getResult().getData();

        mInflater= LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTitle.setText(mData.get(position).getTitle());
        holder.mDate.setText(mData.get(position).getDate());
        Picasso.with(mContext).load(mData.get(position).getThumbnail_pic_s()).into(holder.mImg);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0:mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mDate;
        ImageView mImg;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitle= (TextView) itemView.findViewById(R.id.item_news_title_tv);
            mDate= (TextView) itemView.findViewById(R.id.item_news_date_tv);
            mImg= (ImageView) itemView.findViewById(R.id.item_news_img);
        }
    }
}
