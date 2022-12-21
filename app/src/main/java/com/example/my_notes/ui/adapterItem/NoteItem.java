package com.example.my_notes.ui.adapterItem;

import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapterItem.AdapterItem;

public class NoteItem implements AdapterItem {

    private Note note;

    public NoteItem(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
