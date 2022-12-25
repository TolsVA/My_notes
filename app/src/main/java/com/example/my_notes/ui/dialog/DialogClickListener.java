package com.example.my_notes.ui.dialog;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;

import java.util.List;

public interface DialogClickListener {
    void showNotesListFragment(Note note);

    void applySettings(String text, int id);

    long createNewGroup(int resourceId, String valueOf);

    List<Group> getGroups();

}
