package com.example.my_notes.ui.list;

import com.example.my_notes.domain.Note;
import com.example.my_notes.domain.NotesRepository;

import java.util.List;

public class NotesListPresenter {

    private final NotesRepository repository;

    public NotesListPresenter(NotesRepository repository) {
        this.repository = repository;
    }

    public List<Note> refresh() {
        return repository.getAllNotes();
    }

    public void upgradeNote(Note note) {
        repository.upgradeNote(note);
    }

    public Note addNote(Note note) {
        return repository.addNote(note);
    }

    public void clearDb(){
        repository.clearDb();
    }

    public List<Note> search(String text){
        return repository.searchDb(text);
    }

    public void deleteIndex(long index) {
        repository.deleteIndex(index);
    }

    public void closeDb() {
        repository.closeDb ();
    }
}
