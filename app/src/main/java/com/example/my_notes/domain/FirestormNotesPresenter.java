package com.example.my_notes.domain;

import java.util.List;

public class FirestormNotesPresenter {

    private final FirestormRepository repository;

    private final NoteListView view;

    public FirestormNotesPresenter(FirestormRepository repository, NoteListView view) {
        this.repository = repository;
        this.view = view;
    }

    public void addNote(Note note) {
        repository.addNote ( note, new Callback<Note> ( ) {
            @Override
            public void onSuccess(Note result) {
                view.addNote ( result );
            }

            @Override
            public void onError(Throwable error) {

            }
        } );
    }
}
