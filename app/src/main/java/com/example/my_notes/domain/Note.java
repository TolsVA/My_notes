package com.example.my_notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{

    private long id;

    private String title;

    private String text;

    private String data;

    private long group_id;

    public Note(long _id, String title, String text, String data, long _group_id) {
        this.id = _id;
        this.title = title;
        this.text = text;
        this.data = data;
        this.group_id = _group_id;
    }

    public long getIndex() {
        return id;
    }

    public void setIndex(long index) {
        this.id = index;
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

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long _group_id) {
        this.group_id = _group_id;
    }

    protected Note(Parcel in) {
        id = in.readLong ( );
        title = in.readString ( );
        text = in.readString ( );
        data = in.readString ( );
        group_id = in.readLong ( );
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong ( id );
        dest.writeString ( title );
        dest.writeString ( text );
        dest.writeString ( data );
        dest.writeLong ( group_id );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note> ( ) {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note ( in );
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
