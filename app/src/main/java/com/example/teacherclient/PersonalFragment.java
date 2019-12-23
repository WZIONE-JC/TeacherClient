package com.example.teacherclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PersonalFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private TextView t_name;
    private Button btn_quit;
    private LinearLayout preson_data;
    private LinearLayout courseware;
    private LinearLayout feedback;
    private LinearLayout about_us;
    private ImageView img_head;
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,null);
        initView(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.h_head: //选择头像
                startActivity(new Intent(getActivity(), SelectPicture.class));
                break;
            case R.id.btn_quit://退出登陆
                getActivity().finish();
                editor.putBoolean("isLogin",false);
                editor.apply();
                startActivity(new Intent(getActivity(), LogInActivity.class));
                break;
            case R.id.preson_data:
                startActivity(new Intent(getActivity(), ModifyPersonal.class));
                break;
            case R.id.courseware:
                break;
            case R.id.feedback:
                startActivity(new Intent(getActivity(), ForgetPassword.class));
                break;
            case R.id.about_us:
                break;
        }

    }


    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        t_name=(TextView)view.findViewById(R.id.user_name);
        t_name.setText(preferences.getString("name","昵称"));
        //t_number=(TextView)view.findViewById(R.id.t_number);

        btn_quit=(Button)view.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);

        preson_data=(LinearLayout)view.findViewById(R.id.preson_data);
        preson_data.setOnClickListener(this);


//        collection=(LinearLayout)view.findViewById(R.id.collect);
//        collection.setOnClickListener(this);

        courseware=(LinearLayout)view.findViewById(R.id.courseware);
        courseware.setOnClickListener(this);

//        discussion=(LinearLayout)view.findViewById(R.id.discuss);
//        discussion.setOnClickListener(this);
//
//        phone=(LinearLayout)view.findViewById(R.id.phone);
//        phone.setOnClickListener(this);
//
//        share=(LinearLayout)view.findViewById(R.id.share);
//        share.setOnClickListener(this);

        feedback=(LinearLayout)view.findViewById(R.id.feedback);
        feedback.setOnClickListener(this);

        about_us=(LinearLayout)view.findViewById(R.id.about_us);
        about_us.setOnClickListener(this);

        img_head=(ImageView)view.findViewById(R.id.h_head);
        img_head.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        boolean uploadAvatar = preferences.getBoolean("uploadAvatar",false);
        if (uploadAvatar){
            uploadAvatar();//上传头像
        }

        refreshData();//更新头像


        t_name.setText(preferences.getString("name","昵称"));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            !=PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        WRITE_SDCARD_PERMISSION_REQUEST_CODE);

                            }
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        File file = new File(Environment.getExternalStorageDirectory(),"avatar.jpg");
        bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        img_head.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_SDCARD_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getContext(), "读写内存卡内容权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void uploadAvatar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileName = preferences.getString("id","")+".jpg";
                    OkHttpClient client = new OkHttpClient();
                    File file = new File("//sdcard/avatar.jpg");
                    RequestBody fileBody = RequestBody.create(MediaType.parse("file/*"),file);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_no",preferences.getString("user_id",""))
                            .addFormDataPart("file",fileName,fileBody)
                            .addFormDataPart("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.UPLOAD_AVATAR_PATH))
                            .post(requestBody)
                            .build();

                   client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("lfq" ,"onFailure");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String result = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(result);
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        Log.d("上传头像",result);
                                        editor.putBoolean("uploadAvatar",false);
                                        editor.apply();
                                    }else {
                                        Toast.makeText(getContext(), "登录失效！请重新登录", Toast.LENGTH_SHORT).show();
                                        editor.putBoolean("isLogin",false);
                                        editor.apply();
                                        startActivity(new Intent(getContext(),LogInActivity.class));
                                        getActivity().finish();

                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("lfq" ,response.message() + " error : body " + response.body().string());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 刷新页面信息
     */
    private void refreshData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_no",preferences.getString("user_id",""))
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.GET_AVATAR_PATH))
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("lfq" ,"onFailure");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    String result = response.body().string();
                                    Log.d("s",result);
                                    JSONObject object = new JSONObject(result);
                                    int state = object.getInt("state");
                                    if (state == 0){
                                        //图片保存在本地
                                    }else {
                                        Toast.makeText(getContext(), "登录失效！请重新登录", Toast.LENGTH_SHORT).show();
                                        editor.putBoolean("isLogin",false);
                                        editor.apply();
                                        startActivity(new Intent(getContext(),LogInActivity.class));
                                        getActivity().finish();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("更新头像出错" ,response.message() + " error : body " + response.body().string());
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
