package com.example.teacherclient;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class Classroom extends LitePalSupport {
    private int courseNo;
    private String courseName;
    private int teachTime;
    private int teachLocation;
    private String teacherNo;
    private String code;


    public int getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(int courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(int teachTime) {
        this.teachTime = teachTime;
    }

    public int getTeachLocation() {
        return teachLocation;
    }

    public void setTeachLocation(int teachLocation) {
        this.teachLocation = teachLocation;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
