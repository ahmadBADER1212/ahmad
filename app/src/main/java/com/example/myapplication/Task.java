package com.example.myapplication;

import java.text.DateFormat;
import java.util.Date;

public class Task {
    private String title;
    private boolean isDone;
    private Long created;

    public Task(String title) {
        this.title = title;
        this.isDone = false;
        this.created = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Long getCreated() {
        return created;
    }

    public String getFormattedDateTime() {
        Date date = new Date(created);
        return DateFormat.getDateTimeInstance().format(date);
    }

    @Override
    public String toString() {
        return title;
    }
}
