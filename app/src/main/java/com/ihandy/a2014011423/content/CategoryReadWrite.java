package com.ihandy.a2014011423.content;

import android.util.Log;

import com.ihandy.a2014011423.model.CategoryLabel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyz14 on 2016/9/9.
 */
public class CategoryReadWrite {
    public static final String watchFileName = "watch.txt";
    public static final String unwatchFileName = "unwatch.txt";
    public static String FILESDIR = "/data/data/com.ihandy.a2014011423/";
    private static List<CategoryLabel> watchedList;
    private static List<CategoryLabel> unwatchedList;

    // first read from file, if  none, fetch from server via CategoryFetcher
    public static List<CategoryLabel> getWatchedList() {
        if (watchedList != null) return watchedList;
        List<CategoryLabel> labels = readLabelsFromFile(watchFileName);
        if (labels == null || labels.size() == 0) {
            Log.d("Fetch", "First list");
            watchedList = CategoryFetcher.fetchCategory();
        } // else
        else watchedList = labels;
        for (int i = 0; i < labels.size(); i++)
            Log.d("watched", labels.get(i).getLabel());
        return watchedList;
    }

    public static List<CategoryLabel> getUnwatchedList() {
        if (unwatchedList != null) return unwatchedList;
        List<CategoryLabel> labels = readLabelsFromFile(unwatchFileName);
        if (labels == null) {
            labels = new ArrayList<>();
        }
        unwatchedList = labels;
        return unwatchedList;
    }

    public static void writeLabelsToFile(String filename, List<CategoryLabel> data) {
        try {
            File file = new File(FILESDIR + filename);
            if (!file.exists()) {
                Log.d("file", "Not exist " + filename);
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            for (int i = 0; i < data.size(); i++) {
                String temp = data.get(i).toJSON();
                bufferedWriter.write(temp, 0, temp.length());
                bufferedWriter.newLine();
                Log.d("Write", temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CategoryLabel> readLabelsFromFile(String filename) {
        List<CategoryLabel> labels = new ArrayList<>();
        Log.d("Reading", "Read " + FILESDIR + filename);

        try {
            File file = new File(FILESDIR + filename);
            if (!file.exists()) {
                Log.e("reading", "File not exists " + filename);
                if (filename.equals(watchFileName))
                    labels = CategoryFetcher.fetchCategory();
                else {
                    labels = new ArrayList<>();
                }
                return labels;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(FILESDIR + filename));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                CategoryLabel label = CategoryLabel.fromJSON(line);
                Log.d("Reading", line);
                labels.add(label);
            }
            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labels;
    }


}
