package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teacherclient.Courseware;
import com.example.teacherclient.MoreInfo;
import com.example.teacherclient.MyHomework;
import com.example.teacherclient.R;

import org.litepal.LitePal;

import java.util.List;

public class CourseMain extends AppCompatActivity {
    private String classCode;
    private int classDay;
    private int classTime;
    private String className;

    private ImageView back;
    private TextView name;
    private Button more;
    private LinearLayout courseWare;
    private LinearLayout homework;
    private LinearLayout signIn;
    private LinearLayout test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);
        initData();


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseMain.this, MoreInfo.class));
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
                startActivity(new Intent(CourseMain.this, Courseware.class));
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseMain.this, MyHomework.class));
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

    private void initData(){
        back = (ImageView)findViewById(R.id.back_my_class);
        name = (TextView)findViewById(R.id.class_main_name);
        more = (Button)findViewById(R.id.more_course_announcement);
        courseWare = (LinearLayout) findViewById(R.id.course_ware_class);
        homework = (LinearLayout)findViewById(R.id.homework);
        signIn = (LinearLayout)findViewById(R.id.sign_in);
        test = (LinearLayout)findViewById(R.id.test);


        Intent getIntent = getIntent();
        String code = getIntent.getStringExtra("code");
        if (code == null || code.equals("")){

        }else {
            List<Classroom> classrooms = LitePal.where("code = ?",code).find(Classroom.class);
            Classroom classroom = classrooms.get(0);
            classCode = classroom.getCode();
            className = classroom.getCourseName();
            classDay = classroom.getTeachTime();
            classTime = classroom.getTeachLocation();

        }
        name.setText(className);

    }
}
