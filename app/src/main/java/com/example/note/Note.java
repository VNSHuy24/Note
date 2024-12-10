package com.example.note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private String id;
    private String title;
    private String body;
    private String createdAt;

    public Note(String title, String body) {
        this.id = this.createdAt = getTimeNow();
        this.title = title;
        this.body = body;
    }

    public Note(String id, String title, String body, String createdAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        updateTime();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateTime();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        updateTime();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        updateTime();
    }

    private void updateTime() {
        this.createdAt = getTimeNow();
    }

    static String getTimeNow(){
        // Sử dụng SimpleDateFormat để lấy thời gian hiện tại
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
        String timeNow = formatter.format(new Date());
        return timeNow;
    }
}
