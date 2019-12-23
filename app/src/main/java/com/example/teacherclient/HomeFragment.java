package com.example.teacherclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {


    private LinearLayout myClassroom;
    private LinearLayout joinClassroom;
    private Button moreCourse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCourseTable();

        myClassroom = (LinearLayout)getActivity().findViewById(R.id.my_classroom);
        myClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to to my class activity
                startActivity(new Intent(getActivity(),MyClass.class));
            }
        });

        joinClassroom = (LinearLayout)getActivity().findViewById(R.id.join_classroom);
        joinClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to join class activity
                startActivity(new Intent(getActivity(),JoinClass.class));
            }
        });

        moreCourse = (Button)getActivity().findViewById(R.id.more_course);
        moreCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CourseTable.class));
//               go to course_table activity
//
            }
        });
    }

    private void initCourseTable(){
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String day;
        if (week == 0){
            day = "7";
        }else {
            day = String.valueOf(week);
        }

        List<Course> courses = LitePal.select("time","name","place")
                .where("weekday = ?",day).find(Course.class);

        String[] times = {"8:00~9:50",
                "10:10~12:00",
                "12:10~14:00",
                "14:10~16:00",
                "16:20~18:10",
                "19:00~20:50",
                "21:10~22:00"};

        int number = 0;

        TextView textTip = (TextView)getActivity().findViewById(R.id.text_no_course_tip);

        //隐藏课程
        for (int i = 1; i < 4; i++) {
            Resources resources = getResources();
            String id= "layout_course_" + i;

            int resId = resources.getIdentifier(id,"id",getActivity().getPackageName());
            LinearLayout layout = (LinearLayout) getActivity().findViewById(resId);

            layout.setVisibility(View.GONE);
        }
        if(courses.size()>0){
            textTip.setVisibility(View.GONE);
        }
        for (Course c : courses) {
            number++;
            if (number>3){
                break;
            }
            Resources resources = getResources();
            String id1 = "today_course_" + number + "_time";
            String id2 = "today_course_" + number + "_name";
            String id3 = "today_course_" + number + "_place";

            int resId1 = resources.getIdentifier(id1,"id",getActivity().getPackageName());
            int resId2 = resources.getIdentifier(id2,"id",getActivity().getPackageName());
            int resId3 = resources.getIdentifier(id3,"id",getActivity().getPackageName());
            TextView textTime = (TextView)getActivity().findViewById(resId1);
            TextView textName = (TextView)getActivity().findViewById(resId2);
            TextView textPlace = (TextView)getActivity().findViewById(resId3);


            String id= "layout_course_" + number;

            int resId = resources.getIdentifier(id,"id",getActivity().getPackageName());
            LinearLayout layout = (LinearLayout) getActivity().findViewById(resId);

            layout.setVisibility(View.VISIBLE);


            String time = c.getTime();
            String name = c.getName();
            String place = c.getPlace();
            textTime.setText(times[Integer.parseInt(time)-1]);
            textName.setText(name);
            textPlace.setText(place);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initCourseTable();
    }
}
