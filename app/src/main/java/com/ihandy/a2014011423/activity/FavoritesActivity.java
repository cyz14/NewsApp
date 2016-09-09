package com.ihandy.a2014011423.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.adapter.FavoritesViewAdapter;
import com.ihandy.a2014011423.model.NewsItem;

public class FavoritesActivity extends AppCompatActivity {

    NewsItem[] data;
    private LinearLayout layout;
    private RecyclerView recyclerView;

    public void getData() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getData();

        FavoritesViewAdapter adapter = new FavoritesViewAdapter(this, data);
        recyclerView = (RecyclerView) findViewById(R.id.favorites_list);
        recyclerView.setAdapter(adapter);
    }

}
