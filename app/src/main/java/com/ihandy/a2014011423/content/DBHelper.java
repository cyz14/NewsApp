package com.ihandy.a2014011423.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ihandy.a2014011423.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by cyz14 on 2016/9/10.
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DB_NEWS.db";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_FAVORITES = "favorites";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SOURCE_URL = "source_url";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CATEGORY = "category";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "create table "+ TABLE_CATEGORIES+
            " (title text primary key, source_url text, image text, category text) ");
        sqLiteDatabase.execSQL(
                "create table "+ TABLE_FAVORITES+
                        " (title text primary key, source_url text, image text, category text) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insertNews(NewsItem item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", item.getTitle());
        contentValues.put("source_url", item.getSource().getUrl());
        contentValues.put("image", item.getImages()[0]);
        contentValues.put("category", item.getCategory());
        sqLiteDatabase.insert("favorites", null, contentValues);
        return true;
    }

    public int deleteNews(NewsItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORITES, "title = ?", new String[] {item.getTitle()});
    }

    public ArrayList<NewsItem> getAllNews() {
        ArrayList<NewsItem> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_FAVORITES, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String source = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String source_url = cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE_URL));
            NewsItem item = new NewsItem();
            NewsItem.Source source1 = new NewsItem.Source();
            source1.setUrl(source_url);
            item.setSource(source1);
            arrayList.add(item);
            cursor.moveToNext();
        }
        return arrayList;
    }
}
