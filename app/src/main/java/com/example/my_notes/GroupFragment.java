package com.example.my_notes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_notes.domain.Group;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {

    public static final String TAG = "GroupFragment";

    private static final String ARG_GROUP = "ARG_GROUP";

    private List<Group> groups;

    public LinearLayoutCompat groupContainer;

    public GroupFragment() {
    }

    public static GroupFragment newInstance(List<Group> groups) {
        GroupFragment fragment = new GroupFragment ( );
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_group, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        groupContainer = view.findViewById ( R.id.container_group );

        showGroups ( groups );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showGroups(List<Group> groups) {
//        for (Group group : groups) {
//            View itemView = LayoutInflater.from ( requireContext ( ) )
//                    .inflate ( R.layout.item_group, groupContainer, false );
//
//            TextView name = itemView.findViewById ( R.id.group_name );
//            name.setText ( group.getName () );
//
//            ImageView icon = itemView.findViewById ( R.id.group_icon );
//            icon.setImageDrawable ( getResources ().getDrawable ( group.getIcon () , requireActivity ( ).getTheme ( ) ) );
//
//            groupContainer.addView ( itemView );
//        }
    }
}