package com.nollpointer.dates;

import android.os.Parcel;
import android.os.Parcelable;

public class Date implements Parcelable{
    private String date;
    private String event;
    private String request;
    private int type;

    private String month;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean containsMonth(){
        return month != null;
    }

    protected Date(Parcel in) {
        date = in.readString();
        event = in.readString();
        request = in.readString();
        type = in.readInt();
        month = in.readString();
    }

    public boolean isSameDate(Date date){
        return this.getDate().equals(date.getDate());
    }

    public boolean isContinuous (){
        return date.contains("-") || date.contains("–");
    }

    public boolean contains(String query){
        return date.toLowerCase().contains(query.toLowerCase()) || event.toLowerCase().contains(query.toLowerCase());
    }


    //Parcelable implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(date);
        parcel.writeString(event);
        parcel.writeString(request);
        parcel.writeInt(type);
        parcel.writeString(month);
    }

    public static final Parcelable.Creator<Date> CREATOR = new Parcelable.Creator<Date>() {
        @Override
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

}

