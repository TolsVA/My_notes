package com.example.my_notes.ui.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.my_notes.R;
import com.example.my_notes.ui.dialog.MyDialogFragmentImageView;


public class IconsFragment extends Fragment {

    public static final String TAG = "IconsFragment";

    private static final String ARG_IMAGE = "ARG_IMAGE";

    public ImageView iv;

    public int icon;

    public IconsFragment() {
        // Required empty public constructor
    }

    public static IconsFragment newInstance(int icon) {
        IconsFragment fragment = new IconsFragment ( );
        Bundle args = new Bundle ( );
        args.putInt ( ARG_IMAGE, icon );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            icon = getArguments ( ).getInt ( ARG_IMAGE );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_icons, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        iv = view.findViewById ( R.id.image_view_icon );
        displayDetails(icon);
    }

    private void displayDetails(int icon) {
        iv.setImageResource ( icon );
        iv.setMinimumWidth ( 150 );
        iv.setMinimumHeight ( 150 );
        iv.setColorFilter ( getResources ().getColor ( android.R.color.holo_red_dark, requireContext().getTheme()) );
        iv.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Fragment fragment = getActivity ().getSupportFragmentManager ( )
                            .findFragmentByTag ( MyDialogFragmentImageView.TAG );
                ((MyDialogFragmentImageView) fragment).getImageView(icon);
            }
        } );
    }
}