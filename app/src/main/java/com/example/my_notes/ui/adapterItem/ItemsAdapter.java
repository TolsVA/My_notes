package com.example.my_notes.ui.adapterItem;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.my_notes.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_GROUP = 0;
    private static final int TYPE_NOTE = 1;

    private final List<AdapterItem> items = new ArrayList<> ( );

    private OnClickItem onClickItem;

    private OnLongClickItem onLongClickItem;

    private OnClickItemGroup onClickItemGroup;

    public OnClickItemGroup getOnClickItemGroup() {
        return onClickItemGroup;
    }

    public void setOnClickItemGroup(OnClickItemGroup onClickItemGroup) {
        this.onClickItemGroup = onClickItemGroup;
    }

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

    public void setItems(Collection<AdapterItem> items) {
        this.items.clear ( );
        this.items.addAll ( items );
    }

    public void addItems(int position, AdapterItem item) {
        this.items.add ( position, item );
        items.size ( );
    }

    public int getPreviousClickedItemPosition() {
        return previousClickedItemPosition;
    }

    public void setPreviousClickedItemPosition(int previousClickedItemPosition) {
        this.previousClickedItemPosition = previousClickedItemPosition;
    }

    private int previousClickedItemPosition = -1;

    @Override
    public int getItemViewType(int position) {

        if (items.get ( position ) instanceof NoteItem) {
            return TYPE_NOTE;
        }

        if (items.get ( position ) instanceof GroupItem) {
            return TYPE_GROUP;
        }

        return super.getItemViewType ( position );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NOTE) {
            View itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.item_note, parent, false );
            return new NoteViewHolder ( itemView );
        }
        if (viewType == TYPE_GROUP) {
//            View itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.fragment_group_name, parent, false );
            View itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.item_group, parent, false );
            return new GroupViewHolder ( itemView );
        }
        throw new IllegalStateException ("Всё плохо");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoteViewHolder) {
            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
            NoteItem note = ((NoteItem) items.get ( position ));
            noteViewHolder.getTitle ( ).setText ( note.getNote ().getTitle () );
            noteViewHolder.getText ( ).setText ( note.getNote ().getText () );
            noteViewHolder.getData ( ).setText ( String.valueOf ( note.getNote ().getData () ) );
            noteViewHolder.getCheckBox ( ).isChecked ( );
//            noteViewHolder.getCheckBox ( ).set

            if (getPreviousClickedItemPosition ( ) != position) {
                noteViewHolder.getCardView ( ).setCardBackgroundColor ( Color.WHITE );
            } else {
                noteViewHolder.getCardView ( ).setCardBackgroundColor ( Color.YELLOW );
            }


            noteViewHolder.getCardView ( ).setOnClickListener ( view -> {
                if (ItemsAdapter.this.getOnClickItem ( ) != null) {
                    if (ItemsAdapter.this.getPreviousClickedItemPosition ( ) != position) {
                        ItemsAdapter.this.setPreviousClickedItemPosition ( position );
                    }
                    ItemsAdapter.this.getOnClickItem ( ).onClickItem ( view, note, position );
                }
            } );
            noteViewHolder.getCardView ( ).setOnLongClickListener ( view -> {
                if (ItemsAdapter.this.getOnLongClickItem ( ) != null) {
                    if (ItemsAdapter.this.getPreviousClickedItemPosition ( ) != position) {
                        ItemsAdapter.this.setPreviousClickedItemPosition ( position );
                    }
                    ItemsAdapter.this.getOnLongClickItem ( ).onLongClickItem ( view, note, position, noteViewHolder.getCheckBox ( ) );
                }
                return true;
            } );
        }

        if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            GroupItem group = ((GroupItem) items.get ( position ));

            groupViewHolder.getButtonGroup ().setText ( group.getGroup ().getName () + ": " + group.getGroup ().getCount ());
            groupViewHolder.getButtonGroup ().setIconResource ( group.getGroup ().getIcon () );

            groupViewHolder.getButtonGroup ().setOnClickListener ( view -> {
                if (getOnClickItemGroup ( ) != null) {
                    getOnClickItemGroup ().onClickItemGroup ( view, group, position );
                }
            } );
        }
    }

    @Override
    public int getItemCount() {
        return items.size ( );
    }
}
