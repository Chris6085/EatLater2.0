package com.chris.playground.eatlater;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Function class
 */
public class Utils {

    /**
     * Getting updated Images Path by checking every uri in Gallery.
     *
     * @param checkedUris the previously saved uris in database
     * @return ArrayList with images Path (updated)
     */
    public static ArrayList<String> checkImageUris(ArrayList<String> checkedUris) {
        for (Iterator<String> iterator = checkedUris.iterator(); iterator.hasNext(); ) {
            String uri = iterator.next();
            if (!new File(uri).exists()) {
                iterator.remove();
            }
        }
        return checkedUris;
    }

    /**
     * Getting All Images Path.
     *
     * @param context the activity
     * @return ArrayList with images Path
     */
    public static ArrayList<String> getAllShownImagesPath(Context context) {

        ArrayList<String> listOfAllImages = new ArrayList<>();
        int column_index_data, column_index_folder_name;
        String absolutePathOfImage;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
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
}
