package com.nollpointer.dates;

import android.os.Parcel;
import android.os.Parcelable;

public class Date implements Parcelable{
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

    protected Date(Parcel in) {
        date = in.readString();
        event = in.readString();
        request = in.readString();
        type = in.readInt();
    }

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

    public boolean isSameDate(Date date){
        return this.getDate().equals(date.getDate());
    }
}

