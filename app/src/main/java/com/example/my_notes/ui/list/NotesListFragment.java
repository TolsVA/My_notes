package com.example.my_notes.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.my_notes.R;
import com.example.my_notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment {

//    private final SimpleDateFormat formatDate = new SimpleDateFormat("E dd.BB.yyyy 'и время' hh:mm:ss a zzz", Locale.getDefault());

    public static final String TAG = "NotesListFragment";

    public LinearLayoutCompat notesContainer;

    public List<Note> notes, deleteNotes;

    public static final String ARG_NOTES = "ARG_NOTES";

    public static final String ARG_NOTE = "ARG_NOTE";

    public static final String ARG_INDEX = "ARG_INDEX";

    public static final String RESULT_KEY = "NotesListFragment_RESULT";

    public static final String ADD_DELETE_KEY = "NotesListFragment_ADD_DELETE";

    public static final String CREATE_DELETE_KEY = "NotesListFragment_CREATE_DELETE";

    public int index;

    public NotesListFragment() {
    }

    public static NotesListFragment newInstance(List<Note> notes, int index, List<Note> deleteNotes) {
        NotesListFragment fragment = new NotesListFragment ( );
        Bundle args = new Bundle ( );
        args.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) notes );
        args.putInt ( ARG_INDEX, index );
        args.putParcelableArrayList ( ADD_DELETE_KEY, (ArrayList<? extends Parcelable>) deleteNotes );
        fragment.setArguments ( args );
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            notes = getArguments ( ).getParcelableArrayList ( ARG_NOTES );
            index = getArguments ( ).getInt ( ARG_INDEX );
            deleteNotes = getArguments ( ).getParcelableArrayList ( ADD_DELETE_KEY );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.fragment_notes_list, container, false );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        notesContainer = view.findViewById ( R.id.container_notes );

        showNotes ( notes );

        NestedScrollView scrollView = view.findViewById ( R.id.scroll_list );

        scrollView.requestChildFocus ( notesContainer, notesContainer.getChildAt ( index ) );

        if (notes.size () != 0) {
            notesContainer.getChildAt ( index ).setBackground ( getResources ( ).getDrawable ( R.drawable.layout_bg_2, requireContext ( ).getTheme ( ) ) );
        }
    }

    public void showNotes(List<Note> notes) {
        for (Note note : notes) {

            View itemView = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_note, notesContainer, false );

            TextView title = itemView.findViewById ( R.id.note_title );
            title.setText ( note.getTitle ( ) );

            TextView text = itemView.findViewById ( R.id.note_text );
            text.setText ( note.getText ( ) );

            TextView data = itemView.findViewById ( R.id.note_date );

            CheckBox checkBox = itemView.findViewById ( R.id.delete_index );

            for (int i = 0; i < deleteNotes.size ( ); i++) {
                if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
                    checkBox.setChecked ( true );
                }
            }

//            SimpleDateFormat formatDate = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                formatDate = new SimpleDateFormat("день недели:  EEEE\nдата________:   dd MMMM yyyy" +
////                        "\nВремя______:   hh:mm", Locale.getDefault());
//                formatDate = new SimpleDateFormat ( "EEEE  dd MMMM yyyy   hh:mm", Locale.getDefault ( ) );
//            }
//
//            assert formatDate != null;
//            StringBuilder sb = new StringBuilder(formatDate.format( Date.parse ( note.getData() )).toLowerCase());
//            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
//            String data1 = sb.toString();
//
//            data.setText(data1);
            data.setText ( note.getData ( ) );

            notesContainer.addView ( itemView );

            if (deleteNotes.size ( ) > 0) {
                checkBox.setVisibility ( View.VISIBLE );
                itemView.setOnClickListener ( view -> {
                    if (checkBox.isChecked ( )) {
                        checkBox.setChecked ( false );
                        for (int i = 0; i < deleteNotes.size ( ); i++) {
                            if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
                                deleteNotes.remove ( note );
                            }
                        }
                    } else {
                        checkBox.setChecked ( true );
                        deleteNotes.add ( note );
                    }

                    Bundle data1 = new Bundle ( );
                    data1.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );

                    getParentFragmentManager ( )
                            .setFragmentResult ( ADD_DELETE_KEY, data1 );
                } );
            } else {
                itemView.setOnClickListener ( view -> {
                    Bundle data12 = new Bundle ( );
                    data12.putParcelable ( ARG_NOTE, note );

                    getParentFragmentManager ( )
                            .setFragmentResult ( RESULT_KEY, data12 );
                } );
            }

            if (deleteNotes.size ( ) == 0) {
                itemView.setOnLongClickListener ( view -> {
                    checkBox.setVisibility ( View.VISIBLE );
                    checkBox.setChecked ( true );
                    deleteNotes.add ( note );

                    Bundle data13 = new Bundle ( );
                    data13.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );

                    getParentFragmentManager ( )
                            .setFragmentResult ( CREATE_DELETE_KEY, data13 );
                    return false;
                } );
            }
        }
    }
}