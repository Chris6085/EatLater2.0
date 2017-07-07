package com.chris.playground.eatlater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chris.playground.eatlater.database.InitJsonReaderService;
import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "EatLater_MAIN_ACT";
    private static final String SHARE_PREF_FILE = "EatLater";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final int LOADER = 0;

    private CustomCursorRecyclerViewAdapter mAdapter;
    private TextView mTextMessage;
    private Context mContext;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database if needed.
        mContext = getApplicationContext();
        SharedPreferences sharePref = mContext
                .getSharedPreferences(SHARE_PREF_FILE, Context.MODE_PRIVATE);
        if (sharePref.getBoolean(KEY_FIRST_LAUNCH, true)) {
            // Start the init service just once.
            sharePref.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
            mContext.startService(new Intent(mContext, InitJsonReaderService.class));
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.restaurant_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer.column_count)));
        mAdapter = new CustomCursorRecyclerViewAdapter(this, null);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the new record
                startActivity(new Intent(MainActivity.this, NewRestaurantActivity.class));
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER, null, this);
    }

    // Get the all columns
    static final String[] RESTAURANTS_PROJECTION = new String[] {
            RestaurantEntry._ID,
            RestaurantEntry.TITLE,
            RestaurantEntry.NOTE,
            RestaurantEntry.PHOTOS_URI
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {;
        return new CursorLoader(getApplicationContext(), RestaurantEntry.CONTENT_URI,
                RESTAURANTS_PROJECTION, null, null, RestaurantEntry.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

}
