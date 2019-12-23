package com.example.teacherclient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView;


public class MoreInfo extends  Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        ImageView back = (ImageView)findViewById(R.id.back_form_more_info);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = findViewById(R.id.listview);

        List<Map<String, String>> list = new ArrayList<>();
        for (int i=0; i< 60; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", "编号" + i +1);
            map.put("name", "通知" + i + 100);
            list.add(map);
        }


        // 使用SimpleAdapter适配器
        ListAdapter listAdapter =
                new SimpleAdapter(this, // 上下文
                        list,         // 数据
                        android.R.layout.simple_list_item_2, // 使用系统的布局
                        new String[]{"id", "name"}, // 设置Map的key，数据从哪里来
                        new int[]{android.R.id.text1, android.R.id.text2}); // 系统布局的两个控件ID，数据设置到那里去

        // 把适配器给ListView
        listView.setAdapter(listAdapter);

        /**
         * List条目点击监听事件
         * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MoreInfo.this,ShowMoreInfo.class);
                startActivity(intent);
            }
        });
    }

}
