package com.example.my_notes.ui.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.my_notes.R;
import com.example.my_notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesSearchFragment extends Fragment {

    public static final String TAG = "NotesSearchFragment";

    private static final String ARR_DELETE = "ARR_DELETE";
    private static final String ARR_NOTES = "ARR_NOTES";

    private List<Note> notes, deleteNotes;

    public TextView counterDelete, dataMin, dataMax;

    private ImageView selectNotes;

    public NotesSearchFragment() {
        // Required empty public constructor
    }

    public static NotesSearchFragment newInstance(List<Note> deleteNotes) {
        NotesSearchFragment fragment = new NotesSearchFragment ( );
        Bundle args = new Bundle ( );
//        args.putParcelableArrayList ( ARR_NOTES, (ArrayList<? extends Parcelable>) notes );
        args.putParcelableArrayList ( ARR_DELETE, (ArrayList<? extends Parcelable>) deleteNotes );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
//            notes = getArguments().getParcelableArrayList(ARR_NOTES);
            deleteNotes = getArguments().getParcelableArrayList(ARR_DELETE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.fragment_notes_search, container, false );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        ConstraintLayout layoutSearch = view.findViewById ( R.id.constraint_layout_search );
        LinearLayout layoutDelete = view.findViewById ( R.id.layout_delete );
        counterDelete = view.findViewById ( R.id.counter_delete );
        selectNotes = view.findViewById ( R.id.select_notes );


        if (deleteNotes != null) {
            if (deleteNotes.size ( ) > 0) {
                layoutSearch.setVisibility ( View.GONE );
                layoutDelete.setVisibility ( View.VISIBLE );
                counterDelete.setText ( counterDelete.getText () + String.valueOf ( deleteNotes.size () ) );

                selectNotes.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {

                    }
                } );

            } else {
                layoutSearch.setVisibility ( View.VISIBLE );
                layoutDelete.setVisibility ( View.GONE );
            }
        }

        dataMin = view.findViewById ( R.id.data_min );
        dataMax = view.findViewById ( R.id.data_max );

        dataMin.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
//                showDialogMinMax (dataMin);
//                new MyDialogFragment().show(getChildFragmentManager (), MyDialogFragment.TAG);

                Toast.makeText ( requireContext (), String.valueOf ( dataMin.getId () ), Toast.LENGTH_SHORT ).show ( );

                MyDialogFragment fragment = MyDialogFragment.newInstance ( dataMin.getId () );
                fragment.show(getChildFragmentManager (), MyDialogFragment.TAG);

            }
        } );

        dataMax.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
//                showDialogMinMax (dataMax);
//                new MyDialogFragment().show(getChildFragmentManager (), MyDialogFragment.TAG);

                MyDialogFragment fragment = MyDialogFragment.newInstance ( dataMax.getId () );
                fragment.show(getChildFragmentManager (), MyDialogFragment.TAG);
            }
        } );
    }



//    private void showDialogMinMax(TextView textView) {
//        final Calendar cal = Calendar.getInstance();
//        int mYear = cal.get(Calendar.YEAR);
//        int mMonth = cal.get(Calendar.MONTH);
//        int mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext (),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
//                        textView.setText ( editTextDateParam );
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
}