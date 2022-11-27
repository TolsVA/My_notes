package com.example.my_notes.db;

public class Constants {
    public static final String TABLE_NAME = "table_name";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_DATA = "data";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Constants.db";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_CONTENT + " TEXT," +
                    COLUMN_NAME_DATA + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

