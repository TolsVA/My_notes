package com.example.my_notes.ui.detail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.my_notes.R;

public class MyDialogFragmentImageView extends DialogFragment {

    public static final String TAG = "MyDialogFragmentImageView";

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View customView = getLayoutInflater().inflate( R.layout.icon_group, null);
        GridLayout containerGrid = customView.findViewById ( R.id.grid_layout_image );

        @SuppressLint("Recycle")
        TypedArray icons = getResources().obtainTypedArray( R.array.coat_of_arms_images);

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < icons.length ( ); i++) {
                ImageView iv = new ImageView ( requireContext () );
                iv.setImageResource ( icons.getResourceId ( i, 0 ) );
                iv.setMinimumWidth ( 150 );
                iv.setMinimumHeight ( 150 );
                iv.setColorFilter ( getResources ().getColor ( R.color.purple_700 , requireContext().getTheme()) );
                iv.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText ( requireContext (), "iv", Toast.LENGTH_SHORT ).show ( );
                    }
                } );
                containerGrid.addView ( iv );
            }
        }

        return new AlertDialog.Builder(requireContext ())
                .setTitle ( "Выбери иконку для папки" )
                .setView(customView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext (), "Yes!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext (), "No!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancel", null)
                .create();
    }
}
