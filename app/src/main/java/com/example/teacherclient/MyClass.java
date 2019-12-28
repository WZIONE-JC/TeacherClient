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

import com.example.teacherclient.Classroom;
import com.example.teacherclient.CourseMain;
import com.example.teacherclient.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClass extends Activity implements AdapterView.OnItemClickListener{
    private ListView listv;
    private TextView nowClassName;
    private TextView nowClassDay;
    private TextView nowClassTime;
    private String nowClassCode;
    private  LinearLayout nClass;
    private String[] classCodes;
    private TextView tip;
    private List<Classroom> classrooms;
    private String[]  times = {
            "8:00~9:50",
            "10:10~12:00",
            "12:10~14:00",
            "14:10~16:00",
            "16:20~18:10",
            "19:00~20:50",
            "21:10~22:00"};
    private String[] days = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};


//    //从classroom表获取信息
//    //private int[] pic={R.drawable.ic_1,R.drawable.ic_2,R.drawable.ic_3};
//    private	String[] classname={"移动应用开发","操作系统"};
//    private  String[] person={"张迪老师","张迪老师"};
//    private  String[] date={"2019.9.1","2019.9.1"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);


        initData();

        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        classrooms = LitePal.findAll(Classroom.class);
        classCodes = new String[classrooms.size()];
        for(int i=0;i<classrooms.size();i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            //listItem.put("pic", pic[i]);
            classCodes[i] = classrooms.get(i).getCode();
            listItem.put("classname", classrooms.get(i).getCourseName());
            listItem.put("day", days[classrooms.get(i).getTeachTime()-1]);
            listItem.put("time", times[classrooms.get(i).getTeachLocation()-1]);
            listItems.add(listItem);
        }


        SimpleAdapter simleAdapter = new SimpleAdapter(MyClass.this, listItems,
                R.layout.course_layout	,new String[]{"classname","day","time"},
                new int[]{R.id.classname, R.id.person, R.id.date});
        listv.setAdapter(simleAdapter);

        listv.setOnItemClickListener(this);



        nClass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nowClassCode == null || nowClassCode.equals("")){
                    return;
                }else {
                    Intent intent = new Intent(MyClass.this, CourseMain.class);
                    intent.putExtra("code",nowClassCode);
                    startActivity(intent);
                }

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
//        String Text=map.get("classname");
//        Toast.makeText(MyClass.this,Text,Toast.LENGTH_SHORT).show();
        String tCode = classCodes[i];
        Intent intent = new Intent(MyClass.this, CourseMain.class);
        intent.putExtra("code",tCode);
        startActivity(intent);

    }

    private void initData(){
        nowClassName = (TextView)findViewById(R.id.now_class_name);
        nowClassDay = (TextView)findViewById(R.id.now_class_day);
        nowClassTime = (TextView)findViewById(R.id.now_class_time);
        tip = (TextView)findViewById(R.id.text_classroom_tip);
        listv = (ListView) findViewById(R.id.listView1);
        nClass = (LinearLayout)findViewById(R.id.now_class);

        nowClassTime.setVisibility(View.GONE);
        nowClassName.setVisibility(View.GONE);
        nowClassDay.setVisibility(View.GONE);


        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        String day;
        String time;
        if (hour == 8){
            time = "1";
        }else if (hour == 9 && minute > 0 && minute < 50){
            time = "1";
        }else if (hour == 10 && minute > 10){
            time = "2";
        }else if (hour == 11){
            time = "2";
        }else if (hour == 12 && minute > 10){
            time = "3";
        }else if (hour == 13){
            time = "3";
        }else if (hour == 14 && minute > 10){
            time = "4";
        }else if (hour == 15){
            time = "4";
        }else if (hour == 16 && minute >20){
            time = "5";
        }else if (hour == 17){
            time = "5";
        }else if (hour == 18 && minute<10){
            time = "5";
        }else if (hour == 19){
            time = "6";
        }else if (hour == 20 && minute <50){
            time = "6";
        }else{
            time = "7";
        }



        if (week == 0){
            day = "7";
        }else {
            day = String.valueOf(week);
        }

        List<Classroom> classrooms = LitePal.where("teachTime = ?",day).where("teachLocation = ?", time).find(Classroom.class);
        for (Classroom classroom:classrooms) {
            tip.setVisibility(View.GONE);
            nowClassTime.setVisibility(View.VISIBLE);
            nowClassName.setVisibility(View.VISIBLE);
            nowClassDay.setVisibility(View.VISIBLE);

            nowClassName.setText(classroom.getCourseName());
            nowClassDay.setText(days[classroom.getTeachTime()]);
            nowClassTime.setText(times[classroom.getTeachLocation()]);
            nowClassCode = classroom.getCode();
        }

    }
}