package com.example.my_notes.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.my_notes.domain.Group;
import com.example.my_notes.ui.detail.GroupNameFragment;

import java.util.List;

public class MyAdapterGroup extends FragmentStateAdapter {

    private final List<Group> groups;

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
