package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;


public class CourseTable extends AppCompatActivity {

    private ImageView back;
    private ImageView addNewCourse;
    private String col;
    private String row;
    private String name;
    private String place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_table);
        readCourse();

        back = (ImageView)findViewById(R.id.back_home_main);
        addNewCourse = (ImageView)findViewById(R.id.add_new_course);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                startActivity(new Intent(CourseTable.this,AddCourse.class));
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readCourse();
    }

    private void readCourse(){
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Resources resources = getResources();
        String name;
        if (week == 0){
            for (int i = 1; i < 8; i++) {
                name = "item_" + 7 + "_" + i;
                int resId = resources.getIdentifier(name,"id",this.getPackageName());
                TextView textView = (TextView)findViewById(resId);
                textView.setBackground(getResources().getDrawable(R.drawable.course_bg_0));
            }
        }else {
            for (int i = 1; i < 8; i++) {
                name = "item_" + week + "_" + i;
                int resId = resources.getIdentifier(name,"id",this.getPackageName());
                TextView textView = (TextView)findViewById(resId);
                textView.setBackground(getResources().getDrawable(R.drawable.course_bg_0));
            }
        }
        Log.d("today",String.valueOf(week));
        List<Course> courses = LitePal.findAll(Course.class);
        for (Course temp : courses) {
            row = temp.getTime();
            col = temp.getWeekday();
            place = temp.getPlace();
            name = temp.getName();
            String s = name+"\n" + place;
            setCourse(row,col,s);
        }
    }

    private void setCourse(String row, String col, String course){
        Resources resources = getResources();
        String name = "item_" + col + "_" + row;
        int resId = resources.getIdentifier(name,"id",this.getPackageName());
        TextView textView = (TextView)findViewById(resId);
        textView.setText(course);
        if (Integer.parseInt(row)%3 == 0 && Integer.parseInt(col)%3 == 0){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_1));
        }else if (Integer.parseInt(row)%3 == 0 && Integer.parseInt(col)%3 == 1){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_2));
        }else if (Integer.parseInt(row)%3 == 0 && Integer.parseInt(col)%3 == 2){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_3));
        }else if (Integer.parseInt(row)%3 == 1 && Integer.parseInt(col)%3 == 0){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_4));
        }else if (Integer.parseInt(row)%3 == 1 && Integer.parseInt(col)%3 == 1){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_5));
        }else if (Integer.parseInt(row)%3 == 1 && Integer.parseInt(col)%3 == 2){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_6));
        }else if (Integer.parseInt(row)%3 == 2 && Integer.parseInt(col)%3 == 0){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_7));
        }else if (Integer.parseInt(row)%3 == 2 && Integer.parseInt(col)%3 == 1){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_8));
        }else if(Integer.parseInt(row)%3 == 2 && Integer.parseInt(col)%3 == 2){
            textView.setBackground(getResources().getDrawable(R.drawable.course_bg_9));
        }
    }




}
