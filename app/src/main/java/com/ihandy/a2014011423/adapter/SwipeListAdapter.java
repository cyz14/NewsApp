package com.ihandy.a2014011423.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.activity.WebActivity;
import com.ihandy.a2014011423.model.NewsItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by cyz14 on 2016/9/5.
 * Swipe do refresh
 */
public class SwipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int PULLUP_LOAD_MORE = 1;
    public static final int LOADING_MORE = 2;
    private static final int TYPE_NORMAL_ITEM = 0;
    private static final int TYPE_FOOTER_ITEM = 1;
    private int loadMoreStatus = 0;

    private Activity activity;
    private List<NewsItem> newsList;

    public SwipeListAdapter(Activity activity, List<NewsItem> newsList) {
        this.activity = activity;
        this.newsList = newsList;
    }

    // Create new views (invoked by layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_NORMAL_ITEM) {
            View view = inflater.inflate(R.layout.news_card, parent, false);
            return new NewsCardViewHolder(view);
        } else{
            View view = inflater.inflate(R.layout.recycler_footer_view, parent, false);
            return new FooterViewHolder(view);
        }
    }

    /**
     * Replace the content of the view, invoked by a layout manager
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element

        if (holder instanceof NewsCardViewHolder) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_visibility_off)
                    .showImageForEmptyUri(R.drawable.ic_visibility_off)
                    .showImageOnFail(R.drawable.ic_visibility_off)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            NewsItem item = newsList.get(position);
            if (item == null) {
                Log.e("Null", "Null item");
            }
            NewsCardViewHolder newsCardViewHolder = (NewsCardViewHolder) holder;
            newsCardViewHolder.textView.setText(item.toString());
            String[] imageUrls = item.getImages();

            if (imageUrls == null || imageUrls[0] == null)
                Log.e("Null Image", "No image url found");
            else {
                try {
                    ImageLoader.getInstance().displayImage(imageUrls[0],
                            newsCardViewHolder.imageView, options);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            View view = newsCardViewHolder.itemView;
            NewsItem.Source source = item.getSource();
            final String title = item.getTitle();
            if (source == null) {
                Log.e("Error", "Source is null");
            }
            else {
                final String url = source.getUrl();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, WebActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("title", title);
                        activity.startActivity(intent);
                    }
                });
            }
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder viewHolder = (FooterViewHolder) holder;
            switch (loadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.textView.setText(R.string.xlistview_footer_hint_normal);
                    viewHolder.progressBar.setVisibility(ProgressBar.GONE);
                    break;
                case LOADING_MORE:
                    viewHolder.textView.setVisibility(View.GONE);
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return newsList.get(i).getNews_id();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == newsList.size()) {
            return TYPE_FOOTER_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1; // add the footer view holder
    }

    public static class NewsCardViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public NewsCardViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
            textView = (TextView) itemView.findViewById(R.id.footer_text_view);
        }
    }

    public void setLoadMoreStatus(int status) {
        loadMoreStatus = status;
        notifyDataSetChanged();
    }
}
