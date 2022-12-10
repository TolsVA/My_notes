package com.example.my_notes.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.my_notes.R;
import com.example.my_notes.domain.Note;

public class NoteDetailActivity extends AppCompatActivity implements MyDialogFragment.ClickDatePickerDialog  {

    public static final String EXTRA_NOTE = "EXTRA_NOTE";

    private Note note;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
            if (note != null) {
                outState.putParcelable(EXTRA_NOTE, note);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_note_detail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        } else {

            if (savedInstanceState == null) {
                FragmentManager fm = getSupportFragmentManager();

                note = getIntent().getParcelableExtra(EXTRA_NOTE);

                fm.beginTransaction()
                        .replace ( R.id.fragment_container_detail, NoteDetailFragment.newInstance(note), NoteDetailFragment.TAG)
                        .commit();
            }
        }

        getSupportFragmentManager()
                .setFragmentResultListener(NoteDetailFragment.RESULT_KEY_DETAIL_FRAGMENT, this, (requestKey, result) -> {
                    Note note = result.getParcelable(NoteDetailFragment.ARG_NEW_NOTE);

                    Intent data = new Intent();
                    data.putExtra(EXTRA_NOTE, note);
                    setResult(Activity.RESULT_OK, data);

                    finish();
                } );
    }

    @Override
    public void applySettings(String text, int id) {
        TextView textView = findViewById ( id );
        textView.setText ( text );
    }
}