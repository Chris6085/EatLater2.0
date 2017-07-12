package com.chris.playground.eatlater;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomCursorRecyclerViewAdapter extends CursorRecyclerViewAdapter {

    private static final String TAG = "RecyclerViewAdapter";

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

        // Remove the duplicate image showing up.
        holder.imageView.setImageDrawable(null);

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

        private static final String ID_KEY = "id_key";
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView noteView;
        private long id;

        public CustomViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.restaurant_info_entry, parent, false));
            nameView = (TextView) itemView.findViewById(R.id.title);
            noteView = (TextView) itemView.findViewById(R.id.note);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(clickListener);
        }

        private final View.OnClickListener clickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start the detail of the the restaurant.
                        Intent intent = new Intent(mContext, DisplayDetailActivity.class);
                        intent.putExtra(ID_KEY, id);
                        mContext.startActivity(intent);
                    }
                };

        public void setData(Cursor c) {
            nameView.setText(c.getString(c.getColumnIndex(RestaurantEntry.TITLE)));
            noteView.setText(c.getString(c.getColumnIndex(RestaurantEntry.NOTE)));

            // Set the id and photos.
            this.id = c.getLong(c.getColumnIndex(RestaurantEntry._ID));
            setPhotoData(c);
        }

        private void setPhotoData(Cursor c) {
            String photo_uris = c.getString(c.getColumnIndex(RestaurantEntry.PHOTOS_URI));
            if (null != photo_uris) {
                ArrayList<String> list_uri = Utils.checkImageUris(
                        new ArrayList<>(Arrays.asList(photo_uris.split(","))));

                if (list_uri.size() != 0) {
                    // Load the first photo for default pic.
                    Glide.with(mContext).load(list_uri.get(0))
                            .placeholder(R.drawable.ic_refresh).centerCrop()
                            .into(imageView);
                    return;
                }
            }

            Glide.with(mContext).load(R.drawable.ic_cheese)
                    .placeholder(R.drawable.ic_refresh).centerCrop()
                    .into(imageView);
        }
    }
}