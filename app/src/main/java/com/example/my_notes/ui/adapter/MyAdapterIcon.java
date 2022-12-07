package com.example.my_notes.ui.adapter;

import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.my_notes.ui.detail.IconsFragment;

public class MyAdapterIcon extends FragmentStateAdapter {

    private final TypedArray icons;

    public MyAdapterIcon(@NonNull Fragment fragment, TypedArray icons) {
        super ( fragment );
        this.icons = icons;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(IconsFragment.newInstance(icons.getResourceId ( position, 0 )));
    }

    @Override
    public int getItemCount() {
        return icons.length ();
    }
}
