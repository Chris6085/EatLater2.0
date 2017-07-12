package com.chris.playground.eatlater;

import android.app.Activity;
import android.content.Context;
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
        mImages = Utils.getAllShownImagesPath(mContext);
    }

    public ImageAdapter(Context context, int layoutResourceId, ArrayList<String> images) {
        super(context, layoutResourceId);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mImages = Utils.checkImageUris(images);
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

        holder.imageTitle.setText("test");
        holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(mContext).load(mImages.get(position))
                .placeholder(R.drawable.ic_refresh).centerCrop()
                .into(holder.image);

        return convertView;
    }

    private class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}