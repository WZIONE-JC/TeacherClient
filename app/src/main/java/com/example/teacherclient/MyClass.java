package com.example.teacherclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClass extends Activity implements AdapterView.OnItemClickListener{
    private ListView listv;
    //private int[] pic={R.drawable.ic_1,R.drawable.ic_2,R.drawable.ic_3};
    private	String[] classname={"移动应用开发","操作系统"};
    private  String[] person={"张迪老师","张迪老师"};
    private  String[] date={"2019.9.1","2019.9.1"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        listv = (ListView) findViewById(R.id.listView1);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<classname.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            //listItem.put("pic", pic[i]);
            listItem.put("classname", classname[i]);
            listItem.put("person", person[i]);
            listItem.put("date", date[i]);
            listItems.add(listItem);

        }
        SimpleAdapter simleAdapter = new SimpleAdapter(MyClass.this, listItems,
                R.layout.course_layout	,new String[]{"classname","person","date"},
                new int[]{R.id.classname, R.id.person, R.id.date});
        listv.setAdapter(simleAdapter);

        TextView classname=(TextView)findViewById(R.id.classname); //R.id.tv是xml布局里textview对应的id
        TextView person=(TextView)findViewById(R.id.person); //R.id.tv是xml布局里textview对应的id
        TextView date=(TextView)findViewById(R.id.date); //R.id.tv是xml布局里textview对应的id
        classname.setText("软件法规与知识产权");
        person.setText("YuLiya");
        date.setText("2019.9.1");
        LinearLayout nclass = (LinearLayout)findViewById(R.id.nowclass);
        listv.setOnItemClickListener(this);
        nclass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MyClass.this, CourseMain.class));
            }
        });

        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /*adapterView是指当前的listview；
         *view是当前listview中的item的view的布局,就是可用这个view获取里面控件id后操作控件
         * i是当前item在listview中适配器的位置
         * l是当前item在listview里第几行的位置
         */
        //获得选中项中的HashMap对象

        HashMap<String,String> map=(HashMap<String,String>)adapterView.getItemAtPosition(i);
        String Text=map.get("classname");
        Toast.makeText(MyClass.this,Text,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MyClass.this, CourseMain.class));

    }
}