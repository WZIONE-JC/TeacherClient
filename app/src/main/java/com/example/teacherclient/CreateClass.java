package com.example.teacherclient;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class CreateClass extends Activity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        back = (ImageView) findViewById(R.id.back_form_join_class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}