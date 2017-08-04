package com.chris.playground.eatlater;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.chris.playground.eatlater.database.RestaurantsContract.RestaurantEntry;

public final class SearchListAdapter extends CursorAdapter {

    private static final String ID_KEY = "id_key";

    private Context mContext;
    private Cursor mCursor;

    public SearchListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
        mCursor = c;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    class ViewHolder {
        TextView title;
        long id;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.restaurant_search_entry, null);
        ViewHolder vh = new ViewHolder();
        vh.title = (TextView) view.findViewById(R.id.title_text);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view == null || cursor == null)
            return;
        ViewHolder vh = (ViewHolder) view.getTag();
        vh.title.setText(cursor.getString(cursor.getColumnIndex(RestaurantEntry.TITLE)));
        vh.id = cursor.getLong(cursor.getColumnIndex(RestaurantEntry._ID));

        final long id = vh.id;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the detail of the the restaurant.
                Intent intent = new Intent(mContext, DisplayDetailActivity.class);
                intent.putExtra(ID_KEY, id);
                mContext.startActivity(intent);
            }
        });

    }
}
