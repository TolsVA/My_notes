package com.example.my_notes.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.ui.adapter.MyAdapterIcon;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;
import com.example.my_notes.ui.list.NotesListFragment;

public class MyDialogFragmentImageView extends DialogFragment {

    public static final String TAG = "MyDialogFragmentImageView";

    public ViewPager2 pager2;
    public ImageView imageView;
    public TypedArray icons;

    public int resourceId;

    public interface CreateNewGroup {
        void CreateNewGroup(int resourceId, String text);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View customView = getLayoutInflater().inflate( R.layout.icon_name_group, null);

        imageView = customView.findViewById ( R.id.icon_choose );
        EditText editText = customView.findViewById ( R.id.folder_name );

/*        LinearLayout container = customView.findViewById ( R.id.layout_image );
        final int[] resourceIdNew = new int[1];

        @SuppressLint("Recycle")
        TypedArray icons = getResources().obtainTypedArray( R.array.coat_of_arms_images);
            for (int i = 0; i < icons.length ( ); i++) {
                final int resourceId = icons.getResourceId ( i, 0 );
                ImageView iv = new ImageView ( requireContext () );
                iv.setImageResource ( resourceId );
                iv.setMinimumWidth ( 150 );
                iv.setMinimumHeight ( 150 );
                iv.setColorFilter ( getResources ().getColor ( R.color.purple_700 , requireContext().getTheme()) );
                iv.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        imageView.setVisibility ( View.VISIBLE );
                        imageView.setImageResource ( resourceId );
                        resourceIdNew[0] = resourceId;
                    }
                } );
                container.addView ( iv );
            }
        icons.recycle();*/

        icons = getResources().obtainTypedArray( R.array.coat_of_arms_images);
        pager2 = customView.findViewById ( R.id.layout_image );
        pager2.setAdapter ( new MyAdapterIcon ( this, icons ) );
        pager2.setPageTransformer ( new ZoomOutPageTransformer ( ) );
        savePosition();

        return new AlertDialog.Builder(requireContext ())
//                .setTitle ( "Выбери иконку для папки" )
                .setView(customView)
                .setPositiveButton("Создать папку", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((CreateNewGroup) requireActivity ()).CreateNewGroup ( resourceId, String.valueOf ( editText.getText ( ) ) );
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

    private void savePosition() {
        this.pager2.registerOnPageChangeCallback ( new ViewPager2.OnPageChangeCallback ( ) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled ( position, positionOffset, positionOffsetPixels );
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected ( position );
                resourceId = icons.getResourceId ( position , 0 );
                imageView.setImageResource ( resourceId );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged ( state );
            }
        } );
    }

//    public void getImageView(int resourceId) {
//        this.resourceId = resourceId;
//        imageView.setImageResource ( resourceId );
//        imageView.setColorFilter ( getResources ().getColor ( R.color.purple_700, requireContext().getTheme()) );
//    }
}