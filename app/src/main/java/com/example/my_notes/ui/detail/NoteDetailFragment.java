package com.example.my_notes.ui.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;

public class NoteDetailFragment extends Fragment {

    public static final String TAG = "NoteDetailFragment";

    public static final String ARG_NOTE = "ARG_NOTE";
    public static final String ARG_GROUP = "ARG_GROUP";
    public static final String ARG_NEW_GROUP = "ARG_NEW_GROUP";

    public static final String ARG_NEW_NOTE = "ARG_NEW_NOTE";

    public static final String RESULT_KEY_DETAIL_FRAGMENT = "NoteDetailFragment_RESULT_KEY_DETAIL_FRAGMENT";
    public static final String RESULT_KEY_GROUP_FRAGMENT = "NoteDetailFragment_RESULT_KEY_GROUP_FRAGMENT";
    private Note note;

    private EditText titleView, textView;

    private TextView dataView;

    private Button btnSave;

    public Group group;

    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleView = view.findViewById(R.id.title_detail);

        textView = view.findViewById(R.id.text_detail);

        dataView = view.findViewById(R.id.data_detail);

        btnSave = view.findViewById(R.id.button_save);

        displayDetails(note);

    }

    private void displayDetails(Note note) {
        titleView.setText(note.getTitle());
        textView.setText(note.getText());
        dataView.setText ( note.getData () );

//        SimpleDateFormat formatDate = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            formatDate = new SimpleDateFormat("E '('dd MMMM yyyy')' '-->' hh:mm", Locale.getDefault());
//        }
//
//        assert formatDate != null;
//        StringBuilder sb = new StringBuilder(formatDate.format( Date.parse(note.getData())).toLowerCase());
//        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
//
//        dataView.setText(sb.toString());
        dataView.setText(note.getData());

        dataView.setOnClickListener ( view -> {
            MyDialogFragment fragment = MyDialogFragment.newInstance ( dataView.getId () );
            fragment.show(getChildFragmentManager (), MyDialogFragment.TAG);
        } );


        btnSave.setOnClickListener( view -> {
            note.setTitle(titleView.getText().toString());
            note.setText(textView.getText().toString());
            note.setData (dataView.getText().toString());
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, view);
            activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener ( new PopupMenu.OnMenuItemClickListener ( ) {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId ( )) {
                        case R.id.save_to_default_older:
                            Bundle data = new Bundle();
                            data.putParcelable(ARG_NEW_NOTE, note);
                            getParentFragmentManager()
                                    .setFragmentResult(RESULT_KEY_DETAIL_FRAGMENT, data);
                            return true;
                        case R.id.save_to_new_folder:
                            Toast.makeText ( activity, item.getTitle (), Toast.LENGTH_SHORT ).show ( );
                            return true;
                        case R.id.save_to_folder_of_choice:
                            Bundle data1 = new Bundle();
                            data1.putParcelable(ARG_NEW_GROUP, note);
                            getParentFragmentManager()
                                    .setFragmentResult(RESULT_KEY_GROUP_FRAGMENT, data1);
                            return true;
                        default:
                            return false;
                    }
                }
            } );
            popupMenu.show();
        } );
    }
}