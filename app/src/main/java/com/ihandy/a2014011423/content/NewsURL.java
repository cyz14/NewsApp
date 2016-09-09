package com.ihandy.a2014011423.content;

/**
 * Created by cyz14 on 2016/9/2.
 */
public class NewsURL {
    private String url;
    private long maxNews;
    private String category;
    private final int MAXNEWS = 10;
    private static final String newsUrl = "http://assignment.crazz.cn/news/query?locale=en";

    NewsURL() {
        url = newsUrl;
        maxNews = 0;
    }

    NewsURL setMaxNews(long maxNews) {
        if (maxNews > 0) {
            this.maxNews = maxNews;
            url = url + "&maxnews="+maxNews;
        }
        return this;
    }

    public NewsURL setCategory(String category) {
        this.category = category;
        url = url + "&category="+category;
        return this;
    }

    public long getMaxNews() {
        return maxNews;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return url;
    }
}

