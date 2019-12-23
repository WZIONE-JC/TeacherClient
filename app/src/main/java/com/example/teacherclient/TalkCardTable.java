package com.example.teacherclient;

import org.litepal.crud.LitePalSupport;

public class TalkCardTable extends LitePalSupport {
    private String writer;
    private String reviews;
    private String title;
    private String type;
    private String textContent;
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWriter() {
        return writer;
    }

    public String getReviews() {
        return reviews;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return textContent;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.textContent = content;
    }
}
