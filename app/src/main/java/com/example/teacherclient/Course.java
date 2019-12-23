package com.example.teacherclient;

import org.litepal.crud.LitePalSupport;

public class Course extends LitePalSupport {
    private String weekday;
    private String time;
    private String name;
    private String place;
    private String teacher;

    public String getWeekday() {
        return weekday;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
