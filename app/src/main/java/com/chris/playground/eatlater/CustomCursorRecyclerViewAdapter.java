package com.chris.playground.eatlater;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

public class CustomCursorRecyclerViewAdapter extends CursorRecyclerViewAdapter {

    public CustomCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        CustomViewHolder holder = (CustomViewHolder) viewHolder;
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final TextView noteView;

        public CustomViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.restaurant_info_entry, parent, false));
            nameView = (TextView) itemView.findViewById(R.id.title);
            noteView = (TextView) itemView.findViewById(R.id.note);
            itemView.setOnClickListener(clickListener);
        }

        private final View.OnClickListener clickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: show product details
                    }
                };

        public void setData(Cursor c) {
            nameView.setText(c.getString(c.getColumnIndex(RestaurantEntry.TITLE)));
            noteView.setText(c.getString(c.getColumnIndex(RestaurantEntry.NOTE)));

        }
    }
}