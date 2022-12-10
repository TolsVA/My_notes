package com.example.my_notes.domain;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.my_notes.db.DbManager;

import java.util.List;

public class InMemoryRepository implements NotesRepository {

    private final DbManager dbManager;

    public List<Note> notes;

    public InMemoryRepository(Context context) {
        dbManager = new DbManager(context);
        dbManager.openDb();
    }

    @Override
    public List<Note> getAllNotes() {
        notes = dbManager.getFromDb();
        return notes;
    }

    @Override
    public Note addNote(Note note) {
        return dbManager.insertToDb(note);
    }

    @Override
    public void clearDb() {
        dbManager.clearDb();
    }

    @Override
    public void upgradeNote(Note note) {
        dbManager.upgradeEntry(note);
    }

    public void deleteIndex(long index) {
        dbManager.deleteEntry(index);
    }

    @Override
    public void closeDb() {
        dbManager.closeDb();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> searchDb(String text) {
        return dbManager.searchDb ( text );
    }
}
