package com.example.my_notes.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.detail.GroupNameFragment;
import com.example.my_notes.ui.detail.NoteDetailFragment;

import java.util.List;

public class MyAdapterGroup extends FragmentStateAdapter {

    private final List<Group> groups;

    private FragmentActivity fragmentActivity;


    public MyAdapterGroup(FragmentActivity fragmentActivity, List<Group> groups) {
        super(fragmentActivity);
        this.groups = groups;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Group group = groups.get(position);
        return(GroupNameFragment.newInstance ( group ));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
