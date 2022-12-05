package com.example.my_notes.db;

import android.provider.BaseColumns;

public final class Constants {

    private Constants() {
    }

    public static class FeedEntryNote implements BaseColumns {
        public static final String TABLE_NOTE = "table_note";
        public static final String NOTE_ID = "note_id";
        public static final String NOTE_TITLE = "note_title";
        public static final String NOTE_CONTENT = "note_content";
        public static final String NOTE_DATA = "note_data";
        public static final String NOTE_GROUP_ID = "note_group_id";
    }

    public static class FeedEntryGroup implements BaseColumns {
        public static final String TABLE_GROUP = "table_group";
        public static final String GROUP_ID = "group_id";
        public static final String GROUP_NAME = "group_name";
        public static final String GROUP_ICON = "group_icon";
    }

    public static final String SQL_CREATE_ENTRIES_NOTE =
            "CREATE TABLE " + FeedEntryNote.TABLE_NOTE + " (" +
                    FeedEntryNote.NOTE_ID + " INTEGER PRIMARY KEY," +
                    FeedEntryNote.NOTE_TITLE + " TEXT NOT NULL," +
                    FeedEntryNote.NOTE_CONTENT + " TEXT NOT NULL," +
                    FeedEntryNote.NOTE_DATA + " INTEGER NOT NULL," +
                    FeedEntryNote.NOTE_GROUP_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + FeedEntryNote.NOTE_GROUP_ID + ") REFERENCES " + FeedEntryGroup.TABLE_GROUP +
                    "(" + FeedEntryGroup.GROUP_ID + "))";

    public static final String SQL_CREATE_ENTRIES_GROUP =
            "CREATE TABLE " + FeedEntryGroup.TABLE_GROUP + " (" +
                    FeedEntryGroup.GROUP_ID + " INTEGER PRIMARY KEY," +
                    FeedEntryGroup.GROUP_NAME + " TEXT NOT NULL UNIQUE," +
                    FeedEntryGroup.GROUP_ICON + " INTEGER NOT NULL)";

    public static final String SQL_DELETE_ENTRIES_NOTE =
            "DROP TABLE IF EXISTS " + FeedEntryNote.TABLE_NOTE;

    public static final String SQL_DELETE_ENTRIES_GROUP =
            "DROP TABLE IF EXISTS " + FeedEntryGroup.TABLE_GROUP;

}

