package com.example.lmslite;

import java.io.Serializable;

public class Video implements Serializable {
    private String title;
    private String description;
    private String url;
    private int duration; // in minutes

    public Video(String title, String description, String url, int duration) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
} 