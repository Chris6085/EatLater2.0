package com.chris.playground.eatlater;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RestaurantListAdapter adapter;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // TODO test data
        List<RestaurantEntry> Restaurants;
        Restaurants = new ArrayList<>();
        Restaurants.add(new RestaurantEntry("Emma Wilson", "123"));
        Restaurants.add(new RestaurantEntry("Lavery Maiss", "456"));
        Restaurants.add(new RestaurantEntry("Lillie Watts", "789"));
        Restaurants.add(new RestaurantEntry("Sylvia R", "789"));
        Restaurants.add(new RestaurantEntry("Lillie Little", "9487"));
        Restaurants.add(new RestaurantEntry("Ed Sheeran", "XXX"));
        Restaurants.add(new RestaurantEntry("Emma Wilson", "123"));
        Restaurants.add(new RestaurantEntry("Lavery Maiss", "456"));
        Restaurants.add(new RestaurantEntry("Lillie Watts", "789"));
        Restaurants.add(new RestaurantEntry("Sylvia R", "789"));
        Restaurants.add(new RestaurantEntry("Lillie Little", "9487"));
        Restaurants.add(new RestaurantEntry("Ed Sheeran", "XXX"));
        Restaurants.add(new RestaurantEntry("Emma Wilson", "123"));
        Restaurants.add(new RestaurantEntry("Lavery Maiss", "456"));
        Restaurants.add(new RestaurantEntry("Lillie Watts", "789"));
        Restaurants.add(new RestaurantEntry("Sylvia R", "789"));
        Restaurants.add(new RestaurantEntry("Lillie Little", "9487"));
        Restaurants.add(new RestaurantEntry("Ed Sheeran", "XXX"));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.restaurant_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer.column_count)));
        adapter = new RestaurantListAdapter(Restaurants);
        recyclerView.setAdapter(adapter);
    }

}
