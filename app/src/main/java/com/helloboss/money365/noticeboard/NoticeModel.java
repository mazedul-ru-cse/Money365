package com.helloboss.money365.noticeboard;

public class NoticeModel {

    String title;
    String date;
    String body;

    public NoticeModel(String title, String date, String body) {
        this.title = title;
        this.date = date;
        this.body = body;
    }

    public NoticeModel(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
