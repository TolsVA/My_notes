package com.example.my_notes.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapter.MyAdapterIcon;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;
import com.example.my_notes.ui.detail.NoteDetailFragment;
import com.example.my_notes.ui.list.NotesListFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MyDialogFragmentGroup extends DialogFragment {

    public static final String TAG = "MyDialogFragmentGroup";

    public static final String ARG_GROUP = "ARG_GROUP";

    public List<Group> groups;
    public long groupId;
    public String nameGroup;

    public static MyDialogFragmentGroup newInstance(List<Group> groups) {
        MyDialogFragmentGroup fragment = new MyDialogFragmentGroup ( );
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


    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View customView = getLayoutInflater().inflate( R.layout.fragment_group, null);
        MaterialButton materialButton = customView.findViewById ( R.id.group_icon_selected );
        LinearLayoutCompat groupContainer = customView.findViewById ( R.id.container_group );
        for (int i = 0; i < groups.size (); i++) {
            final int resourceId = groups.get ( i ).getIcon ();
            View view = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_group, groupContainer, false );

            MaterialButton buttonGroupName = view.findViewById ( R.id.group_icon );
            buttonGroupName.setIconResource ( resourceId );
            buttonGroupName.setText ( groups.get ( i ).getName () );
            final int position = i;
            groupContainer.addView ( view );

            buttonGroupName.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    groupId = groups.get ( position ).getId ();
                    nameGroup = groups.get ( position ).getName ();
                    materialButton.setIconResource ( resourceId );
                    materialButton.setText ( groups.get ( position ).getName ()  );
                }
            } );
        }

        return new AlertDialog.Builder(requireContext ())
                .setTitle ( "Выбери папку или создай новую" )
                .setView(customView)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        new MyDialogFragmentImageView ( ).show ( requireActivity ().getSupportFragmentManager ( ), MyDialogFragmentImageView.TAG );
                        ((MainActivity)getActivity ()).setGroupId ( groupId );
                        List<Note> notes = ((MainActivity)getActivity ()).getNotes ();
                        for (Fragment fragment : getActivity ().getSupportFragmentManager ().getFragments ()) {
                            if (fragment instanceof NotesListFragment) {
                                ((NotesListFragment) fragment).showNotes ( notes );
                                break;
                            }
                        }
                        for (Fragment fragment : getActivity ().getSupportFragmentManager ().getFragments ()) {
                            if (fragment instanceof NoteDetailFragment) {
//                                ((NoteDetailFragment) fragment).toolbar.setTitle ( nameGroup );
                                ((NoteDetailFragment) fragment).setNameGroup(groupId);
                                break;
                            }
                        }
                    }
                })
                .create();
    }

}
