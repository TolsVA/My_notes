package com.example.my_notes.ui.list;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.my_notes.domain.Callback;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.domain.NoteListView;
import com.example.my_notes.domain.NotesRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesListPresenter {

    private Context context;

    private final NotesRepository repository;

    private final NoteListView view;

    public NotesListPresenter(Context context, NotesRepository repository, NoteListView view) {
        this.context = context;
        this.repository = repository;
        this.view = view;
    }

    public void upgradeNote(Note note) {
        repository.upgradeNote(note);
    }

    public void addNote(Note note) {
        repository.addNote ( note );
        view.addNote ( note );
    }

    public void clearDb(){
        repository.clearDb();
    }

    public List<Note> search(String text){
        return repository.searchDb(text);
    }

/*    public int checkGroupFor(String text){
        return repository.checkGroupForDbGroup ( text );
    }*/

    public void deleteIndex(long group_id, List<Note> deleteNotes) {
//        view.showProgress();
        repository.deleteIndex (group_id, deleteNotes, new Callback<List<Note>> ( ) {
            @Override
            public void onSuccess(List<Note> result) {
//                view.showNotes(result);
//                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {
//                view.hideProgress();
            }
        } );
        view.deleteNotes ( deleteNotes );
    }

    public void deleteIndexGroup(long index) {
        repository.deleteIndexGroup (index);
    }

    public void closeDb() {
        repository.closeDb ();
    }

//    public List<Note> refreshNotes(long group_id) {
//        List<Note> notes = repository.getAllNotes ( group_id, c  );
//        view.showNotes ( notes );
//        return notes;
//    }

    public void refreshNotes(long group_id) {
        view.showProgress();
        repository.getAllNotes ( group_id, new Callback<List<Note>> (){
            @Override
            public void onSuccess(List<Note> result) {
                view.showNotes(result);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        } );
    }

    public List<Group> refreshGroup() {
        return repository.getAllGroup ();
    }

    public void addGroup(Group group) {
        repository.addGroup ( group );
    }

/*    public List<Group> searchByGroupName(String folderName) {
        return repository.searchByGroupName(folderName);
    }*/

    public void deleteIndexNoteGroupId(long position) {
        repository.deleteIndexNoteGroupId(position);
    }
}
