package com.chris.playground.eatlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {

    private View mDecorView;
    private boolean mImmersiveFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        initUI(getIntent());
        // Hide System UI (default)
        hideSystemUi();
    }

    private void initUI(Intent intent) {

        mDecorView = getWindow().getDecorView();

        ImageView imageView = (ImageView) findViewById(R.id.fullScreenImageView);
        if (imageView != null) {
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mImmersiveFlag) {
                        hideSystemUi();
                    } else {
                        showSystemUi();
                    }
                    return false;
                }
            });
        }

        if (intent != null) {
            String uri = intent.getStringExtra(DisplayDetailActivity.IMAGE_URI_KEY);
            if (uri != null && imageView != null) {
                Glide.with(this).load(uri)
                        .placeholder(R.drawable.ic_refresh).centerCrop()
                        .into(imageView);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "uri == null or extra is not found",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "intent == null",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void hideSystemUi() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        mImmersiveFlag = !mImmersiveFlag;
    }

    private void showSystemUi() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mImmersiveFlag = !mImmersiveFlag;
    }
}
