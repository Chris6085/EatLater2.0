package com.chris.playground.eatlater.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class RestaurantsContract {

    private RestaurantsContract() {}

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.chris.playground";
    public static final String PATH_RESTAURANTS = "restaurants";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    /* Inner class that defines the table contents */
    public static abstract class RestaurantEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANTS).build();

        public static final String TABLE_NAME = "restaurants";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String TITLE = "title";
        public static final String NOTE = "note";
//        public static final String COLUMN_TEL = "tel";
//        public static final String COLUMN_NAME_ASSOCIATE_DIARY = "associate_diary";
//        public static final String COLUMN_NAME_IMAGE_FILE = "image_file_name";
//        public static final String COLUMN_LATITUDE = "latitude";
//        public static final String COLUMN_LONGITUDE = "longitude";
//        public static final String COLUMN_EATEN_FLAG = "eaten_flag";

    }
}
