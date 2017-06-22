package com.chris.playground.eatlater.database;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class InitJsonReaderService extends IntentService{

    private static final boolean DEBUG = true;
    private static final String TAG = "InitJsonReaderService";
    private static final String INIT_JSON_FILE_NAME = "init_restaurant.json";
    private static final String RESTAURANTS_LIST = "restaurants";
    private static final String RESTAURANT_TITLE = "title";
    private static final String RESTAURANT_NOTE = "note";

    public InitJsonReaderService() {
        super("InitJsonReaderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (DEBUG) {
            Log.d(TAG, "onHandleIntent");
        }
        createInitialDbContentFromFile(INIT_JSON_FILE_NAME);
    }

    private void createInitialDbContentFromFile(String fileName) {
        ContentValues contentValues = new ContentValues();
        try {
            String jsonString = loadJSONFromAsset(fileName);
            if (null != jsonString) {
                // Parse the structure of JSON file.
                JSONObject rootJObject = new JSONObject(jsonString);
                JSONArray restaurant_list = rootJObject.getJSONArray(RESTAURANTS_LIST);
                JSONObject restaurant;

                for (int i = 0; i < restaurant_list.length(); i++) {
                    // Load object
                    restaurant = restaurant_list.getJSONObject(i);

                    contentValues.put(RestaurantsContract.RestaurantEntry.TITLE,
                            restaurant.getString(RESTAURANT_TITLE));
                    contentValues.put(RestaurantsContract.RestaurantEntry.NOTE,
                            restaurant.getString(RESTAURANT_NOTE));

                    getContentResolver().insert(RestaurantsContract.RestaurantEntry.CONTENT_URI,
                            contentValues);
                    contentValues.clear();
                }
            } else {
                Log.e(TAG, "load json file error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json;
        AssetManager assetManager = getAssets();
        if (DEBUG) {
            Log.d(TAG, "loadJSONFromAsset");
        }
        try {
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
