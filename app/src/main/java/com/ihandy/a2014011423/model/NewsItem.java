package com.ihandy.a2014011423.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyz14 on 2016/9/4.
 * To save json data of a piece of news
 */
public class NewsItem {
    private String category;
    private String[] images;
    private long news_id;
    private Source source;
    private String title;

    public NewsItem() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public long getNews_id() {
        return news_id;
    }

    public void setNews_id(long news_id) {
        this.news_id = news_id;
    }

    public Source getSource() {
        return source;
    }

    public String getUrl() {
        return source.getUrl();
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setSourceUrl(String url) {
        if (source == null) source = new Source();
        source.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static NewsItem FromJSON(JSONObject jsonObject) {
        NewsItem item = new NewsItem();
        try
        {
            item.setCategory(jsonObject.getString("category"));
            item.setTitle(jsonObject.getString("title"));
            item.setNews_id(Long.parseLong(jsonObject.getString("news_id")));
            JSONObject object = jsonObject.getJSONObject("source");
            if (object != null) {
                Source source = Source.FromJson(object);
                if (source != null)
                    item.setSource(source);
            } else {
                Log.e("Null", "Source is null");
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonImages = jsonObject.getJSONArray("imgs");
            if (jsonImages.length() == 0) {
                Log.e("json", "no images got from "+ jsonObject.toString().replace(" ", ""));
            }
            String[] images = new String[jsonImages.length()];
            for (int i = 0; i < images.length; i++) {
                images[i] = jsonImages.getJSONObject(i).optString("url");
                Log.d("Url", images[i]);
            }
            item.setImages(images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public String toString() {
        return getTitle() == null ? "null" : getTitle();
    }

    public static class Source {
        private String name;
        private String url;
        public Source() {}

        public Source(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public static Source FromJson(JSONObject jsonObject) {
            Source source = new Source("hello", "www.google.com"); // default

            try {

                String name = jsonObject.getString("name");
                if (name != null)
                    source.setName(name);
                String url = jsonObject.getString("url");

                if (url!=null)
                    source.setUrl(url);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return source;
        }
        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
