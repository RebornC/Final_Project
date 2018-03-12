package com.example.yc.finalproject.model;

public class Memo {

    private String memo_name;
    private String memo_intro;
    private int image_id;

    public Memo(String memo_name, String memo_intro, int image_id) {
        this.memo_name = memo_name;
        this.memo_intro = memo_intro;
        this.image_id = image_id;
    }

    public String getMemo_name() {
        return memo_name;
    }

    public String getMemo_intro() {
        return memo_intro;
    }

    public int getImage_id() {
        return image_id;
    }

}
