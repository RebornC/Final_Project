package com.example.yc.finalproject.model;

public class affair_user {
    private int id;
    private String time;
    private String affair;
    private String deadline;
    private String finish;
    private Integer deadlineInInt;
    private int year, mon, day;

    public affair_user(int id, String time, String affair, String deadline, String finish) {
        this.id = id;
        this.time= time;
        this.affair = affair;
        this.deadline = deadline;
        this.finish = finish;
        this.deadlineInInt = StringToInt(deadline);
    }

    private Integer StringToInt(String date) {
        year = Integer.parseInt(date.substring(0,4));
        mon = Integer.parseInt(date.substring(5,7));
        day = Integer.parseInt(date.substring(8,10));
        return year * mon * day;
    }

    public int getId() {
        return this.id;
    }

    public String getTime() {
        return time;
    }

    public String getAffair() {
        return affair;
    }

    public String getDeadline() {
        return deadline;
    }

    public Integer getDeadlineInInt() {
        return deadlineInInt;
    }

    public String getFinish() {
        return this.finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getYear() {
        return year;
    }

    public int getMon() {
        return mon;
    }
    public int getDay() {
        return day;
    }

}