package com.example.my_notes.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import java.util.ArrayList;
import java.util.List;

public class MyDialogFragmentChoose extends DialogFragment {
    public static final String TAG = "MyBottomDialogFragmentGroup";

    public static final String ARG_GROUP = "ARG_GROUP";

    public List<Group> groups;

    public static MyDialogFragmentChoose newInstance(List<Group> groups) {
        MyDialogFragmentChoose fragment = new MyDialogFragmentChoose ( );
        Bundle args = new Bundle ( );
        args.putParcelableArrayList ( ARG_GROUP, (ArrayList<? extends Parcelable>) groups );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            groups = getArguments ( ).getParcelableArrayList ( ARG_GROUP );
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] items;
        String title;
        if (groups.size () > 0) {
            title = "Внимание запущена процедура удаления папок!!!";
            int length = groups.size ( ) + 1;
            items = new String[length];
            items[0] = "Удалить все папки";
            for (int i = 1; i < length; i++) {
                items[i] = getResources ().getString ( R.string.delete_folder_2 ) + "  \"" + groups.get ( i - 1 ).getName ( ) + "\"";
            }
        } else {
            title = "У вас нет не одной заметки нечего удалять \uD83D\uDE41";
            items = new String[0];
        }
//        String[] items = getResources().getStringArray( R.array.choose);

        return new AlertDialog.Builder(requireContext ())
                .setTitle ( title )
                .setIcon ( getResources ().getDrawable ( R.drawable.ic_baseline_error_outline_24, requireContext ().getTheme () ) )
                .setItems ( items, (dialogInterface, i) -> {

                    Bundle bundle = new Bundle ();
                    bundle.putInt ( ConstantsNote.ARG_INDEX, i );

                    getParentFragmentManager ()
                            .setFragmentResult ( ConstantsNote.KEY_INDEX, bundle );

                    dismiss ();
                } )
                .create ();
    }
}
