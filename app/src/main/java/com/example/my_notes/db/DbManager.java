package com.example.my_notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private final DbHelper dbHelper;
    private SQLiteDatabase db;


    // Определить проекцию, указывающую, какие столбцы из базы данных
    // вы фактически будете использовать после этого запроса.
    String[] projection = {
            BaseColumns._ID,
            Constants.FeedEntryNote.NOTE_TITLE,
            Constants.FeedEntryNote.NOTE_CONTENT,
            Constants.FeedEntryNote.NOTE_DATA
    };

    //Отфильтровать результаты, WHERE "название" = "Мое название"
    String selection = Constants.FeedEntryNote.NOTE_TITLE + " = ?";
    String[] selectionArgs = { "My Title" };

    // Как вы хотите, чтобы результаты сортировались в результирующем курсоре
    String sortOrder =
            Constants.FeedEntryNote.NOTE_ID + " DESC";


    public DbManager(Context context) {
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
        cv.put( Constants.FeedEntryNote.NOTE_GROUP_ID, note.getFolderName ());
        // Вставляем новую строку, возвращая значение первичного ключа новой строки
        long newRowId = db.insert( Constants.FeedEntryNote.TABLE_NOTE, null, cv);
        cv.put( Constants.FeedEntryNote.NOTE_ID, newRowId);

        note.setIndex(newRowId);
        return note;
    }

    public void upgradeEntry(Note note) {
        ContentValues cv = new ContentValues();
        cv.put( Constants.FeedEntryNote.NOTE_ID, note.getIndex());
        cv.put( Constants.FeedEntryNote.NOTE_TITLE, note.getTitle());
        cv.put( Constants.FeedEntryNote.NOTE_CONTENT, note.getText());
        cv.put( Constants.FeedEntryNote.NOTE_DATA, note.getData());
        cv.put( Constants.FeedEntryNote.NOTE_GROUP_ID, note.getFolderName ());
        String where = Constants.FeedEntryNote.NOTE_ID + "=" + note.getIndex();
        db.update( Constants.FeedEntryNote.TABLE_NOTE, cv, where, null);
    }

    public void deleteEntry(long index) {
        db.delete( Constants.FeedEntryNote.TABLE_NOTE, Constants.FeedEntryNote.NOTE_ID + " = " + index, null);
//        Toast.makeText(context, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }

    public List<Note> getFromDb(){
        Cursor cursor = db.query(
                Constants.FeedEntryNote.TABLE_NOTE,    // Таблица для запроса
                null,           // Массив возвращаемых столбцов
                null,          // Столбцы для предложения WHERE
                null,       // значения для предложения WHERE
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

            int folderName = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryNote.NOTE_GROUP_ID));

            Note note = new Note(index, title, text, data, folderName);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        group.setIndex(newRowId);
        return group;
    }

    public List<Group> getFromDbGroup(){
        Cursor cursor = db.query(
                Constants.FeedEntryGroup.TABLE_GROUP,    // Таблица для запроса
                null,           // Массив возвращаемых столбцов
                null,          // Столбцы для предложения WHERE
                null,       // значения для предложения WHERE
                null,          // не группировать строки
                null,            // не фильтровать по группам строк
                null         // Порядок сортировки
        );

        List<Group> groups = new ArrayList<>();
        while(cursor.moveToNext()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));

            int icon = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));

            Group group = new Group (index, name, icon);
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

    public Group searchByGroupNameDbGroup(String folderName) {

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
        );

        Group group = null;
        while(cursor.moveToNext()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ID));

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_NAME));

            int icon = cursor.getInt (
                    cursor.getColumnIndexOrThrow( Constants.FeedEntryGroup.GROUP_ICON));

            group = new Group (index, name, icon);
        }
        cursor.close();
        return group;

    }
}
