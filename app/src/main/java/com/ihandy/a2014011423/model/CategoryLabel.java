package com.ihandy.a2014011423.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyz14 on 2016/9/4.
 */
public class CategoryLabel implements Comparable {
    private String label;   // key in json
    private String title;   // value in json

    public CategoryLabel(String label, String title) {
        this.label = label;
        this.title = title;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public String toJSON() {
        JSONObject object = null;
        try {
            object = new JSONObject()
                    .put("label", label)
                    .put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Failed to generate json string");
        }
        return object.toString();
    }

    public static CategoryLabel fromJSON(String json) {
        CategoryLabel label = new CategoryLabel(null, null);
        try {
            JSONObject object = new JSONObject(json);
            label.setLabel(object.getString("label"));
            label.setTitle(object.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public int compareTo(Object o) {
        CategoryLabel label = (CategoryLabel)o;
        return getLabel().compareTo(label.getLabel());
    }
}
