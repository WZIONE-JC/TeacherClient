package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.List;

public class DiscussItem extends AppCompatActivity {
    private String discussNo;
    private TextView title;
    private TextView content;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_item);

        initData();
    }

    private void initData(){
        discussNo = getIntent().getStringExtra("discussNo");
        title = (TextView)findViewById(R.id.discuss_title);
        content = (TextView)findViewById(R.id.discuss_text);
        back = (ImageView)findViewById(R.id.back_from_discuss);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<TalkCardTable> tables = LitePal.where("discussNo = ?" , discussNo)
                .find(TalkCardTable.class);
        title.setText(tables.get(0).getTitle());
        content.setText(tables.get(0).getContent());
    }
}
