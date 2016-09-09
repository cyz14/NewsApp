package com.ihandy.a2014011423.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.activity.WebActivity;
import com.ihandy.a2014011423.model.NewsItem;

/**
 * Created by cyz14 on 2016/9/5.
 * Render favorites page
 */
public class FavoritesViewAdapter extends RecyclerView.Adapter<FavoritesViewAdapter.MyViewHolder> {

    private Activity activity;
    private NewsItem[] mData;

    public FavoritesViewAdapter(Activity activities, NewsItem[] mData) {
        this.activity = activities;
        this.mData = mData;
    }

    @Override
    public FavoritesViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_card, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoritesViewAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(mData[position].getTitle());
        holder.url = mData[position].getUrl();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", holder.url);
                intent.putExtra("title", holder.textView.getText());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public String url;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
