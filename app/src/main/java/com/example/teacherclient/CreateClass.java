package com.example.teacherclient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CreateClass extends Activity {

    private ImageView back;
    private Spinner courseDay;
    private Spinner courseTime;
    private EditText courseName;
    private Button createClassroom;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private Handler handler;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        initData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClassroom();
            }
        });
    }

    private void initData(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        back = (ImageView) findViewById(R.id.back_form_join_class);
        courseDay = (Spinner)findViewById(R.id.create_course_day);
        courseTime = (Spinner)findViewById(R.id.create_course_time);
        courseName = (EditText)findViewById(R.id.create_course_name);
        createClassroom = (Button)findViewById(R.id.button_create_classroom);

        String[] day = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        final String[] time = {"第一节(8:00~9:50)",
                "第二节(10:10~12:00)",
                "第三节(12:10~14:00)",
                "第四节(14:10~16:00)",
                "第五节(16:20~18:10)",
                "第六节(19:00~20:50)",
                "第七节(21:10~22:00)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,day);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,time);
        courseDay.setAdapter(adapter);
        courseTime.setAdapter(adapter1);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        AlertDialog dialog = new AlertDialog.Builder(CreateClass.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("成功")
                                .setMessage("课程邀请码为：" + code)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getClassInfo();
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                        break;
                }
            }
        };

    }


    //发送创建课堂的请求

    private void createClassroom(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("name",courseName.getText().toString());
                    Log.d("time",String.valueOf(courseDay.getSelectedItemPosition()+1));
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("token",preferences.getString("token",""))
                            .add("teacher_no",preferences.getString("id",""))
//                            .add("token","063ebcef520e9602c356f92edad474e5")
                            .add("course_name",courseName.getText().toString())
                            .add("teach_time",String.valueOf(courseDay.getSelectedItemPosition()+1))
                            .add("teach_location",String.valueOf(courseTime.getSelectedItemPosition()+1))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.CREATE_CLASSROOM))
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("error","s");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()){
                                String result = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(result);
                                    Log.d("success",object.toString());
                                    int state = object.getInt("status");
                                    if (state == 0){
                                        code = object.getString("code");
                                        handler.sendEmptyMessage(0);
                                        Looper.prepare();
                                        Toast.makeText(CreateClass.this,"创建课堂成功",Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                            }else {
                                Log.d("fail",response.body().string());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void getClassInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("teacher_no",preferences.getString("id",""))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_CLASS_INFO))
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()){
                                String result = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(result);
                                    Log.d("a",object.toString());
                                    int state = object.getInt("status");
                                    if (state == 0){
                                        JSONArray list = object.getJSONArray("list");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject temp = list.getJSONObject(i);
                                            List<Classroom> classrooms = LitePal.where("code = ? ",temp.getString("code")).find(Classroom.class);
                                            if (classrooms == null || classrooms.size() == 0){
                                            }else {
                                                for (int j = 0; j < classrooms.size(); j++) {
                                                    classrooms.get(j).delete();
                                                }
                                            }
                                            Classroom classroom = new Classroom();
                                            classroom.setCourseNo(temp.getInt("course_no"));
                                            classroom.setCourseName(temp.getString("course_name"));
                                            classroom.setTeachTime(Integer.valueOf(temp.getString("teach_time")));
                                            classroom.setTeachLocation(Integer.valueOf(temp.getString("teach_location")));
                                            classroom.setTeacherNo(temp.getString("teacher_no"));
                                            classroom.setCode(temp.getString("code"));
                                            classroom.save();
                                        }

                                    }
                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                            }else {
                                Log.d("a",response.body().string());
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}