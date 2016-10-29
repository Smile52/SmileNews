package com.dandy.smilenews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dandy.smilenews.R;

/**
 * Created by Dandy on 2016/10/28.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
