package com.nollpointer.dates.practise;

import android.os.Parcel;
import android.os.Parcelable;

public class PractiseResult implements Parcelable {

    private String question;
    private boolean isCorrect;

    public PractiseResult(String question, boolean isCorrect) {
        this.question = question;
        this.isCorrect = isCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    protected PractiseResult(Parcel in) {
        question = in.readString();
        isCorrect = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeByte((byte) (isCorrect ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PractiseResult> CREATOR = new Parcelable.Creator<PractiseResult>() {
        @Override
        public PractiseResult createFromParcel(Parcel in) {
            return new PractiseResult(in);
        }

        @Override
        public PractiseResult[] newArray(int size) {
            return new PractiseResult[size];
        }
    };
}