package com.example.teacherclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseCall extends AppCompatActivity {
    private ImageView back;
    private TextView send;
    private TimePicker timePicker;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_call);
        initData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCall();
                finish();
            }
        });
    }

    private void initData(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        back = (ImageView)findViewById(R.id.back_form_call);
        send = (TextView)findViewById(R.id.button_send_call);
        timePicker = (TimePicker)findViewById(R.id.call_deadline);

    }

    private void sendCall(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String endTime = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH) + 1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+
                        timePicker.getHour() + ":" + timePicker.getMinute() + ":00";

                long startTime = System.currentTimeMillis();
                try {
                    Date date = format.parse(endTime);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(getIntent().getIntExtra("courseNo",-1)))
                            .add("token",preferences.getString("token",""))
                            .add("start_time", String.valueOf(startTime))
                            .add("dead_time",String.valueOf(date.getTime()))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.SEND_CALL))
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()){
                                String s = response.body().string();
                                Log.d("c",s);
                                try {
                                    JSONObject result = new JSONObject(s);
                                    int state = result.getInt("state");
                                    if (state == 0){
                                        Looper.prepare();
                                        Toast.makeText(CourseCall.this,"开始点名",Toast.LENGTH_SHORT).show();
                                        Looper.loop();

                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }else{
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

}
