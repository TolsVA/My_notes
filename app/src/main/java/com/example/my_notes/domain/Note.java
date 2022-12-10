package com.example.my_notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{

    private long index;

    private String title;

    private String text;

    private String data;

    public Note(long index, String title, String text, String data) {
        this.index = index;
        this.title = title;
        this.text = text;
        this.data = data;

    }

    protected Note(Parcel in) {
        index = in.readLong();
        title = in.readString();
        text = in.readString();
        data = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(index);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
