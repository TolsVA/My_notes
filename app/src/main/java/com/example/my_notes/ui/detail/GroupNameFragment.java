package com.example.my_notes.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.google.android.material.button.MaterialButton;

public class GroupNameFragment extends Fragment {
    private static final String ARG_GROUP = "ARG_GROUP";

    private Group group;

    public GroupNameFragment() {
    }

    public static GroupNameFragment newInstance(Group group) {
        GroupNameFragment fragment = new GroupNameFragment ( );
        Bundle args = new Bundle ( );
        args.putParcelable ( ARG_GROUP, group );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            group = getArguments ( ).getParcelable ( ARG_GROUP );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.fragment_group_name, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        MaterialButton nameGroup = view.findViewById ( R.id.name_group );
        nameGroup.setText ( group.getName () );
    }
}