package com.example.my_notes.domain;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.my_notes.db.DbManager;
import java.util.List;

public class InMemoryRepository implements NotesRepository {

    private final DbManager dbManager;

    public List<Note> notes;

    public List<Group> groups;

/*    private Executor executor = Executors.newSingleThreadExecutor ();
    private Handler handler = new Handler ( Looper.getMainLooper () );*/

    public InMemoryRepository(Context context) {
        dbManager = new DbManager(context);
        dbManager.openDb();
    }

    @Override
    public List<Note> getAllNotes(long group_id) {
        notes = dbManager.getFromDb(group_id);
        return notes;
    }

/*    @Override
    public void getAllNotes(long group_id, Callback<List<Note>> callback) {
        executor.execute ( new Runnable ( ) {
            @Override
            public void run() {
                try {
                    Thread.sleep ( 1000L );
                } catch (InterruptedException e) {
                    e.printStackTrace ( );
                }
                handler.post ( new Runnable ( ) {
                    @Override
                    public void run() {
                        callback.onSuccess ( dbManager.getFromDb(group_id) );
                    }
                } );
            }
        } );
    }*/

    @Override
    public List<Group> getAllGroup() {
        groups = dbManager.getFromDbGroup ();
        return groups;
    }

    @Override
    public void addNote(Note note) {
        dbManager.insertToDbNote ( note );
    }

    @Override
    public void addGroup(Group group) {
        dbManager.insertToDbGroup ( group );
    }

    @Override
    public int checkGroupForDbGroup(String text) {
        return dbManager.checkGroupForDbGroup ( text );
    }

    @Override
    public List<Group> searchByGroupName(String folderName) {
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

    public void deleteIndexGroup(long index) {
        dbManager.deleteEntryGroup (index);
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

    @Override
    public void deleteIndexNoteGroupId(long position) {
        dbManager.deleteIndexNoteGroupId(position);
    }
}
