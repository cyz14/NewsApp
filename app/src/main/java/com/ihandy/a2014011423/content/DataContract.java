package com.ihandy.a2014011423.content;

import android.provider.BaseColumns;

/**
 * Created by cyz14 on 2016/9/10.
 *
 */
public final class DataContract {
    private DataContract() {
    }

    public static class DataEntry implements BaseColumns {
        public static final String SOURCE_URL = "source_url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_CATEGORY = "category";
    }
}
