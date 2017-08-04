package com.chris.playground.eatlater;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chris.playground.eatlater.database.RestaurantsContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NewRestaurantActivity extends AppCompatActivity {

    private static final String TAG = "NewRestaurantActivity";
    private static final int CAMERA_PERMISSION = 101;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private static final String KEY_TITLE_TEXT = "key_title_text";
    private static final String KEY_NOTE_TEXT = "key_note_text";
    private static final String KEY_PHOTOS_URI = "key_photos_uri";

    private EditText mTitleView;
    private EditText mNoteView;
    private ImageButton mSubmitButton;
    private GridView mPhotoGridView;
    private ArrayList<String> mPhotoPreviewList;

    private String mCurrentPhotoPath;
    private static final String AUTHORITY_FILE_PROVIDER = "com.example.android.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);

        initUI();

    }

    private void initUI() {

        mTitleView = (EditText) findViewById(R.id.title_edit_Text);
        mNoteView = (EditText) findViewById(R.id.note_edit_Text);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(NewRestaurantActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(NewRestaurantActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(NewRestaurantActivity.this,
                            new String[]{Manifest.permission.CAMERA
                                    , Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA_PERMISSION);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        mSubmitButton = (ImageButton) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormComplete()) {
                    saveRestaurantInfo();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Fill in the above columns", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPhotoGridView = (GridView) findViewById(R.id.gridView);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mPhotoPreviewList = new ArrayList<>();
    }

    private boolean isFormComplete() {
        if (!TextUtils.isEmpty(mTitleView.getText())
                && !TextUtils.isEmpty(mNoteView.getText())) {
            mSubmitButton.setClickable(true);
            return true;
        }
        return false;
    }

    private void saveRestaurantInfo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RestaurantsContract.RestaurantEntry.TITLE,
                mTitleView.getText().toString());
        contentValues.put(RestaurantsContract.RestaurantEntry.NOTE,
                mNoteView.getText().toString());

        if (mPhotoPreviewList.size() != 0) {
            String uri_list = mPhotoPreviewList.get(0);
            for (int i = 1; i < mPhotoPreviewList.size(); i++) {
                uri_list += "," + mPhotoPreviewList.get(i);
            }
            contentValues.put(RestaurantsContract.RestaurantEntry.PHOTOS_URI, uri_list);
        }

        getContentResolver().insert(RestaurantsContract.RestaurantEntry.CONTENT_URI,
                contentValues);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Start the intent of camera
                    dispatchTakePictureIntent();
                } else {
                    Log.d(TAG, "permission denied");
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                photoFile = null;
                mCurrentPhotoPath = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, AUTHORITY_FILE_PROVIDER, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (mCurrentPhotoPath != null) {
                mPhotoPreviewList.add(mCurrentPhotoPath);
                updatePhotosPreview();
                galleryAddPic();
                mCurrentPhotoPath = null;
            }
        }
    }

    private void updatePhotosPreview() {
        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.grid_item_layout
                , mPhotoPreviewList);
        mPhotoGridView.setAdapter(imageAdapter);
    }

    private void galleryAddPic() {
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE_TEXT, mTitleView.getText().toString());
        outState.putString(KEY_NOTE_TEXT, mNoteView.getText().toString());

        if (mPhotoPreviewList.size() != 0) {
            String uri_list = mPhotoPreviewList.get(0);
            for (int i = 1; i < mPhotoPreviewList.size(); i++) {
                uri_list += "," + mPhotoPreviewList.get(i);
            }
            outState.putString(KEY_PHOTOS_URI, uri_list);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTitleView.setText(savedInstanceState.getString(KEY_TITLE_TEXT));
        mNoteView.setText(savedInstanceState.getString(KEY_NOTE_TEXT));

        String photo_uris = savedInstanceState.getString(KEY_PHOTOS_URI);
        if (null != photo_uris) {
            mPhotoPreviewList = new ArrayList<>(Arrays.asList(photo_uris.split(",")));
            updatePhotosPreview();
        }
    }
}
