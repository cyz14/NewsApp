package com.ihandy.a2014011423.content;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.ihandy.a2014011423.model.CategoryLabel;
import com.ihandy.a2014011423.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cyz on 8/31/16.
 * Fetch category json data from server in background.
 */
public class CategoryFetcher {
    private static final String categoryUrl = "http://assignment.crazz.cn/news/en/category?timestamp=";

    private static long time = 0;
    private static List<CategoryLabel> allCategories = new ArrayList<>();

    CategoryFetcher() {
    }

    /**
     * Fetch categories from server when creating main activity
     */
    public static List<CategoryLabel> fetchCategory() {
        Thread cateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                time = System.currentTimeMillis();
                // Log.i("time", String.valueOf(time));

                HttpRequest httpRequest = HttpRequest.get(categoryUrl + time);
                String response = httpRequest.body();
//                Log.i("Returned Json", response);

                try {
                    allCategories = parseJsonOld(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        allCategories = parseJsonNew(response);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        cateThread.start();

        try {
            cateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(allCategories);
        return allCategories;
    }

    private static List<CategoryLabel> parseJsonOld(String response) throws JSONException {
        List<CategoryLabel> tempLabels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject dataObject = jsonObject
                    .getJSONObject("data")
                    .getJSONObject("categories");
            JSONArray jsonArray = dataObject.names();
            for (int i = 0; i < jsonArray.length(); i++) {
                String key = jsonArray.get(i).toString();
                // Log.i("json cate", key +": "+dataObject.getString(key));
                CategoryLabel label = new CategoryLabel(key, dataObject.getString(key));
                tempLabels.add(label);
            }
        } catch (JSONException e) {
            Log.e("JSON Error", e.getMessage());
            throw e;
        }
        return tempLabels;
    }

    private static List<CategoryLabel> parseJsonNew(String response) throws JSONException {
        List<CategoryLabel> tempLabels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject
                    .getJSONObject("data")
                    .getJSONArray("categories");
            for (int i = 0; i < jsonArray.length(); i++) {
                String key = jsonArray.get(i).toString();
                // Log.i("json cate", key +": "+dataObject.getString(key));
                CategoryLabel label = new CategoryLabel(key, key.replace("_", " ").toUpperCase());
                tempLabels.add(label);
            }
        } catch (JSONException e) {
            Log.e("JSON Error", e.getMessage());
            throw e;
        }
        return tempLabels;
    }

    public static List<NewsItem> getNewsOfCategory(final String category) {
        final String newsUrl = new NewsURL().setCategory(category).toString();

        NewsItemsThread newsThread = new NewsItemsThread(newsUrl);
        newsThread.start();
        try {
            newsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return newsThread.getItems();
    }

    public static List<NewsItem> getMoreNewsOfCategoryBefore(final String category, NewsItem item) {
        final String url = new NewsURL().setCategory(category).setMaxNews(item.getNews_id()).toString();
        NewsItemsThread thread = new NewsItemsThread(url);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return thread.getItems();
    }

    public static List<NewsItem> getNewsItemsFromJSON(String json) {
        List<NewsItem> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(json)
                    .getJSONObject("data")
                    .getJSONArray("news");
            if (jsonArray.length() == 0) {
                Log.e("Error", "No news got");
                return items;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                NewsItem temp = NewsItem.FromJSON(obj);
                Log.d("news", temp.getTitle());
//              if (temp.getSource() != null)
                items.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Return the time of last fetchCategory operation
     */
    public static long getLastTime() {
        if (time == 0)
            time = System.currentTimeMillis();
        return time;
    }

    private static class NewsItemsThread extends Thread {
        List<NewsItem> items;
        private String url;

        NewsItemsThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            HttpRequest httpRequest = HttpRequest.get(url);
            String response = httpRequest.body();

            items = getNewsItemsFromJSON(response);
        }

        public List<NewsItem> getItems() {
            return items;
        }
    }
}
