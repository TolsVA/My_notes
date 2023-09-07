package com.example.my_notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private final DbHelper dbHelper;
    private SQLiteDatabase db;
    Context context;

    String sortOrder =
            Constants.FeedEntryNote.NOTE_ID + " DESC";

    public DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void openDb() {
        db = dbHelper.getReadableDatabase();
    }

    public Note insertToDbNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put( Constants.FeedEntryNote.NOTE_TITLE, note.getTitle());
        cv.put( Constants.FeedEntryNote.NOTE_CONTENT, note.getText());
        cv.put( Constants.FeedEntryNote.NOTE_DATA, note.getData());
        cv.put( Constants.FeedEntryNote.NOTE_GROUP_ID, note.getGroup_id ());
        // Вставляем новую строку, возвращая значение первичного ключа новой строки
        long newRowId = db.insert( Constants.FeedEntryNote.TABLE_NOTE, null, cv);
        cv.put( Constants.FeedEntryNote.NOTE_ID, newRowId);

        note.setId(newRowId);
        return note;
    }

    public void upgradeEntry(Note note) {
        ContentValues cv = new ContentValues();
        cv.put( Constants.FeedEntryNote.NOTE_ID, note.getId());
        cv.put( Constants.FeedEntryNote.NOTE_TITLE, note.getTitle());
        cv.put( Constants.FeedEntryNote.NOTE_CONTENT, note.getText());
        cv.put( Constants.FeedEntryNote.NOTE_DATA, note.getData());
        cv.put( Constants.FeedEntryNote.NOTE_GROUP_ID, note.getGroup_id ());
        String where = Constants.FeedEntryNote.NOTE_ID + "=" + note.getId();
        db.update( Constants.FeedEntryNote.TABLE_NOTE, cv, where, null);
    }

    public void deleteEntry(long index) {
        db.delete( Constants.FeedEntryNote.TABLE_NOTE, Constants.FeedEntryNote.NOTE_ID
                + " = " + index, null);
    }

    public List<Note> getFromDb(long group_id){
        String selection;
        String[] selectionArgs;
        if (group_id == 0) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = Constants.FeedEntryNote.NOTE_GROUP_ID + " = ?";
            selectionArgs = new String[]{String.valueOf ( group_id )};
        }
        Cursor cursor = db.query(
                Constants.FeedEntryNote.TABLE_NOTE,    // Таблица для запроса
                null,           // Массив возвращаемых столбцов
                selection,          // Столбцы для предложения WHERE
                selectionArgs,       // значения для предложения WHERE
                null,          // не группировать строки
                null,            // не фильтровать по группам строк
                sortOrder          // Порядок сортировки
        );

        List<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_ID));

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_TITLE));

            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_CONTENT));

            String data = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_DATA));

            long folder_group_id = cursor.getLong (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_GROUP_ID));

            Note note = new Note(index, title, text, data, folder_group_id);
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    public void clearDb() {
        db.delete( Constants.FeedEntryNote.TABLE_NOTE, null, null);
        db.delete( Constants.FeedEntryGroup.TABLE_GROUP, null, null);
    }


    public void closeDb() {
        dbHelper.close();
    }

    public List<Note> searchDb(String _text) {

        String sql = "SELECT * FROM " + Constants.FeedEntryNote.TABLE_NOTE + " WHERE " +
                Constants.FeedEntryNote.NOTE_TITLE + " GLOB " + "\"*" +  _text + "*\"" +
                " OR " + Constants.FeedEntryNote.NOTE_CONTENT + " GLOB " + "\"*" +  _text + "*\"";

        Cursor cursor = db.rawQuery ( sql, null, null );

        List<Note> notes = new ArrayList<>();
        while(cursor.moveToNext ()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_ID));

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_TITLE));

            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_CONTENT));

            String data = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_DATA));

            Note note = new Note(index, title, text, data, 1);
            notes.add(note);
        }
        cursor.close();

        return notes;
    }

    public Group insertToDbGroup(Group group) {
        ContentValues cv = new ContentValues();
        cv.put( Constants.FeedEntryGroup.GROUP_NAME, group.getName ());
        cv.put( Constants.FeedEntryGroup.GROUP_ICON, group.getIcon ());
        // Вставляем новую строку, возвращая значение первичного ключа новой строки
        long newRowId = db.insert( Constants.FeedEntryGroup.TABLE_GROUP, null, cv);
        cv.put( Constants.FeedEntryGroup.GROUP_ID, newRowId);

        group.setId(newRowId);
        return group;
    }

    public List<Group> getFromDbGroup(){
        String sql = "SELECT " + Constants.FeedEntryGroup.TABLE_GROUP + ".*, " +
                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + ", " +
                "COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP +
                " LEFT JOIN " + Constants.FeedEntryNote.TABLE_NOTE +
                " ON " + Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + " = " +
                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_ID +
                " GROUP BY " + Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + ";";

        Cursor cursor = db.rawQuery ( sql, null, null );

        List<Group> groups = new ArrayList<>();
        while(cursor.moveToNext()) {

            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));

            int icon = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));

            int note_group_id = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_GROUP_ID));

            int count = cursor.getInt (
                    cursor.getColumnIndexOrThrow( "count" ));

            if (note_group_id == 0) {
                count = 0;
            }

            Group group = new Group (id, name, icon, count);
            groups.add(group);
        }
        cursor.close();
        return groups;
    }

    public int checkGroupForDbGroup(String _text) {

        String sql = "SELECT COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP + " WHERE " +
                Constants.FeedEntryGroup.GROUP_NAME + " = \"" + _text + "\"";

        Cursor cursor = db.rawQuery ( sql, null, null );

        int index = 0;
        while(cursor.moveToNext ()) {
            index = cursor.getInt (
                    cursor.getColumnIndexOrThrow ( "count" ));
        }
        cursor.close();

        return index;
    }


    public List<Group> searchByGroupNameDbGroup(String folderName) {

        String sql = "SELECT " + Constants.FeedEntryGroup.TABLE_GROUP + ".*, " +
                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + ", " +
                "COUNT(*) as count FROM " + Constants.FeedEntryGroup.TABLE_GROUP +
                " INNER JOIN " + Constants.FeedEntryNote.TABLE_NOTE + " ON " +
                Constants.FeedEntryNote.TABLE_NOTE + "." + Constants.FeedEntryNote.NOTE_GROUP_ID + " = " +
                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_ID + " AND " +
                Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + " = '" + folderName + "' " +
                " GROUP BY " + Constants.FeedEntryGroup.TABLE_GROUP + "." + Constants.FeedEntryGroup.GROUP_NAME + ";";

        Cursor cursor = db.rawQuery ( sql, null, null );
/*
        String selection = Constants.FeedEntryGroup.GROUP_NAME + " = ?";
        String[] selectionArgs = { folderName };
        Cursor cursor = db.query(
                Constants.FeedEntryGroup.TABLE_GROUP,    // Таблица для запроса
                null,           // Массив возвращаемых столбцов
                selection ,          // Столбцы для предложения WHERE
                selectionArgs,       // значения для предложения WHERE
                null,          // не группировать строки
                null,            // не фильтровать по группам строк
                null         // Порядок сортировки
        );*/

        List<Group> groups = new ArrayList<>();
        while(cursor.moveToNext()) {

            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));

            int icon = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));

            int count = cursor.getInt (
                    cursor.getColumnIndexOrThrow( "count" ));

            Group group = new Group (id, name, icon, count);
            groups.add(group);
        }
        cursor.close();
        return groups;

    }

    public void deleteEntryGroup(long index) {
        db.delete( Constants.FeedEntryGroup.TABLE_GROUP, Constants.FeedEntryGroup.GROUP_ID
                + " = " + index, null);
    }

    public void deleteIndexNoteGroupId(long position) {
        db.delete( Constants.FeedEntryNote.TABLE_NOTE, Constants.FeedEntryNote.NOTE_GROUP_ID
                + " = " + position, null);
    }
}
