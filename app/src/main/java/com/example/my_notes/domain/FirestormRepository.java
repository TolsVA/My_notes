package com.example.my_notes.domain;

import java.util.List;

public interface FirestormRepository {

    void addNote(Note note, Callback<Note> callback);

    void getAll(Callback<List<Note>> callback);
}
