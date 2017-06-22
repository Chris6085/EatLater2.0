package com.chris.playground.eatlater.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

public class RestaurantsContentProvider extends ContentProvider {
    private static final String TAG = "RestaurantsContentProvider";

    private static final String DATABASE_NAME = "Restaurants.db";
    private static final int DATABASE_VERSION = 1;

    private static final int RESTAURANTS = 100;
    private static final int RESTAURANT_ID = 101;

    private static final UriMatcher sUriMatcher;
    private static final HashMap<String, String> sSettingsProjectionMap;
    private static final String ID_SELECTION = RestaurantEntry._ID + "=";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(RestaurantsContract.AUTHORITY, RestaurantsContract.PATH_RESTAURANTS, RESTAURANTS);
        sUriMatcher.addURI(RestaurantsContract.AUTHORITY, RestaurantsContract.PATH_RESTAURANTS + "/#",
                RESTAURANT_ID);

        sSettingsProjectionMap = new HashMap<>();
        sSettingsProjectionMap.put(RestaurantEntry._ID, RestaurantEntry._ID);
        sSettingsProjectionMap.put(RestaurantEntry.TITLE, RestaurantEntry.TITLE);
        sSettingsProjectionMap.put(RestaurantEntry.NOTE, RestaurantEntry.NOTE);
    }

    private ContentResolver mResolver;

    public RestaurantsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DatabaseHelper.getDatabase(getContext());
        int count;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case RESTAURANTS: {
                count = db.delete(RestaurantEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case RESTAURANT_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(RestaurantEntry.TABLE_NAME,
                        ID_SELECTION + id +
                                (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""),
                        selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        mResolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case RESTAURANTS:
                // directory
                return "vnd.android.cursor.dir" + "/" + RestaurantsContract.AUTHORITY
                        + "/" + RestaurantsContract.PATH_RESTAURANTS;
            case RESTAURANT_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + RestaurantsContract.AUTHORITY
                        + "/" + RestaurantsContract.PATH_RESTAURANTS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = DatabaseHelper.getDatabase(getContext());
        Uri newUri;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANTS:
                long id = db.insert(RestaurantEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    newUri = ContentUris.withAppendedId(uri, id);
                } else  {
                    throw new SQLiteException("Unable to insert " + values + " for " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        mResolver.notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public boolean onCreate() {
        try {
            mResolver = getContext().getContentResolver();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = DatabaseHelper.getDatabase(getContext());
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANTS: {
                sqlBuilder.setTables(RestaurantEntry.TABLE_NAME);
                sqlBuilder.setProjectionMap(sSettingsProjectionMap);
                break;
            }
            case RESTAURANT_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(RestaurantEntry.TABLE_NAME);
                sqlBuilder.setProjectionMap(sSettingsProjectionMap);
                sqlBuilder.appendWhere(ID_SELECTION + id);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        String order = TextUtils.isEmpty(sortOrder)
                ? RestaurantEntry.DEFAULT_SORT_ORDER : sortOrder;

        // Get the database and run the query
        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null
                , order);
        // Tell the cursor which uri to watch, so it knows when its source data changes
        cursor.setNotificationUri(mResolver, uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = DatabaseHelper.getDatabase(getContext());
        int count;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANTS: {
                count = db.update(RestaurantEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case RESTAURANT_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(RestaurantEntry.TABLE_NAME,
                        values,
                        ID_SELECTION + id +
                                (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""),
                        selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        mResolver.notifyChange(uri, null);

        return count;
    }

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static SQLiteDatabase database;
        private static final String CREATE_TABLE =
                "CREATE TABLE " + RestaurantEntry.TABLE_NAME + " (" +
                        RestaurantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RestaurantEntry.TITLE + " TEXT NOT NULL, " +
                        RestaurantEntry.NOTE + " TEXT NOT NULL)";

        DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        public static SQLiteDatabase getDatabase(Context context) {
            if (database == null || !database.isOpen()) {
                database = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
                        .getWritableDatabase();
            }
            return database;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + RestaurantEntry.TABLE_NAME);
            onCreate(db);
        }
    }
}
