package com.example.my_notes.ui.adapterItem;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_notes.R;
import com.google.android.material.button.MaterialButton;


public class GroupViewHolder extends RecyclerView.ViewHolder {

    private final MaterialButton buttonGroup;

    public GroupViewHolder(@NonNull View itemView) {
        super ( itemView );
        buttonGroup = itemView.findViewById ( R.id.group_icon );
    }

    public MaterialButton getButtonGroup() {
        return buttonGroup;
    }
}
