package com.example.my_notes.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.my_notes.R;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapter.MyAdapterIcon;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;

public class MyDialogFragmentImageView extends DialogFragment {

    public static final String TAG = "MyDialogFragmentImageView";

    public static final String ARG_NOTE = "ARG_NOTE";

    public Note note;

    public ViewPager2 pager2;
    public ImageView imageView;
    public TypedArray icons;

    public int resourceId;

    public long groupId;

    public static MyDialogFragmentImageView newInstance(Note note) {
        MyDialogFragmentImageView fragment = new MyDialogFragmentImageView ( );
        Bundle args = new Bundle ( );
        args.putParcelable ( ARG_NOTE, note );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            note = getArguments ().getParcelable ( ARG_NOTE );
        }
        setCancelable ( false );
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View customView = getLayoutInflater().inflate( R.layout.icon_name_group, null);

        imageView = customView.findViewById ( R.id.icon_choose );
        EditText editText = customView.findViewById ( R.id.folder_name );

        icons = getResources().obtainTypedArray( R.array.coat_of_arms_images);
        pager2 = customView.findViewById ( R.id.layout_image );
        pager2.setAdapter ( new MyAdapterIcon ( this, icons ) );
        pager2.setPageTransformer ( new ZoomOutPageTransformer ( ) );
        savePosition();

        return new AlertDialog.Builder(requireContext ())
                .setView(customView)
                .setPositiveButton("Создать папку", (dialogInterface, i) -> {
                    Activity activity = requireActivity ( );
                    if (activity instanceof DialogClickListener) {
                        groupId = ((DialogClickListener) activity).createNewGroup ( resourceId, String.valueOf ( editText.getText ( ) ) );
                        note.setGroup_id ( groupId );
                        ((DialogClickListener) activity).showNotesListFragment (  note  );
                    }
                } )
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
}
