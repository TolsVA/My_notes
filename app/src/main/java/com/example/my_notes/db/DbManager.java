package com.example.my_notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

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
            Constants.COLUMN_NAME_TITLE,
            Constants.COLUMN_NAME_CONTENT,
            Constants.COLUMN_NAME_DATA
    };

    //Отфильтровать результаты, WHERE "название" = "Мое название"
    String selection = Constants.COLUMN_NAME_TITLE + " = ?";
    String[] selectionArgs = { "My Title" };

    // Как вы хотите, чтобы результаты сортировались в результирующем курсоре
    String sortOrder =
            Constants.COLUMN_NAME_TITLE + " DESC";

    public DbManager(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void openDb() {
        db = dbHelper.getWritableDatabase();
    }

    public Note insertToDb(Note note) {
        ContentValues cv = new ContentValues();
        cv.put( Constants.COLUMN_NAME_TITLE, note.getTitle());
        cv.put( Constants.COLUMN_NAME_CONTENT, note.getText());
        cv.put( Constants.COLUMN_NAME_DATA, note.getData());
        // Вставляем новую строку, возвращая значение первичного ключа новой строки
        long newRowId = db.insert( Constants.TABLE_NAME, null, cv);
        cv.put( Constants._ID, newRowId);

        note.setIndex(newRowId);
        return note;
    }

    public void upgradeEntry(Note note) {
        ContentValues cv = new ContentValues();
        cv.put( Constants._ID, note.getIndex());
        cv.put( Constants.COLUMN_NAME_TITLE, note.getTitle());
        cv.put( Constants.COLUMN_NAME_CONTENT, note.getText());
        cv.put( Constants.COLUMN_NAME_DATA, note.getData());
        String where = Constants._ID + "=" + note.getIndex();
        db.update( Constants.TABLE_NAME, cv, where, null);
    }

    public void deleteEntry(long index) {
        db.delete( Constants.TABLE_NAME, Constants._ID + " = " + index, null);
//        Toast.makeText(context, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }

    public List<Note> getFromDb(){
        Cursor cursor = db.query(
                Constants.TABLE_NAME,    // Таблица для запроса
                null,           // Массив возвращаемых столбцов
                null,          // Столбцы для предложения WHERE
                null,       // значения для предложения WHERE
                null,          // не группировать строки
                null,            // не фильтровать по группам строк
                sortOrder           // Порядок сортировки
        );

        List<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow( Constants._ID));

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.COLUMN_NAME_TITLE));

            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.COLUMN_NAME_CONTENT));

            String data = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.COLUMN_NAME_DATA));

            Note note = new Note(index, title, text, data);
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    public void clearDb() {
        db.delete( Constants.TABLE_NAME, null, null);
    }


    public void closeDb() {
        dbHelper.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Note> searchDb(String _text) {

        String sql = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " +
                Constants.COLUMN_NAME_TITLE + " GLOB " + "\"*" +  _text + "*\"" +
                " OR " + Constants.COLUMN_NAME_CONTENT + " GLOB " + "\"*" +  _text + "*\"";

        Cursor cursor = db.rawQuery ( sql, null, null );

        List<Note> notes = new ArrayList<>();
        while(cursor.moveToNext ()) {

            long index = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Constants._ID));

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(Constants.COLUMN_NAME_TITLE));

            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow(Constants.COLUMN_NAME_CONTENT));

            String data = cursor.getString(
                    cursor.getColumnIndexOrThrow( Constants.COLUMN_NAME_DATA));

            Note note = new Note(index, title, text, data);
            notes.add(note);
        }
        cursor.close();

        return notes;
    }

}
