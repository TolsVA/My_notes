package com.example.my_notes.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.my_notes.R;
import com.example.my_notes.domain.Note;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final List<Note> notes = new ArrayList<> ( );

    private OnClickItem onClickItem;

    private OnLongClickItem onLongClickItem;

    public OnClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public OnLongClickItem getOnLongClickItem() {
        return onLongClickItem;
    }

    public void setOnLongClickItem(OnLongClickItem onLongClickItem) {
        this.onLongClickItem = onLongClickItem;
    }

    public void setNotes(Collection<Note> notes) {
        this.notes.clear ( );
        this.notes.addAll ( notes );
    }

    public int getPreviousClickedItemPosition() {
        return previousClickedItemPosition;
    }

    public void setPreviousClickedItemPosition(int previousClickedItemPosition) {
        this.previousClickedItemPosition = previousClickedItemPosition;
    }

    private int previousClickedItemPosition = -1;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.item_note, parent, false );
        return new NoteViewHolder ( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get ( position );
        holder.getTitle ( ).setText ( note.getTitle ( ) );
        holder.getText ( ).setText ( note.getText ( ) );
        holder.getData ( ).setText ( String.valueOf ( note.getData ( ) ) );
        holder.getCheckBox ( ).isChecked ( );

        if(getPreviousClickedItemPosition () != position) {
            holder.getCardView ( ).setCardBackgroundColor ( Color.WHITE );
        } else {
            holder.getCardView ().setCardBackgroundColor(Color.YELLOW);
        }


        holder.getCardView ().setOnClickListener ( view -> {
            if (getOnClickItem () != null) {

                if(getPreviousClickedItemPosition () != position) {
                    setPreviousClickedItemPosition ( position );
                }

                getOnClickItem ( ).onClickItem ( view, note, position );
            }
        } );
        holder.getCardView ().setOnLongClickListener ( view -> {
            if (getOnLongClickItem ( ) != null) {
                getOnLongClickItem ( ).onLongClickItem ( view, note, position, holder.getCheckBox () );
            }
            return true;
        } );
    }

    @Override
    public int getItemCount() {
        return notes.size ( );
    }
}
