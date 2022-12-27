package com.example.my_notes.domain;

public interface FirestormRepository {

    void addNote(Note note, Callback<Note> callback);
}
