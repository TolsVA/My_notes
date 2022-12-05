package com.example.my_notes.ui.list;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.domain.NotesRepository;

import java.util.List;

public class NotesListPresenter {

    private final NotesRepository repository;

    public NotesListPresenter(NotesRepository repository) {
        this.repository = repository;
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

    public int checkGroupFor(String text){
        return repository.checkGroupForDbGroup ( text );
    }

    public void deleteIndex(long index) {
        repository.deleteIndex(index);
    }

    public void closeDb() {
        repository.closeDb ();
    }

    public List<Note> refreshNotes() {
        return repository.getAllNotes();
    }
    public List<Group> refreshGroup() {
        return repository.getAllGroup ();
    }

    public Group addGroup(Group group) {
        return repository.addGroup(group);
    }

    public Group searchByGroupName(String folderName) {
        return repository.searchByGroupName(folderName);
    }
}
