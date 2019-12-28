package com.example.teacherclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseTest extends AppCompatActivity {
    private ListView list_homework;
    private ImageView back;
    private TextView newHomework;
    private int courseNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_test);

        newHomework = (TextView)findViewById(R.id.send_test);
        courseNo = getIntent().getIntExtra("courseNo",-1);
        newHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseTest.this, NewTest.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });
        list_homework = (ListView) findViewById(R.id.my_test);

        final List<TestTable> tests = LitePal.where("courseNo = ?", String.valueOf(getIntent().getIntExtra("courseNo",-1))).find(TestTable.class);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<tests.size();i++){
            Date date = new Date(tests.get(i).getDeadline());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("homework", tests.get(i).getTitle());
            listItem.put("date", format.format(date));
            listItems.add(listItem);
        }
        SimpleAdapter simleAdapter = new SimpleAdapter(CourseTest.this, listItems,
                R.layout.homework_layout,new String[]{"homework","date"},
                new int[]{R.id.hw_title, R.id.hw_date});
        list_homework.setAdapter(simleAdapter);
        //点击事件
        list_homework.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(CourseTest.this)
                        .setTitle(tests.get(position).getTitle())
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(tests.get(position).getContent())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        back = (ImageView)findViewById(R.id.back_from_test);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
