package com.codepath.collabdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.codepath.collabdj.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView splash = (ImageView) this.findViewById(R.id.ivSplash);
        
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
