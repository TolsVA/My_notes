package com.example.my_notes.ui.dialog;

import com.example.my_notes.domain.Note;

public interface DialogClickListener {
    void showNotesListFragment(Note note);

    void applySettings(String text, int id);

    long createNewGroup(int resourceId, String valueOf);
}
