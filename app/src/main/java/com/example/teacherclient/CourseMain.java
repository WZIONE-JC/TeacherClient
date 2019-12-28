package com.example.teacherclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseMain extends AppCompatActivity {
    private int courseNo;
    private String classCode;
    private int classDay;
    private int classTime;
    private String className;
    private String studentName;

    private ImageView back;
    private TextView name;
    private Button more;
    private LinearLayout courseWare;
    private LinearLayout homework;
    private LinearLayout signIn;
    private LinearLayout test;
    private TextView send;
    private TextView nowAnnounceTitle;
    private TextView nowAnnounceTime;
    private Button randomCall;

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);
        initData();
        getAnnounceInfo();
        setAnnounce();
        getHomework();
        getTest();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        AlertDialog dialog = new AlertDialog.Builder(CourseMain.this)
                                .setTitle("随机测试")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage("随机结果："+ studentName)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                }
            }
        };
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMain.this, MoreInfo.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
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
                Intent intent = new Intent(CourseMain.this, Courseware.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMain.this, MyHomework.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMain.this, BulidAnnounce.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMain.this, CourseCall.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMain.this, CourseTest.class);
                intent.putExtra("courseNo",courseNo);
                startActivity(intent);
            }
        });

        randomCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseStudent();
            }
        });

    }

    private void initData(){
        nowAnnounceTitle = (TextView)findViewById(R.id.now_announce_title);
        nowAnnounceTime = (TextView)findViewById(R.id.now_announce_time);
        send = (TextView)findViewById(R.id.send_announce);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        back = (ImageView)findViewById(R.id.back_my_class);
        name = (TextView)findViewById(R.id.class_main_name);
        more = (Button)findViewById(R.id.more_course_announcement);
        courseWare = (LinearLayout) findViewById(R.id.course_ware_class);
        homework = (LinearLayout)findViewById(R.id.homework);
        signIn = (LinearLayout)findViewById(R.id.sign_in);
        test = (LinearLayout)findViewById(R.id.test);
        randomCall = (Button)findViewById(R.id.button_random_call);


        Intent getIntent = getIntent();
        String code = getIntent.getStringExtra("code");
        if (code == null || code.equals("")){

        }else {
            List<Classroom> classrooms = LitePal.where("code = ?",code).find(Classroom.class);
            Classroom classroom = classrooms.get(0);
            courseNo = classroom.getCourseNo();
            classCode = classroom.getCode();
            className = classroom.getCourseName();
            classDay = classroom.getTeachTime();
            classTime = classroom.getTeachLocation();

        }
        name.setText(className);
        getFileInfo();

    }

    private void setAnnounce(){
        List<Announce> announces = LitePal.where("courseNo = ?", String.valueOf(courseNo)).order("time desc").find(Announce.class);
        if (!announces.isEmpty()){
            nowAnnounceTitle.setText(announces.get(0).getTitle());
            long time = Long.parseLong(announces.get(0).getTime());
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            nowAnnounceTime.setText(format.format(date));
        }
    }

    private void getFileInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(courseNo))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_FILE_INFO))
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("lfq" ,e.toString());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String result = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(result);
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        Log.d("b",result);
                                        JSONArray list = object.getJSONArray("fileEntities");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject temp = list.getJSONObject(i);
                                            List<CourseFile> courseFiles = LitePal.where("fileNo = ? ",temp.getString("file_no")).find(CourseFile.class);
                                            if (courseFiles == null || courseFiles.size() == 0){
                                            }else {
                                                for (int j = 0; j < courseFiles.size(); j++) {
                                                    courseFiles.get(j).delete();
                                                }
                                            }
                                            CourseFile courseFile = new CourseFile();
                                            courseFile.setFileNo(temp.getString("file_no"));
                                            courseFile.setCourseNo(temp.getString("course_no"));
                                            courseFile.setFileName(temp.getString("file_name"));
                                            courseFile.save();
                                        }
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            } else {
                                Log.d("失败",response.body().string());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getAnnounceInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(courseNo))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_ANNOUNCE))
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
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        JSONArray list = object.getJSONArray("announcements");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject temp = list.getJSONObject(i);
                                            List<Announce> announces = LitePal.where("announcementNo = ? ",String.valueOf(temp.getInt("announcement_no"))).find(Announce.class);
                                            if (announces == null || announces.size() == 0){
                                            }else {
                                                for (int j = 0; j < announces.size(); j++) {
                                                    announces.get(j).delete();
                                                }
                                            }
                                            Announce announce = new Announce();
                                            announce.setCourseNo(temp.getString("course_no"));
                                            announce.setAnnouncementNo(temp.getInt("announcement_no"));
                                            announce.setTitle(temp.getString("title"));
                                            announce.setContent(temp.getString("content"));
                                            announce.setTime(temp.getString("time"));
                                            announce.save();
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

    private void getHomework(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(courseNo))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_HOMEWORK))
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
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        JSONArray list = object.getJSONArray("homeworkList");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject temp = list.getJSONObject(i);
                                            List<HomeWork> homeWorks = LitePal.where("homeworkNo = ? ",String.valueOf(temp.getInt("homework_no"))).find(HomeWork.class);
                                            if (homeWorks == null || homeWorks.size() == 0){
                                            }else {
                                                for (int j = 0; j < homeWorks.size(); j++) {
                                                    homeWorks.get(j).delete();
                                                }
                                            }
                                            HomeWork homeWork = new HomeWork();
                                            homeWork.setCourseNo(temp.getString("course_no"));
                                            homeWork.setHomeworkNo(temp.getInt("homework_no"));
                                            homeWork.setTitle(temp.getString("title"));
                                            homeWork.setContent(temp.getString("content"));
                                            homeWork.setDeadline(temp.getLong("deadtime"));
                                            homeWork.save();
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

    private void getTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(courseNo))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_TEST))
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
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        JSONArray list = object.getJSONArray("tests");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject temp = list.getJSONObject(i);
                                            List<TestTable> tests = LitePal.where("testNo = ? ",String.valueOf(temp.getInt("test_no"))).find(TestTable.class);
                                            if (tests == null || tests.size() == 0){
                                            }else {
                                                for (int j = 0; j < tests.size(); j++) {
                                                    tests.get(j).delete();
                                                }
                                            }
                                            TestTable test = new TestTable();
                                            test.setCourseNo(temp.getString("course_no"));
                                            test.setTestNo(temp.getInt("test_no"));
                                            test.setTitle(temp.getString("title"));
                                            test.setContent(temp.getString("content"));
                                            test.setDeadline(temp.getLong("deadtime"));
                                            test.save();
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


    private void chooseStudent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(courseNo))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.CHOOSE_STUDENT))
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
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        JSONObject object1 = object.getJSONObject("student");
                                        studentName = object1.getString("student_name");
                                        handler.sendEmptyMessage(0);
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
