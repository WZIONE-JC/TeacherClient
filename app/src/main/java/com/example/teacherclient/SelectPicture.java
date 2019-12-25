package com.example.teacherclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;

public class SelectPicture extends AppCompatActivity  implements View.OnClickListener{

    private TextView text_take_photo;
    private TextView text_pick_photo;
    private Button btn_cancel;
    private ImageView img_head;

    public static final int REQUEST_TAKE_PHOTO = 1;//请求拍照
    public static final int CHOOSE_PHOTO = 2;
    private static final int TAKE_PHOTO_PERMISSION_REQUEST_CODE = 3; // 拍照的权限处理返回码
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 4; // 读储存卡内容的权限处理返回码
    private static final int CROP_PHOTO_REQUEST_CODE = 5; // 裁剪图片返回的 requestCode
    private String currentPhotoPath;

    private Uri photoUri;
    private Uri photoOutputUri = null;

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);
        initview();

        if(ContextCompat.checkSelfPermission(SelectPicture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(SelectPicture.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }
    }

    private void initview() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        btn_cancel=findViewById(R.id.cancel);
        text_take_photo=(TextView)findViewById(R.id.take_photo);
        text_pick_photo=(TextView)findViewById(R.id.pick_photo);
        //img_head = (ImageView) LayoutInflater.from(SelectPicture.this).inflate(R.layout.fragment_personal, null).findViewById(R.id.h_head);
        btn_cancel.setOnClickListener(this);
        text_take_photo.setOnClickListener(this);
        text_pick_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                this.finish();
                break;
            case R.id.take_photo:
                if(ContextCompat.checkSelfPermission(SelectPicture.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    /*
                     * 下面是对调用相机拍照权限进行申请
                     */
                    ActivityCompat.requestPermissions(SelectPicture.this,
                            new String[]{Manifest.permission.CAMERA,}, TAKE_PHOTO_PERMISSION_REQUEST_CODE);
                } else {
                    takePhoto();
                }
                break;
            case R.id.pick_photo:
                pickPhoto();
                break;
        }
    }

    //打开相册
    private void takePhoto() {


        File file = new File(getExternalCacheDir(),"avatar.jpg");
        try {
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24) {
            photoUri = FileProvider.getUriForFile(this, "com.example.teacherclient.fileprovider", file);
        } else {
            photoUri = Uri.fromFile(file); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }

        // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照所得照片的输出目录
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);

    }

    //拍照
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 启动系统相册
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case TAKE_PHOTO_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;

            case WRITE_SDCARD_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "读写内存卡内容权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    cropPhoto(photoUri);
                    break;
                case CHOOSE_PHOTO:
                    cropPhoto(data.getData());
                case CROP_PHOTO_REQUEST_CODE:
                    //上传图片
                    editor.putBoolean("uploadAvatar",true);
                    editor.apply();
                    finish();
                    break;

            }

        }
    }


    private void cropPhoto(Uri inputUri) {
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据Uri 和类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropPhotoIntent.putExtra("aspectX", 1);
        cropPhotoIntent.putExtra("aspectY", 1);
        cropPhotoIntent.putExtra("outputX", 300);
        cropPhotoIntent.putExtra("outputY", 300);
        cropPhotoIntent.putExtra("scale", true);
        // 设置图片的最终输出目录
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoOutputUri = Uri.parse("file:///sdcard/"+ preferences.getString("id","")+".jpg"));
        cropPhotoIntent.putExtra("return-data", false);
        cropPhotoIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropPhotoIntent.putExtra("noFaceDetection", true); // no face detection
        cropPhotoIntent = Intent.createChooser(cropPhotoIntent, "裁剪图片");

        startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
    }


}