package com.example.my_notes.ui.list;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_notes.R;
import com.example.my_notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment {

    public static final String TAG = "NotesListFragment";

    private LinearLayout notesContainer;

    public List<Note> notes;

    private static final String ARG_NOTES = "ARG_NOTES";

    public static final String ARG_NOTE = "ARG_NOTE";

    public static final String RESULT_KEY = "NotesListFragment_RESULT";

    public static final String DELETE_KEY = "NotesListFragment_DELETE";

    public NotesListFragment() {
    }

    public static NotesListFragment newInstance(List<Note> notes) {
        NotesListFragment fragment = new NotesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList (ARG_NOTES, (ArrayList<? extends Parcelable>) notes );
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_notes_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments () != null) {
            notes = getArguments ( ).getParcelableArrayList ( ARG_NOTES ) ;
        }

        notesContainer = view.findViewById( R.id.container_notes);

        showNotes(notes);
    }

    public void showNotes(List<Note> notes) {

        for (Note note : notes) {

            View itemView = LayoutInflater.from(requireContext()).inflate( R.layout.item_note, notesContainer, false);

            TextView title = itemView.findViewById( R.id.note_title);
            title.setText(note.getTitle());

            TextView text = itemView.findViewById( R.id.note_text);
            text.setText(note.getText());

            TextView data = itemView.findViewById( R.id.note_date);
            data.setText(note.getData());

            Button bDelete = itemView.findViewById( R.id.delete_index);

            bDelete.setOnClickListener( view -> {

                Bundle data1 = new Bundle();
                data1.putParcelable(ARG_NOTE, note);

                getParentFragmentManager()
                        .setFragmentResult(DELETE_KEY, data1 );

            } );

            itemView.setOnClickListener( view -> {
                Bundle data12 = new Bundle();
                data12.putParcelable(ARG_NOTE, note);

                getParentFragmentManager()
                        .setFragmentResult(RESULT_KEY, data12 );
            } );

            notesContainer.addView(itemView);
        }

    }
}