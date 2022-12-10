package com.example.my_notes.domain;

import java.util.List;

public interface NotesRepository {

    List<Note> getAllNotes();

    void upgradeNote(Note note);

    Note addNote(Note note);

    void clearDb();

    void deleteIndex(long index);

    void closeDb();

    List<Note> searchDb(String text);

}
