package com.example.my_notes.ui.adapter;

import android.view.View;
import android.widget.CheckBox;
import com.example.my_notes.domain.Note;

public interface OnLongClickItem {
    void onLongClickItem(View view, Note note, int position, CheckBox checkBox);
}
