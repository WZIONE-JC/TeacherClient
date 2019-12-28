package com.example.teacherclient;

import org.litepal.crud.LitePalSupport;

public class Announce extends LitePalSupport {
    private int announcementNo;
    private String courseNo;
    private String title;
    private String content;
    private String time;

    public int getAnnouncementNo() {
        return announcementNo;
    }

    public void setAnnouncementNo(int announcementNo) {
        this.announcementNo = announcementNo;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
