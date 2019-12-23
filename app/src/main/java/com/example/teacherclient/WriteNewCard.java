package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WriteNewCard extends AppCompatActivity {

    private ImageView back;
    private Button send;
    private EditText type;
    private EditText title;
    private EditText text;
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
                TalkCardTable talkCard = new TalkCardTable();
                talkCard.setWriter("litterboys");
                talkCard.setReviews("0");
                talkCard.setType(type.getText().toString());
                talkCard.setTitle(title.getText().toString());
                talkCard.setContent(text.getText().toString());
                talkCard.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                talkCard.save();
                Toast.makeText(WriteNewCard.this,"发布成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}
