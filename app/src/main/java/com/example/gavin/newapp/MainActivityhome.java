package com.example.gavin.newapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

//declares variables used
public class MainActivityhome extends AppCompatActivity {
    ImageButton imageButton4;
    ImageButton imageButton5;
    ImageButton imageButton6;
    ImageButton imageButton3;
    Button addevents;

    public void init(){
//activities button function
        imageButton3= (ImageButton)findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent INT = new Intent(MainActivityhome.this,MapsActivity.class);

                startActivity(INT);
            }
        });
        //food button function
        imageButton4= (ImageButton)findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent INT = new Intent(MainActivityhome.this, MapsActivity3.class);

                startActivity(INT);
            }
        });

//contact button function
        imageButton5= (ImageButton)findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent INT = new Intent(MainActivityhome.this, MainActivitycontact.class);

                startActivity(INT);
            }
        });

//events button function
        imageButton6= (ImageButton)findViewById(R.id.imageButton6);
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent INT = new Intent(MainActivityhome.this, MapsActivity2.class);

                startActivity(INT);
            }
        });
//add events button function
        addevents= (Button)findViewById(R.id.addevents);
        addevents.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent INT = new Intent(MainActivityhome.this, Addmarker.class);

            startActivity(INT);
        }
    });




}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activityhome);
        init();

    }
}

