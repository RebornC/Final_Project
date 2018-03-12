package com.example.yc.finalproject.model;

/**
 * Created by kaiminglee on 5/1/2018.
 */

public class birthday_user {
    private int id;
    private String name;
    private String birth;
    private String gift;
    private Integer birthInInt;
    private int mon, day;

    public birthday_user(int id, String name, String birth, String gift) {
        this.id = id;
        this.name= name;
        this.birth = birth;
        this.gift = gift;
        this.birthInInt = StringToInt(birth);
    }

    private Integer StringToInt(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        mon = month;
        int day = Integer.parseInt(date.substring(8,10));
        this.day = day;
        return month * day;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBirth() {
        return this.birth;
    }

    public String getGift() {
        return this.gift;
    }

    public Integer getBirthInInt() {
        return this.birthInInt;
    }

    public int getMon() {
        return mon;
    }

    public int getDay() {
        return day;
    }

}
