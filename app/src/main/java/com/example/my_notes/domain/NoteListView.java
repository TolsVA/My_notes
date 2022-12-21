package com.example.my_notes.domain;

import java.util.List;

public interface NoteListView {

    void showNotes(List<Note> notes);

    void showProgress();

    void hideProgress();
}
