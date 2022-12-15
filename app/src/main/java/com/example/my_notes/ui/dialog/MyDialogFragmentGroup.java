package com.example.my_notes.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
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
    public static final String ARG_NOTE = "ARG_NOTE";

    public Note note;
    public List<Group> groups;
    public long groupId;
    public String nameGroup;

    public static MyDialogFragmentGroup newInstance(List<Group> groups, Note note) {
        MyDialogFragmentGroup fragment = new MyDialogFragmentGroup ( );
        Bundle args = new Bundle ( );
        args.putParcelableArrayList ( ARG_GROUP, (ArrayList<? extends Parcelable>) groups );
        args.putParcelable ( ARG_NOTE, note );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            groups = getArguments ( ).getParcelableArrayList ( ARG_GROUP );
            note = getArguments ().getParcelable ( ARG_NOTE );
        }
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View customView = getLayoutInflater().inflate( R.layout.fragment_group, null);
        MaterialButton materialButton = customView.findViewById ( R.id.group_icon_selected );
        MaterialButton ok = customView.findViewById ( R.id.ok );

        LinearLayoutCompat groupContainer = customView.findViewById ( R.id.container_group );
        for (int i = 0; i < groups.size (); i++) {
            final int resourceId = groups.get ( i ).getIcon ();
            View view = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_group, groupContainer, false );

            MaterialButton buttonGroupName = view.findViewById ( R.id.group_icon );
            buttonGroupName.setIconResource ( resourceId );
//            buttonGroupName.setIconTint ( ColorStateList.valueOf ( getResources ().getColor ( R.color.purple_700,getActivity ().getTheme () ) ) );
            buttonGroupName.setText ( groups.get ( i ).getName () );
            final int position = i;
            groupContainer.addView ( view );

            buttonGroupName.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    groupId = groups.get ( position ).getId ();
                    nameGroup = groups.get ( position ).getName ();
                    materialButton.setIconResource ( resourceId );
//                    materialButton.setIconTint ( ColorStateList.valueOf ( getResources ().getColor ( R.color.purple_700,getActivity ().getTheme () ) ) );
                    materialButton.setText ( nameGroup  );
                    note.setGroup_id ( groupId );
                }
            } );
        }

        AlertDialog builder = new AlertDialog.Builder(requireContext ())
                .setView(customView)
                .create();

        ok.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (note.getGroup_id () == 0) {
                    Toast.makeText ( requireContext (), "Не выбрана папка для сохранения", Toast.LENGTH_SHORT ).show ( );
                } else {
                    ((MainActivity) getActivity ()).showNotesListFragment ( note );
                    builder.dismiss ( );
                }
            }
        } );
        return builder;
    }
}
