package com.chris.playground.eatlater;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

public class SearchableActivity extends ListActivity {

    private static final String TAG = "SearchableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri detailUri = intent.getData();
            Long id = Long.parseLong(detailUri.getLastPathSegment());
            Intent detailsIntent = new Intent(getApplicationContext(), DisplayDetailActivity.class);
            detailsIntent.putExtra("id_key", id);
            startActivity(detailsIntent);
            finish();
        }
    }

    private void showResults(String query) {
        Cursor c = getContentResolver().query(RestaurantEntry.CONTENT_URI,
                new String[]{RestaurantEntry.TITLE, RestaurantEntry._ID},
                RestaurantEntry.TITLE + " = ?",
                new String[]{query},
                RestaurantEntry.DEFAULT_SORT_ORDER);

        setListAdapter(new SearchListAdapter(this, c, false));
    }
}
