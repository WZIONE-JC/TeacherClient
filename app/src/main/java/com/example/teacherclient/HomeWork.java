package com.example.teacherclient;

import org.litepal.crud.LitePalSupport;

public class HomeWork extends LitePalSupport {
    private int homeworkNo;
    private String courseNo;
    private String title;
    private String content;
    private long deadline;

    public int getHomeworkNo() {
        return homeworkNo;
    }

    public void setHomeworkNo(int homeworkNo) {
        this.homeworkNo = homeworkNo;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
