package com.example.my_notes.domain;

import com.example.my_notes.ui.adapterItem.AdapterItem;

import java.util.List;

public interface NoteListView {

//    void showNotes(List<AdapterItem> notes);
    void showNotes(List<Note> notes);

    void upgradeNote(Note note);

    void deleteNotes(List<Note> deleteNotes);

    void addNote(Note note);

    void showProgress();

    void hideProgress();
}
