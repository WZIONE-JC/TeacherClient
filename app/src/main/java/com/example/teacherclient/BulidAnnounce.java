package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BulidAnnounce extends AppCompatActivity {
    private ImageView back;
    private TextView send;
    private EditText title;
    private EditText content;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulid_announce);
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnnounce();
                finish();
            }
        });
    }

    private void init(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        back = (ImageView)findViewById(R.id.back_form_build_announce);
        send = (TextView)findViewById(R.id.button_send_announce);
        title = (EditText)findViewById(R.id.announce_title);
        content = (EditText)findViewById(R.id.announce_content);
    }

    private void sendAnnounce(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(getIntent().getIntExtra("courseNo",-1)))
                            .add("title",title.getText().toString())
                            .add("content",content.getText().toString())
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.SEND_ANNOUNCE))
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
                                        Toast.makeText(BulidAnnounce.this,"通知发布成功",Toast.LENGTH_SHORT).show();
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
