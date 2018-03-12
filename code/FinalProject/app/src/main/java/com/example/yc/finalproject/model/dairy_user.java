package com.example.yc.finalproject.model;

public class dairy_user {

    private String time;
    private String topic;
    private String content;

    public dairy_user(String time, String topic, String content) {
        this.time= time;
        this.topic = topic;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

}