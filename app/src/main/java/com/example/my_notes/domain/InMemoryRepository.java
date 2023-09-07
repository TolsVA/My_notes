package com.example.my_notes.domain;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.example.my_notes.db.DbManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InMemoryRepository implements NotesRepository {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Handler handler = new Handler( Looper.getMainLooper());

    private final DbManager dbManager;

    public InMemoryRepository(Context context) {
        dbManager = new DbManager(context);
        dbManager.openDb();
    }

    @Override
    public void getAllNotes(long group_id, Callback<List<Note>> callback) {
        executor.execute( () -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post( new Runnable ( ) {
                @Override
                public void run() {
                    callback.onSuccess ( dbManager.getFromDb ( group_id ) );
                }
            } );
        } );
    }

    @Override
    public List<Group> getAllGroup() {
        return dbManager.getFromDbGroup ();
    }

    @Override
    public Note addNote(Note note) {
        return dbManager.insertToDbNote ( note );
    }

    @Override
    public Group addGroup(Group group) {
        return dbManager.insertToDbGroup ( group );
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
    public void deleteIndex( long index ) {
        executor.execute ( () -> dbManager.deleteEntry( index ) );
    }

    @Override
    public void upgradeNote(Note note) {
        dbManager.upgradeEntry(note);
    }


    public void deleteIndexGroup(long index) {
        dbManager.deleteEntryGroup (index);
    }

    @Override
    public void closeDb() {
        dbManager.closeDb();
    }

    @Override
    public List<Note> searchDb(String text) {
        return dbManager.searchDb ( text );
    }

    @Override
    public void deleteIndexNoteGroupId(long position) {
        dbManager.deleteIndexNoteGroupId(position);
    }
}
