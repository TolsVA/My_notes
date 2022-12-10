package com.example.my_notes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.my_notes.domain.InMemoryRepository;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.detail.MyDialogFragment;
import com.example.my_notes.ui.detail.NoteDetailActivity;
import com.example.my_notes.ui.detail.NoteDetailFragment;
import com.example.my_notes.ui.list.NotesListFragment;
import com.example.my_notes.ui.list.NotesListPresenter;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.ClickDatePickerDialog {

    public static final String ARG_NOTE_ACTIVITY_MAIN = "ARG_NOTE_ACTIVITY_MAIN";

    private Note note, selectedNote;

    private NotesListPresenter presenter;

    public List<Note> notes;

    public FragmentManager fm;

    SimpleDateFormat formatDate;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult ( new ActivityResultContracts.StartActivityForResult ( ), new ActivityResultCallback<ActivityResult> ( ) {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode ( ) == Activity.RESULT_OK) {

                assert result.getData ( ) != null;
                note = result.getData ( ).getParcelableExtra ( NoteDetailActivity.EXTRA_NOTE );

                if (selectedNote.getIndex ( ) != -1) {
                    presenter.upgradeNote ( note );
                } else {
                    presenter.addNote ( note );
                }
                selectedNote = note;
                notes = presenter.refresh ( );

                fm.beginTransaction ( )
                        .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes ), NotesListFragment.TAG )
                        .commit ( );
            }
        }
    } );


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState ( outState );

        if (selectedNote != null) {
            outState.putParcelable ( ARG_NOTE_ACTIVITY_MAIN, selectedNote );
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        ActionBar actionBar = getSupportActionBar ( );
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled ( true );

        formatDate = new SimpleDateFormat("E '('dd MMMM yyyy')' '-->' hh:mm", Locale.getDefault());

        presenter = new NotesListPresenter (  new InMemoryRepository ( this ) );

        fm = getSupportFragmentManager ( );

        notes = presenter.refresh ( );

        if (savedInstanceState != null && savedInstanceState.containsKey ( ARG_NOTE_ACTIVITY_MAIN )) {
            selectedNote = savedInstanceState.getParcelable ( ARG_NOTE_ACTIVITY_MAIN );
        }

        if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showDetails ( selectedNote );
        }

        fm.beginTransaction ( )
                .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes ) )
                .commit ( );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.RESULT_KEY, this, (requestKey, result) -> {
                    selectedNote = result.getParcelable ( NotesListFragment.ARG_NOTE );

                    if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        showDetails ( selectedNote );
                    } else {
                        Intent intent = new Intent ( MainActivity.this, NoteDetailActivity.class );
                        intent.putExtra ( NoteDetailActivity.EXTRA_NOTE, selectedNote );
                        launcher.launch ( intent );
                    }
                } );
        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.DELETE_KEY, this, (requestKey, result) -> {
                    note = result.getParcelable ( NotesListFragment.ARG_NOTE );

                    long index = note.getIndex ( );
                    presenter.deleteIndex ( index );
                    notes = presenter.refresh ( );

                    showNotes ( null );

                } );
        getSupportFragmentManager ( )
                .setFragmentResultListener ( NoteDetailFragment.RESULT_KEY_DETAIL_FRAGMENT, this, (requestKey, result) -> {
                    note = result.getParcelable ( NoteDetailFragment.ARG_NEW_NOTE );
                    if (note.getIndex ( ) != -1) {
                        presenter.upgradeNote ( note );
                    } else {
                        selectedNote = presenter.addNote ( note );
                    }
                    notes = presenter.refresh ( );

                    showNotes ( note );
                } );
    }

    private void showNotes(Note note) {
        if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showDetails ( note );
        }
        fm.beginTransaction ( )
                .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes ), NotesListFragment.TAG )
                .commit ( );
    }

    @SuppressLint("NewApi")
    private void showDetails(Note note) {
        if (note == null) {
            selectedNote = new Note ( -1, "", "", formatDate.format ( new Date () )  );
        }

        Bundle bundle = new Bundle ( );
        bundle.putParcelable ( NoteDetailFragment.ARG_NOTE, selectedNote );
        getSupportFragmentManager ( )
                .setFragmentResult ( NoteDetailFragment.RESULT_KEY, bundle );

    }

    @Override
    public void onDestroy() {
        super.onDestroy ( );
        presenter.closeDb ( );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ( );
        inflater.inflate ( R.menu.main, menu );
        return super.onCreateOptionsMenu ( menu );
    }

    @SuppressLint({"NewApi", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ( )) {
            case android.R.id.home:
//                finish();
                return true;
            case R.id.add_note:
                selectedNote = new Note ( -1, "", "", formatDate.format ( new Date () ) );
                if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    showDetails ( selectedNote );
                } else {
                    Intent intent = new Intent ( MainActivity.this, NoteDetailActivity.class );
                    intent.putExtra ( NoteDetailActivity.EXTRA_NOTE, selectedNote );
                    launcher.launch ( intent );
                }
                return true;
            case R.id.search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        notes = presenter.search ( newText );
                        FragmentManager fm = getSupportFragmentManager();
                        fm.popBackStack();
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, NotesListFragment.newInstance(notes), NotesListFragment.TAG)
                                .commit();
                        return false;
                    }
                });
                return true;
            case R.id.clear_history:
                selectedNote = new Note ( -1, "", "", formatDate.format ( new Date () )  );

                presenter.clearDb ( );

                recreate ( );
                return true;
            default:
                return super.onOptionsItemSelected ( item );
        }
    }

    @Override
    public void applySettings(String text, int id) {
        TextView textView = findViewById ( id );
        textView.setText ( text );
    }
}