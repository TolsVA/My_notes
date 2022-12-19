package com.example.my_notes.ui.adapter;

import android.view.View;
import com.example.my_notes.domain.Note;

public interface OnClickItem {
    void onClickItem(View view, Note note, int position);
}
