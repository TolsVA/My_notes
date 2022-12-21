package com.example.my_notes.ui.adapterItem;

import android.view.View;
import android.widget.CheckBox;

public interface OnLongClickItem {
    void onLongClickItem(View view, NoteItem note, int position, CheckBox checkBox);
}
