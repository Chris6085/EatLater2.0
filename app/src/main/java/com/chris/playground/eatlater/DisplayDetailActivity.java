package com.chris.playground.eatlater;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayDetailActivity extends AppCompatActivity {

    private static final String TAG = "DisplayDetailActivity";
    private static final int READ_EXTERNAL_STORAGE_PERMISSION = 101;
    private static final String ID_KEY = "id_key";
    private String mId;
    private ArrayList<String> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        mId = String.valueOf(getIntent().getLongExtra(ID_KEY, -1));
        initTextUI();

        // Display the related photo if available
        if (ContextCompat.checkSelfPermission(DisplayDetailActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DisplayDetailActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
        } else {
            initPhotoGridUI();
        }
    }

    private void initTextUI() {
        EditText titleText = (EditText) findViewById(R.id.restaurant_title);
        EditText noteText = (EditText) findViewById(R.id.restaurant_note);

        Cursor c = getContentResolver().query(RestaurantEntry.CONTENT_URI,
                new String[]{RestaurantEntry.TITLE, RestaurantEntry.NOTE},
                RestaurantEntry._ID + " = ?",
                new String[]{mId},
                RestaurantEntry.DEFAULT_SORT_ORDER);

        if (null != c && c.moveToFirst()) {
            titleText.setText(c.getString(c.getColumnIndex(RestaurantEntry.TITLE)));
            noteText.setText(c.getString(c.getColumnIndex(RestaurantEntry.NOTE)));

            c.close();
        }
    }

    private void initPhotoGridUI() {
        Cursor c = getContentResolver().query(RestaurantEntry.CONTENT_URI,
                new String[]{RestaurantEntry.PHOTOS_URI},
                RestaurantEntry._ID + " = ?",
                new String[]{mId},
                RestaurantEntry.DEFAULT_SORT_ORDER);

        if (null != c && c.moveToFirst()) {
            String photo_uris = c.getString(c.getColumnIndex(RestaurantEntry.PHOTOS_URI));

            if (null != photo_uris) {
                mImages = new ArrayList<>(Arrays.asList(photo_uris.split(",")));
                GridView gallery = (GridView) findViewById(R.id.gridView);
                gallery.setAdapter(new ImageAdapter(this, R.layout.grid_item_layout, mImages));
                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        if (null != mImages && !mImages.isEmpty()) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "position " + position + " " + mImages.get(position),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            c.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initPhotoGridUI();
                } else {
                    Log.d(TAG, "permission denied");
                }
            }
        }
    }


}
