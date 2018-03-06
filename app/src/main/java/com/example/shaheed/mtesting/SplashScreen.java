package com.example.shaheed.mtesting;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.view.Window;

public class SplashScreen extends Activity {

    private static final int flashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashScreen.this, ListData.class);
                startActivity(mIntent);
                getWindow().setExitTransition(new Explode());
                finish();
            }
        }, flashTime);
    }
}