package com.example.teacherclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Courseware extends Activity {
    private TextView uploadFile;
    private Handler handler;
    private Uri fileUri;
    private String path;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private List<CourseFile> courseFiles;


    private ListView listv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courseware);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        File file = new File(fileUri.getPath());
                        AlertDialog dialog = new AlertDialog.Builder(Courseware.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("提示")
                                .setMessage("确定上传文件" + file.getName())
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        uploadFile();//上传文件
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                        break;
                    case 1:
                        Toast.makeText(Courseware.this,"文件上传成功",Toast.LENGTH_SHORT).show();
                        getFileInfo();
                }
            }
        };

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        uploadFile = (TextView)findViewById(R.id.upload_file);
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


        listv = (ListView) findViewById(R.id.listView);
        upData();

        ImageView back = (ImageView)findViewById(R.id.back_from_courseware);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void chooseFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (data == null)
                return;
            fileUri = data.getData();
            path = MyStaticValue.getFilePathByUri(this,fileUri);
            handler.sendEmptyMessage(0);
        }
    }


    private void upData(){
        courseFiles = LitePal.where("courseNo = ?", String.valueOf(getIntent().getIntExtra("courseNo",-1))).find(CourseFile.class);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<courseFiles.size();i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("classname", courseFiles.get(i).getFileName());
            listItems.add(listItem);

        }
        //SimpleAdapter simleAdapter = new SimpleAdapter(Courseware.this, listItems,
        // R.layout.courseware_layout	,new String[]{"classname"},
        // new int[]{R.id.classname1});

        SimpleAdapter simleAdapter = new SimpleAdapter(Courseware.this, listItems,
                R.layout.courseware_layout ,new String[]{"classname"},new int[]{R.id.classname1}){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final int p=position;
                final View view=super.getView(position, convertView, parent);
                ImageView downloadBtn = (ImageView) view.findViewById(R.id.download_courseware);
                downloadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Courseware.this,"文件开始下载",Toast.LENGTH_SHORT).show();
                        downloadFile(p);

                    }
                });
                return view;
            }
        };
        listv.setAdapter(simleAdapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/yiclass");//存储文件夹
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file1 = new File(file, courseFiles.get(position).getFileName());
                    if (!file1.exists()){
                        Toast.makeText(Courseware.this, "文件不存在，请先下载文件！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String type = "";
                    for(int i =0;i<MIME_MapTable.length;i++) {
                        //判断文件的格式
                        if (file1.getPath().toString().contains(MIME_MapTable[i][0].toString())) {
                            type = MIME_MapTable[i][1];
                            break;
                        }
                    }
                    Log.d("type++++++",type);
                    Uri uri;
                    if(Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(Courseware.this, "com.example.teacherclient.fileprovider", file1);
                    } else {
                        uri = Uri.fromFile(file1); // Android 7.0 以前使用原来的方法来获取文件的 Uri
                    }
                    intent.setDataAndType(uri, type);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Courseware.this, "sorry附件不能打开，请下载相关软件！",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private String [][]  MIME_MapTable={
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
    private void uploadFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    File file = new File(path);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"),file);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("user_no",preferences.getString("id",""))
                            .addFormDataPart("course_no",String.valueOf(getIntent().getIntExtra("courseNo",-1)))
                            .addFormDataPart("file",file.getName(),fileBody)
                            .addFormDataPart("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.UPLOAD_FILE))
                            .post(requestBody)
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
                                        handler.sendEmptyMessage(1);
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


    private void getFileInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("course_no",String.valueOf(getIntent().getIntExtra("courseNo",-1)))
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

    private void downloadFile(final int i){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("course_no",courseFiles.get(i).getCourseNo())
                            .add("file_no",courseFiles.get(i).getFileNo())
                            .add("file_name",courseFiles.get(i).getFileName())
                            .add("token",preferences.getString("token",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(new URL(MyStaticValue.DOWNLOAD_FILE))
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
                                InputStream inputStream = response.body().byteStream();
                                long content = (long) response.body().contentLength();
                                int len = 0;
                                byte[] buf = new byte[1024];
                                FileOutputStream outputStream = null;
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/yiclass");//存储文件夹
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                File file1 = new File(file, courseFiles.get(i).getFileName());
                                int j = 0;
                                while (file1.exists()){
                                    j++;
                                    file1 = new File(file, "("+j+")"+courseFiles.get(i).getFileName());
                                }

                                try{
                                    outputStream = new FileOutputStream(file1);
                                    while ((len = inputStream.read(buf)) != -1) {
                                        outputStream.write(buf, 0, len);
                                    }
                                    outputStream.flush();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }finally {
                                    try {
                                        if (inputStream != null){
                                            inputStream.close();
                                        }
                                        if (outputStream != null){
                                            outputStream.close();
                                        }
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }

                                Log.d("secc","");
                                Looper.prepare();
                                Toast.makeText(Courseware.this,"文件下载完成",Toast.LENGTH_SHORT).show();
                                Looper.loop();
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
}