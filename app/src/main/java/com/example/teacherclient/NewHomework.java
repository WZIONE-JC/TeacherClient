package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewHomework extends AppCompatActivity {
    private ImageView back;
    private TextView send;
    private EditText title;
    private EditText content;
    private DatePicker datePicker;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_homework);
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
                sendHomework();
            }
        });
    }

    private void initData(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        back = (ImageView)findViewById(R.id.back_form_homework);
        send = (TextView)findViewById(R.id.button_send_homework);
        title = (EditText) findViewById(R.id.homework_title);
        content = (EditText)findViewById(R.id.homework_content);
        datePicker = (DatePicker)findViewById(R.id.homework_deadline);

    }

    private void sendHomework(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth()+" 23:59:59";
                try {
                    Date date = format.parse(time);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(getIntent().getIntExtra("courseNo",-1)))
                            .add("title",title.getText().toString())
                            .add("content",content.getText().toString())
                            .add("token",preferences.getString("token",""))
                            .add("deadline",String.valueOf(date.getTime()))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.SEND_HOMEWORK))
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
                                        Toast.makeText(NewHomework.this,"作业发布成功",Toast.LENGTH_SHORT).show();
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
