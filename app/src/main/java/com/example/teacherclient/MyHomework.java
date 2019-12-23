package com.example.teacherclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyHomework extends AppCompatActivity {
    private ListView list_homework;
    private ImageView back;

    private	String[] homework={"第一次作业","第二次作业"};
    private  String[] date={"2019.9.1","2019.9.2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_homework);
        list_homework = (ListView) findViewById(R.id.my_homework);

        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<homework.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("homework", homework[i]);
            listItem.put("date", "截止日期"+date[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simleAdapter = new SimpleAdapter(MyHomework.this, listItems,
                R.layout.homework_layout,new String[]{"homework","date"},
                new int[]{R.id.hw_title, R.id.hw_date});
        list_homework.setAdapter(simleAdapter);
        //点击事件
        list_homework.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0){
                    startActivity(new Intent(MyHomework.this, HomeworkDetail.class));
                }
            }
        });

        back = (ImageView)findViewById(R.id.back_to_course_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
