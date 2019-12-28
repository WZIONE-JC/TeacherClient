package com.example.teacherclient;

import org.litepal.crud.LitePalSupport;

public class CourseFile extends LitePalSupport {
    private String fileNo;
    private String courseNo;
    private String fileName;

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
