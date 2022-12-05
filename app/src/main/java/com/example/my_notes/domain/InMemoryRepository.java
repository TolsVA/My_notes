package com.example.my_notes.domain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.my_notes.db.DbManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements NotesRepository {

    private final DbManager dbManager;
//    private final DbManagerCroup dbManagerGroup;

    public List<Note> notes;

    public List<Group> groups;

    public InMemoryRepository(Context context) {
//        dbManagerGroup = new DbManagerCroup ( context );
//        dbManagerGroup.openDb ();
        dbManager = new DbManager(context);
        dbManager.openDb();
    }

    @Override
    public List<Note> getAllNotes() {
        notes = dbManager.getFromDb();
        return notes;
    }

    @Override
    public List<Group> getAllGroup() {
        groups = dbManager.getFromDbGroup ();
        return groups;
    }


    @Override
    public Note addNote(Note note) {
        return dbManager.insertToDbNote (note);
    }

    @Override
    public Group addGroup(Group group) {
        return dbManager.insertToDbGroup (group);
    }

    @Override
    public int checkGroupForDbGroup(String text) {
        return dbManager.checkGroupForDbGroup ( text );
    }

    @Override
    public Group searchByGroupName(String folderName) {
        return dbManager.searchByGroupNameDbGroup ( folderName );
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
//        dbManagerGroup.closeDb();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> searchDb(String text) {
        return dbManager.searchDb ( text );
    }

}
