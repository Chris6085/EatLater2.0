package com.chris.playground.eatlater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chrisit_Chang on 2017/6/19.
 */

public final class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private List<RestaurantEntry> restaurantEntries;

    RestaurantListAdapter(List<RestaurantEntry> restaurantEntries) {
        this.restaurantEntries = restaurantEntries;
    }

    void setRestaurantEntries(List<RestaurantEntry> restaurantEntries) {
        this.restaurantEntries = restaurantEntries;
        notifyDataSetChanged();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RestaurantViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder viewHolder, int i) {
        viewHolder.bind(restaurantEntries.get(i));
    }

    @Override
    public int getItemCount() {
        return restaurantEntries.size();
    }

    public static final class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private final TextView priceView;
        private final TextView nameView;

        RestaurantViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.restaurant_info_entry, parent, false));
            priceView = (TextView) itemView.findViewById(R.id.price);
            nameView = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(clickListener);
        }

        private final View.OnClickListener clickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: show product details
                    }
                };

        void bind(RestaurantEntry restaurant) {
            priceView.setText(restaurant.price);
            nameView.setText(restaurant.title);
        }
    }
}
