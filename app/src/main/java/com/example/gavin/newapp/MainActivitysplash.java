package com.example.gavin.newapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivitysplash extends AppCompatActivity {

    //splash screen set to 4000 milliseconds
    private static final int DELAY_MILLISECONDS = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removes the top header
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //XML layouts
        setContentView(R.layout.activity_main_activitysplash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //brings to the homepage
                Intent i = new Intent(MainActivitysplash.this, MainActivityhome.class);
                startActivity(i);
                finish();
            }
        }, DELAY_MILLISECONDS);
    }
}