package com.example.teacherclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class WriteNewCard extends AppCompatActivity {

    private ImageView back;
    private Button send;
    private EditText type;
    private EditText title;
    private EditText text;
    private SharedPreferences preferences;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_card);

        back = (ImageView)findViewById(R.id.back_to_talk_area);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type = (EditText)findViewById(R.id.card_type);
        title = (EditText)findViewById(R.id.card_title);
        text = (EditText)findViewById(R.id.card_text);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        Toast.makeText(WriteNewCard.this,"发布成功",Toast.LENGTH_SHORT).show();
                        getDiscuss();
                        break;
                    case 1:
                        WriteNewCard.this.finish();
                        break;
                }
            }
        };

        send = (Button)findViewById(R.id.send_card);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().trim().equals("")){
                    Toast.makeText(WriteNewCard.this,"相关课程不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().toString().trim().equals("")){
                    Toast.makeText(WriteNewCard.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (text.getText().toString().trim().equals("")){
                    Toast.makeText(WriteNewCard.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                sendDiscuss();
            }
        });


    }

    private void sendDiscuss(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("user_no",preferences.getString("id",""))
                            .add("token",preferences.getString("token",""))
                            .add("course_name",type.getText().toString())
                            .add("title",title.getText().toString())
                            .add("content",text.getText().toString())
                            .add("discuss_start_time",String.valueOf(System.currentTimeMillis()))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.NEW_DISCUSS))
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

    private void getDiscuss(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_DISCUSS))
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
                                            List<TalkCardTable> tables = LitePal.where("discussNo = ? ",temp.getString("discuss_no")).find(TalkCardTable.class);
                                            if (tables == null || tables.size() == 0){
                                            }else {
                                                for (int j = 0; j < tables.size(); j++) {
                                                    tables.get(j).delete();
                                                }
                                            }
                                            TalkCardTable table = new TalkCardTable();
                                            table.setDiscussNo(String.valueOf(temp.getInt("discuss_no")));
                                            table.setType(temp.getString("course_name"));
                                            table.setTitle(temp.getString("discuss_title"));
                                            table.setWriter(temp.getString("post_no"));
                                            table.setTextContent(temp.getString("content"));
                                            table.setTimeStamp(String.valueOf(temp.getLong("discuss_start_time")));
                                            table.save();
                                        }

                                    }
                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                                handler.sendEmptyMessage(1);
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
