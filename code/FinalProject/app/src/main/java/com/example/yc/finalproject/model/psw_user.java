package com.example.yc.finalproject.model;

public class psw_user {

    private String login;
    private String id;
    private String password;

    public psw_user(String login, String id, String password) {
        this.login = login;
        this.id = id;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

}