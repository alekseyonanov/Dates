package com.nollpointer.dates;

public class Date {
    private String date;
    private String event;
    private String request;
    private int type;

    public Date(String date, String event, String request, int type) {
        this.date = date;
        this.event = event;
        this.request = request;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
