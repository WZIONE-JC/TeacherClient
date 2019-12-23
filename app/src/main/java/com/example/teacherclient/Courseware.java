package com.example.teacherclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Courseware extends Activity {
    private ListView listv;
    private	String[] classwarename={"Chapter2 XXXXXX","Chapter2 XXXXXXX"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courseware);
        listv = (ListView) findViewById(R.id.listView);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<classwarename.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("classname", classwarename[i]);
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
                        Toast.makeText(Courseware.this,"下载键被点击",Toast.LENGTH_SHORT).show();
                    }
                });
                return view;
            }
        };
        listv.setAdapter(simleAdapter);

        ImageView back = (ImageView)findViewById(R.id.back_from_courseware);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}