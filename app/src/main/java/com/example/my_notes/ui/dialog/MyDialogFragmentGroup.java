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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MyDialogFragmentGroup extends DialogFragment {

    public static final String TAG = "MyDialogFragmentGroup";

    public static final String ARG_GROUP = "ARG_GROUP";

    public List<Group> groups;

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
        int resourceId;
        View customView = getLayoutInflater().inflate( R.layout.fragment_group, null);
        GridLayout groupContainer = customView.findViewById ( R.id.grid_container_group );

        for (int i = 0; i < groups.size (); i++) {
            resourceId = groups.get ( i ).getIcon ();
            View view = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_group, groupContainer, false );
            MaterialButton buttonGroupName = view.findViewById ( R.id.group_icon );
            buttonGroupName.setIconResource ( resourceId );
            buttonGroupName.setText ( groups.get ( i ).getName () );
            buttonGroupName.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
//                        imageView.setVisibility ( View.VISIBLE );
//                        imageView.setImageResource ( resourceId );
//                        resourceIdNew[0] = resourceId;
                }
            } );
            groupContainer.addView ( view );
        }

        return new AlertDialog.Builder(requireContext ())
                .setTitle ( "Выбери папку" )
                .setView(customView)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext (), "No!", Toast.LENGTH_SHORT).show();
//                        List<Group> groups = ((MainActivity) getActivity ( )).groups;
                    }
                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(requireContext (), "No!", Toast.LENGTH_SHORT).show();
//                    }
//                })
                .setNeutralButton("Отмена", null)
                .create();
    }

//    public void getImageView(int resourceId) {
//        this.resourceId = resourceId;
//        imageView.setImageResource ( resourceId );
//        imageView.setColorFilter ( getResources ().getColor ( R.color.purple_700, requireContext().getTheme()) );
//    }
}
