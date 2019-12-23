package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CourseMain extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        ImageView back = (ImageView)findViewById(R.id.back_my_class);
        Button more = (Button)findViewById(R.id.more_course_announcement);
        LinearLayout courseWare = (LinearLayout) findViewById(R.id.course_ware_class);
        LinearLayout homework = (LinearLayout)findViewById(R.id.homework);
        LinearLayout signIn = (LinearLayout)findViewById(R.id.sign_in);
        LinearLayout test = (LinearLayout)findViewById(R.id.test);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseMain.this,MoreInfo.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        courseWare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseMain.this,Courseware.class));
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseMain.this,MyHomework.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

    }
}
