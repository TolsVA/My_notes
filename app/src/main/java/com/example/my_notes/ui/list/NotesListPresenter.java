package com.example.my_notes.ui.list;

import android.os.Handler;
import android.os.Looper;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.domain.NoteListView;
import com.example.my_notes.domain.NotesRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesListPresenter {

    private final NotesRepository repository;

    private final NoteListView view;

    public NotesListPresenter(NotesRepository repository, NoteListView view) {
        this.repository = repository;
        this.view = view;
    }

    public void upgradeNote(Note note) {
        repository.upgradeNote(note);
    }

    public void addNote(Note note) {
        repository.addNote ( note );
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

    public void deleteIndex(long index) {
        repository.deleteIndex(index);
    }

    public void deleteIndexGroup(long index) {
        repository.deleteIndexGroup (index);
    }

    public void closeDb() {
        repository.closeDb ();
    }

    public List<Note> refreshNotes(long group_id) {
        return repository.getAllNotes ( group_id );
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
