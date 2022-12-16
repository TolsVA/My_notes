package com.example.my_notes.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class MyBottomDialogFragmentGroup extends BottomSheetDialogFragment {

    public static final String TAG = "MyBottomDialogFragmentGroup";

    public static final String ARG_GROUP = "ARG_GROUP";
    public static final String ARG_NOTE = "ARG_NOTE";

    public Note note;
    public List<Group> groups;
    public long groupId;
    public String nameGroup;

    public static MyBottomDialogFragmentGroup newInstance(List<Group> groups, Note note) {
        MyBottomDialogFragmentGroup fragment = new MyBottomDialogFragmentGroup ( );
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customView = inflater.inflate( R.layout.fragment_group, container, false);
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

            buttonGroupName.setOnClickListener ( view1 -> {
                groupId = groups.get ( position ).getId ();
                nameGroup = groups.get ( position ).getName ();
                materialButton.setIconResource ( resourceId );
                materialButton.setText ( nameGroup  );
                note.setGroup_id ( groupId );
            } );
        }

        ok.setOnClickListener ( view -> {
            if (note.getGroup_id () == 0) {
                Toast.makeText ( requireContext (), "Не выбрана папка для сохранения", Toast.LENGTH_SHORT ).show ( );
            } else {

                Activity activity = requireActivity ( );
                if (activity instanceof DialogClickListener) {
                    ((DialogClickListener) activity).showNotesListFragment ( note );
                }
                dismiss ( );
            }
        } );
        return customView;
    }
}
