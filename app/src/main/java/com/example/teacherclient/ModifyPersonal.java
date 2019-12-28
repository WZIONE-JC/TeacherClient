package com.example.teacherclient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONObject;

import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ModifyPersonal extends AppCompatActivity implements View.OnClickListener{

    private Button btn_submit;
    private TextView t_id;
    private EditText name;
    private EditText major;
    private EditText tel;
    private EditText mail;
    private Spinner sex;
    private ImageView img_back;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String path = MyStaticValue.UPDATE_INFO_PATH;
    private String[] sexType = {"男","女"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal);
        initview();
        initData();
    }


    private void initview() {

        t_id=findViewById(R.id.id);
        name=(EditText)findViewById(R.id.name);
        major=(EditText)findViewById(R.id.major);
        tel=(EditText)findViewById(R.id.tel);
        mail=(EditText)findViewById(R.id.mail);
        sex = (Spinner)findViewById(R.id.sex);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyPersonal.this,R.layout.support_simple_spinner_dropdown_item,sexType);
        sex.setAdapter(adapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        btn_submit=(Button)findViewById(R.id.submit);
        btn_submit.setOnClickListener(this);

        img_back=findViewById(R.id.back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.submit:
                submitInfo();
                break;
        }
    }

    private void initData(){
        //其实应该先从服务器获取数据更新本地信息

        String id = preferences.getString("id","");
        String name = preferences.getString("name","昵称");
        String major = preferences.getString("major","");
        String phone = preferences.getString("phone","");
        String mail = preferences.getString("mail","");
        String sex = preferences.getString("sex","m");
        t_id.setText(id);
        this.name.setText(name);
        this.major.setText(major);
        this.tel.setText(phone);
        this.mail.setText(mail);
        if (sex.equals("m")){
            this.sex.setSelection(0);
        }else {
            this.sex.setSelection(1);
        }
    }

    private void submitInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String type;
                if (sex.getSelectedItem().toString().equals("男")){
                    type = "m";
                }else {
                    type = "f";
                }
                String responseData;
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",t_id.getText().toString())
                            .add("user_name",name.getText().toString())
                            .add("sex",type)
                            .add("title",major.getText().toString())
                            .add("token",preferences.getString("token","no_token"))
                            .add("birthday","2019-12-30")
                            .build();

                    Log.d("q",t_id.getText().toString());
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(new URL(path))
                            .post(requestBody)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                    JSONObject result = new JSONObject(responseData);
                    Log.d("修改后",responseData);
                    if (result.isNull("state")){
                        Looper.prepare();
                        Toast.makeText(ModifyPersonal.this,"服务器异常",Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }else {
                        int state = result.getInt("state");

                        if (state == MyStaticValue.STATE_OK){
                            editor.putString("name",name.getText().toString());
                            editor.putString("major",major.getText().toString());
                            editor.putString("phone",tel.getText().toString());
                            editor.putString("mail",mail.getText().toString());
                            editor.putString("sex",type);
                            editor.apply();
                            Looper.prepare();
                            Toast.makeText(ModifyPersonal.this,"更新成功",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        }else {
                            Looper.prepare();
                            Toast.makeText(ModifyPersonal.this,"更新失败",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }).start();


    }
}
