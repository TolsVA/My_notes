package com.example.my_notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

    private long index;

    private String name;

    private int icon;

    public Group(long index, String name, int icon) {
        this.index = index;
        this.name = name;
        this.icon = icon;
    }

    protected Group(Parcel in) {
        index = in.readLong ( );
        name = in.readString ( );
        icon = in.readInt ( );
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong ( index );
        dest.writeString ( name );
        dest.writeInt ( icon );
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
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


}
