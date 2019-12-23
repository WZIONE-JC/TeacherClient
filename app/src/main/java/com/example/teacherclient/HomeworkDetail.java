package com.example.teacherclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeworkDetail extends AppCompatActivity implements View.OnClickListener{
    private TextView text_hw_title;
    private TextView text_hw_detail;
    private TextView text_hw_duetime;
    private TextView text_hw_grade;
    private EditText edit_hw_answer;
    private Button btn_upload;
    private Button btn_submit;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        initview();
    }

    private void initview() {
        btn_upload=findViewById(R.id.upload);
        btn_submit=findViewById(R.id.submit);
        text_hw_title=(TextView)findViewById(R.id.hw_title);
        text_hw_detail=(TextView)findViewById(R.id.hw_detail);
        text_hw_duetime=(TextView)findViewById(R.id.hw_duetime);
        text_hw_grade=(TextView)findViewById(R.id.hw_grade);
        edit_hw_answer=findViewById(R.id.hw_answer);
        img_back=findViewById(R.id.back);

        btn_upload.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.upload://上传
                break;
            case R.id.submit://提交
                break;
        }
    }

}
