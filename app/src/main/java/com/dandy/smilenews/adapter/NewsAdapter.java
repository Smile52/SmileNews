package com.dandy.smilenews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private final static String TAG="love";
    private boolean mIsScroll=true;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTitle.setText(mData.get(position).getTitle());
        holder.mDate.setText(mData.get(position).getDate());

            Picasso.with(mContext).load(mData.get(position).getThumbnail_pic_s()).into(holder.mImg);


        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0:mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnScrollListener(RecyclerView recyclerView){
        recyclerView.setOnScrollListener(new AutoLoadScrollListener());
    }


    class AutoLoadScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState){
                case 0:
                    Log.e(TAG,"屏幕停止滚动");
                    mIsScroll=true;
                    break;
                case 1:
                    Log.e(TAG,"屏幕在滚动 且 用户仍在触碰或手指还在屏幕上");
                    mIsScroll=false;
                    break;

                case 2:
                    Log.e(TAG,"随用户的操作，屏幕上产生的惯性滑动");
                    break;
                default:
                    break;
            }
        }
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
