package com.ihandy.a2014011423.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.activity.CategorySettingActivity;
import com.ihandy.a2014011423.model.CategoryLabel;

import java.util.List;

/**
 * Created by cyz14 on 2016/9/8.
 *
 */
public class RowDeleteAdapter extends BaseAdapter { // watched
    private LayoutInflater inflater;
    private List<CategoryLabel> data;
    private Handler handler;

    public RowDeleteAdapter(Context context, Handler handler, List<CategoryLabel> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CateViewHolder viewHolder = null;
        final CategoryLabel label = data.get(i);
        if (view == null) {
            viewHolder = new CateViewHolder();
            view = inflater.inflate(R.layout.category_delete_row, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.category_label);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.category_add);
            viewHolder.imageView.setOnClickListener(new ArrowOnClickListener(label));
            view.setTag(viewHolder);
        } else {
            viewHolder = (CateViewHolder) view.getTag();
        }
        viewHolder.textView.setText(data.get(i).getTitle());
        return view;
    }

    class CateViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

    private class ArrowOnClickListener implements View.OnClickListener {
        private CategoryLabel label;

        public ArrowOnClickListener(CategoryLabel label) {
            this.label = label;
        }

        @Override
        public void onClick(View view) {
            Message msg = new Message();
            msg.arg1 = CategorySettingActivity.ARROW_DOWN;
            Bundle bundle = new Bundle();
            bundle.putString("label", label.getLabel());
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
}