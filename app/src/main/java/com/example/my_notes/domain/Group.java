package com.example.my_notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

    private long id;

    private String name;

    private int icon;

    private final int count;

    public Group(long id, String name, int icon, int count) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.count = count;
    }

    protected Group(Parcel in) {
        id = in.readLong ( );
        name = in.readString ( );
        icon = in.readInt ( );
        count = in.readInt ( );
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong ( id );
        dest.writeString ( name );
        dest.writeInt ( icon );
        dest.writeInt ( count );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group> ( ) {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group ( in );
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCount() {
        return count;
    }
}
