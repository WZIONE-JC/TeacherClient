package com.example.teacherclient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView;

import org.litepal.LitePal;


public class MoreInfo extends  Activity{

    private int courseNo;
    private List<Announce> announces;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        courseNo = getIntent().getIntExtra("courseNo",-1);
        ImageView back = (ImageView)findViewById(R.id.back_form_more_info);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = findViewById(R.id.listview);
        announces = LitePal.where("courseNo = ?", String.valueOf(courseNo)).order("time desc").find(Announce.class);
        List<Map<String, String>> list = new ArrayList<>();
        for (int i=0; i< announces.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", announces.get(i).getTitle());
            map.put("name", announces.get(i).getContent());
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
                AlertDialog dialog = new AlertDialog.Builder(MoreInfo.this)
                        .setTitle(announces.get(position).getTitle())
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage(announces.get(position).getContent())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }


}
