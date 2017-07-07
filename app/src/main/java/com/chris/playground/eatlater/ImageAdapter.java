package com.chris.playground.eatlater;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<String> mImages;

    public ImageAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mImages = getAllShownImagesPath(mContext);
    }

    public ImageAdapter(Context context, int layoutResourceId, ArrayList<String> images) {
        super(context, layoutResourceId);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mImages = images;
    }

    public int getCount() {
        return mImages.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater()
                    .inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) convertView.findViewById(R.id.display_text);
            holder.image = (ImageView) convertView.findViewById(R.id.display_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageTitle.setText("123");
        holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(mContext).load(mImages.get(position))
                .placeholder(R.drawable.ic_refresh).centerCrop()
                .into(holder.image);

        return convertView;
    }

    /**
     * Getting All Images Path.
     *
     * @param context
     *            the activity
     * @return ArrayList with images Path
     */
    public ArrayList<String> getAllShownImagesPath(Context context) {

        ArrayList<String> listOfAllImages = new ArrayList<>();
        int column_index_data, column_index_folder_name;
        String absolutePathOfImage;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        try {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            cursor.close();
        }
        return listOfAllImages;
    }

    private class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}

