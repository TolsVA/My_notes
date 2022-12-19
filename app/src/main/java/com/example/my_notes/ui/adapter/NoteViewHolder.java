package com.example.my_notes.ui.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.my_notes.R;
import com.google.android.material.card.MaterialCardView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private final MaterialCardView cardView;
    private final TextView title;
    private final TextView text;
    private final TextView data;
    private final CheckBox checkBox;

    public NoteViewHolder(@NonNull View itemView) {
        super ( itemView );
        title = itemView.findViewById ( R.id.note_title );
        text = itemView.findViewById ( R.id.note_text );
        data = itemView.findViewById ( R.id.note_date );
        checkBox = itemView.findViewById ( R.id.note_check_box );
        cardView = itemView.findViewById ( R.id.card_item );
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getText() {
        return text;
    }

    public TextView getData() {
        return data;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public MaterialCardView getCardView() {
        return cardView;
    }
}