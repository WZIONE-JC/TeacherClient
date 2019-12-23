package com.example.teacherclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class AddCourse extends AppCompatActivity {
    private int col;
    private int row;
    private Button button;
    private Spinner courseDay;
    private Spinner courseTime;
    private ImageView back;
    private EditText name;
    private EditText classroom;
    private EditText teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseDay = (Spinner)findViewById(R.id.course_day_1);
        courseTime = (Spinner)findViewById(R.id.course_time_1);
        button = (Button)findViewById(R.id.button_add_course);
        back = (ImageView)findViewById(R.id.back_to_course_table);
        name = (EditText)findViewById(R.id.edit_course_name);
        classroom = (EditText)findViewById(R.id.edie_course_classroom);
        teacher = (EditText)findViewById(R.id.edie_course_teacher);

        String[] day = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        String[] time = {"第一节(8:00~9:50)",
                "第二节(10:10~12:00)",
                "第三节(12:10~14:00)",
                "第四节(14:10~16:00)",
                "第五节(16:20~18:10)",
                "第六节(19:00~20:50)",
                "第七节(21:10~22:00)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddCourse.this,R.layout.support_simple_spinner_dropdown_item,day);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,time);
        courseDay.setAdapter(adapter);
        courseTime.setAdapter(adapter1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(AddCourse.this,"课程名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (classroom.getText().toString().equals("")){
                    Toast.makeText(AddCourse.this,"上课教室不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                col = courseDay.getSelectedItemPosition() + 1;
                row = courseTime.getSelectedItemPosition() + 1;
                Log.d("col", String.valueOf(col));
                Log.d("row",String.valueOf(row));
                List<Course> courses = LitePal.
                        where("weekday = ?",String.valueOf(col)).
                        where("time = ?",String.valueOf(row)).find(Course.class);
                if (courses == null || courses.size() == 0){

                }else {
                    for (int i = 0; i < courses.size(); i++) {
                        courses.get(i).delete();
                    }
                }
                Course course = new Course();
                course.setName(name.getText().toString());
                course.setPlace(classroom.getText().toString());
                course.setTeacher(teacher.getText().toString());
                course.setWeekday(String.valueOf(col));
                course.setTime(String.valueOf(row));
                course.save();
                Toast.makeText(AddCourse.this,"课程添加成功",Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
