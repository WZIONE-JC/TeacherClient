package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogin = preferences.getBoolean("isLogin",false);
//                boolean isLogin = true;
                if (!isLogin){
                    //go to login activity
                    startActivity(new Intent(WelcomeActivity.this, LogInActivity.class));
                    finish();
                }else {
                    loginAgain();
                    finish();
                    //go to main activity
                }
            }
        },1000);

    }

    private void loginAgain(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",preferences.getString("id",""))
                            .add("password",preferences.getString("pass",""))
                            .build();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(new URL(MyStaticValue.LOGIN_PATH))
                            .post(requestBody)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("欢迎界面登录",responseData);
                    JSONObject result = new JSONObject(responseData);

                    if (result.has("state")){
                        int state = result.getInt("state");
//                        Log.d("Login",String.valueOf(state));
                        if (state == 0){
                            String token = result.getString("token");
                            editor.putString("token",token);
                            editor.putBoolean("isLogin",true);
                            editor.apply();
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            editor.putBoolean("isLogin",false);
                            editor.apply();
                            Looper.prepare();
                            Toast.makeText(WelcomeActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            startActivity(new Intent(WelcomeActivity.this,LogInActivity.class));
                        }
                        finish();//destroy this activity
                    }else {
                        editor.putBoolean("isLogin",false);
                        editor.apply();
                        Log.d("login:",result.toString());
                        Looper.prepare();
                        Toast.makeText(WelcomeActivity.this,"用户不存在！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        startActivity(new Intent(WelcomeActivity.this,LogInActivity.class));
                        finish();//destroy this activity
                    }
                }catch (Exception e){
                    editor.putBoolean("isLogin",false);
                    editor.apply();
                    Looper.prepare();
                    Toast.makeText(WelcomeActivity.this,"服务器异常！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    startActivity(new Intent(WelcomeActivity.this,LogInActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
