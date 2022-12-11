package com.example.my_notes.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.detail.NoteDetailFragment;

import java.util.List;

public class MyAdapter extends FragmentStateAdapter {

    private final List<Note> notes;

    public MyAdapter(FragmentActivity fragmentActivity, List<Note> notes) {
        super(fragmentActivity);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Note note = notes.get(position);
        return(NoteDetailFragment.newInstance ( note ));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
