package com.example.my_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultOwner;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.InMemoryRepository;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapter.MyAdapter;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;
import com.example.my_notes.ui.dialog.MyDialogFragment;
import com.example.my_notes.ui.detail.NoteDetailFragment;
import com.example.my_notes.ui.dialog.MyDialogFragmentImageView;
import com.example.my_notes.ui.list.NotesListFragment;
import com.example.my_notes.ui.list.NotesListPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.ClickDatePickerDialog,
        MyDialogFragmentImageView.CreateNewGroup {

    public SharedPreferences pref;

    public static final String ARG_INDEX = "ARG_INDEX";
    public static final String ARG_GROUP_ID = "ARG_GROUP_ID";

    public List<Note> notes;

    public Note note, newNote;

    public Group group;

    public long group_id;

    public Group newGroup;

    public List<Group> groups;

    public int index;

    public int indexPrev = -1;

    public ViewPager2 pager;

    private FloatingActionButton fab;

    private CoordinatorLayout coordinatorLayout;

    private View itemView;

    public List<Note> deleteNotes;

    private NotesListPresenter presenter;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState ( outState );
        SharedPreferences.Editor editor = pref.edit ( );
        if (index != -1) {
            editor.putInt ( ARG_INDEX, index );
        }
        editor.putLong ( ARG_GROUP_ID, group_id );
        editor.apply ( );
    }


    @Override
    public void onDestroy() {
        super.onDestroy ( );
        presenter.closeDb ( );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        deleteNotes = new ArrayList<> ( );

        pref = getSharedPreferences ( "TABLE", MODE_PRIVATE );

        index = pref.getInt ( ARG_INDEX, 0 );

        group_id = pref.getLong ( ARG_GROUP_ID, 0 );

        presenter = new NotesListPresenter ( new InMemoryRepository ( this ) );

        notes = presenter.refreshNotes ( group_id );

        coordinatorLayout = findViewById ( R.id.fragment_container );

        pager = findViewById ( R.id.fragment_container_detail );

        FragmentManager fm = getSupportFragmentManager ( );
        fm.popBackStack ( );
        fm.beginTransaction ( )
                .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                .commit ( );
        if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showDetails ( );
            savePosition ( );
        }

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.RESULT_KEY, this, (requestKey, result) -> {
                    note = result.getParcelable ( NotesListFragment.ARG_NOTE );

                    indexPrev = index;
                    index = notes.indexOf ( note );

                    if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fab.setVisibility ( View.VISIBLE );
                        fab.setClickable ( true );
                        pager.setCurrentItem ( index, false );
                    } else {
                        fab.setVisibility ( View.GONE );

                        Fragment fmList = getSupportFragmentManager ( )
                                .findFragmentByTag ( NotesListFragment.TAG );

                        if (fmList != null) {
                            getSupportFragmentManager ( )
                                    .beginTransaction ( )
                                    .addToBackStack ( "" )
                                    .hide ( fmList )
                                    .commit ( );
                        }

                        itemView = LayoutInflater.from ( MainActivity.this )
                                .inflate ( R.layout.view_pager2, coordinatorLayout, false );

                        pager = itemView.findViewById ( R.id.fragment_container_detail );

                        showDetails ( );

                        savePosition ( );

                        if (index > 0) {
                            selectPosition ( );
                        }
                        coordinatorLayout.addView ( itemView );
                    }
                } );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NoteDetailFragment.RESULT_KEY_DETAIL_FRAGMENT, this, (requestKey, result) -> {
                    note = result.getParcelable ( NoteDetailFragment.ARG_NEW_NOTE );
                    showNotesList ( note );
                } );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.CREATE_DELETE_KEY, this, (requestKey, result) -> {
                    deleteNotes = result.getParcelableArrayList ( NotesListFragment.ARG_NOTES );

                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                            .commit ( );
                } );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.DELETE_NOTE_KEY, this, (requestKey, result) -> {
                    deleteNotes = result.getParcelableArrayList ( NotesListFragment.ARG_DELETE_NOTE );

                    for (Note note : deleteNotes) {
                        presenter.deleteIndex ( note.getIndex ( ) );
                    }
                    deleteNotes.clear ( );
                    notes = presenter.refreshNotes ( group_id );
                    index = 0;

                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                            .commit ( );
                } );
    }

    private void showNotesList(Note note) {
        if (note.getIndex ( ) != -1) {
            presenter.upgradeNote ( note );
            notes = presenter.refreshNotes ( group_id );
        } else {
            presenter.addNote ( note );
            notes = presenter.refreshNotes ( group_id );
            indexPrev = index;
            index = 0;
            note = notes.get ( index );
        }

        if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (notes.size ( ) > 1) {
                showDetails ( );
            }
        } else {
            Fragment f = getSupportFragmentManager ( )
                    .findFragmentByTag ( NoteDetailFragment.TAG );

            if (f != null) {
                getSupportFragmentManager ( )
                        .beginTransaction ( )
                        .remove ( f )
                        .commit ( );
            }

            if (coordinatorLayout != null) {
                coordinatorLayout.removeView ( itemView );
            }
        }

        //Перерисовать фрагмент способ 1
        FragmentManager fm = getSupportFragmentManager ( );
        fm.popBackStack ( );
        fm.beginTransaction ( )
                .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                .commit ( );

/*                     Перерисовать фрагмент способ 2 найти фрагмент по TAG
                    Fragment fmList = getSupportFragmentManager ( )
                            .findFragmentByTag ( NotesListFragment.TAG );
                    fm.popBackStack ();
                    assert fmList != null;
                    ((NotesListFragment)fmList).showNotes(notes);

                    //Перерисовать фрагмент способ 3 найти фрагмент пройдя по стеку фрагментов this.getSupportFragmentManager ().getFragments ()
                    for (Fragment fragment : this.getSupportFragmentManager ().getFragments ()) {
                        if (fragment instanceof NotesListFragment) {
                            ((NotesListFragment) fragment).showNotes ( notes );
                            break;
                        }
                    }*/
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void selectPosition() {

        NestedScrollView scrollView = findViewById ( R.id.scroll_list );
        LinearLayoutCompat notesContainer = findViewById ( R.id.container_notes );

//        if (notes.size ( ) > 0) {
//            scrollView.requestChildFocus ( notesContainer, notesContainer.getChildAt ( index ) );
//            notesContainer.getChildAt ( index ).setBackground ( getDrawable ( R.drawable.layout_bg_2 ) );
//        }
//
//        if (indexPrev >= 0 && index != indexPrev) {
//            notesContainer.getChildAt ( indexPrev )
//                    .setBackground ( getDrawable ( R.drawable.layout_bg ) );
//        }
    }

    private FragmentStateAdapter changeContent(List<Note> _notes) {
        return new MyAdapter ( MainActivity.this, _notes );
    }

    private void showDetails() {
        pager.setAdapter ( changeContent ( notes ) );
        pager.setCurrentItem ( index, false );
        pager.setPageTransformer ( new ZoomOutPageTransformer ( ) );
    }

    private void savePosition() {
        this.pager.registerOnPageChangeCallback ( new ViewPager2.OnPageChangeCallback ( ) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled ( position, positionOffset, positionOffsetPixels );
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected ( position );
                if (index != position) {
                    indexPrev = index;
                    index = position;
                }

                Toast.makeText ( MainActivity.this, "Position pager = " + position, Toast.LENGTH_SHORT ).show ( );
                selectPosition ( );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged ( state );
            }
        } );
    }

    @Override
    public void onBackPressed() {
        Toast.makeText ( this, "рас", Toast.LENGTH_SHORT ).show ( );


        Fragment fmList = getSupportFragmentManager ( )
                .findFragmentByTag ( GroupFragment.TAG );
        if (fmList == null) {
            if (coordinatorLayout != null) {

                coordinatorLayout.removeView ( itemView );

/*                fab.setVisibility ( View.VISIBLE );сег

            getSupportFragmentManager ( )
                    .beginTransaction ( )
                    .remove ( Objects.requireNonNull ( getSupportFragmentManager ( ).findFragmentByTag ( GroupFragment.TAG ) ) )
                    .commit ( );

            Fragment fmList = getSupportFragmentManager ( )
                    .findFragmentByTag ( NotesListFragment.TAG );

            assert fmList != null;
            getSupportFragmentManager ( )
                    .beginTransaction ( )
                    .show ( fmList )
                    .commit ( );

            savePosition();*/
            }
        }
        super.onBackPressed ( );
    }

    @Override
    public void applySettings(String text, int id) {
        TextView textView = findViewById ( id );
        textView.setText ( text );
    }

    public List<Group> getGroups() {
        return presenter.refreshGroup ( );
    }

    public void fabEventHandling(FloatingActionButton fab, CharSequence title) {
        this.fab = fab;
        fab.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                fab.setVisibility ( View.GONE );
                view.setClickable ( false );
                view.setVisibility ( View.GONE );


//                String folderName = MainActivity.this.getString ( R.string.uncategorized );
//                if (presenter.checkGroupFor ( folderName ) == 0) {
//                    group = new Group ( -1, folderName, R.drawable.ic_baseline_folder_24, 0 );
//                    group = presenter.addGroup ( group );
//                } else {
//                    groups = presenter.searchByGroupName ( folderName );
//                    group = groups.get ( 0 ); // Доработать
//                }

                newNote = new Note ( -1, "", "", String.valueOf ( new Date ( ) ), 0 );
                if (MainActivity.this.getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    pager.setAdapter ( MainActivity.this.changeContent ( Collections.singletonList ( newNote ) ) );
                } else {
                    Fragment fmList = MainActivity.this.getSupportFragmentManager ( )
                            .findFragmentByTag ( NotesListFragment.TAG );

                    FragmentManager fmDetail = MainActivity.this.getSupportFragmentManager ( );

                    assert fmList != null;
                    fmDetail.beginTransaction ( )
                            .remove ( fmList )
                            .addToBackStack ( "" )
                            .add ( R.id.fragment_container, NoteDetailFragment.newInstance ( newNote ), NoteDetailFragment.TAG )
                            .commit ( );
                }
                selectPosition ( );
            }
        } );
    }

    public long getGroupId() {
        return group_id;
    }

    public void setGroupId(long group_id) {
        this.group_id = group_id;
    }

    public List<Note> getNotes() {
        return presenter.refreshNotes ( group_id );
    }

    public String getGroupName() {
        groups = presenter.refreshGroup ( );
        if (group_id > 0) {
            for (Group group : groups) {
                if (group.getId ( ) == group_id) {
                    return group.getName ( );
                }
            }
        }
        return getResources ( ).getString ( R.string.you_have_no_notes );
    }

    @Override
    public long createNewGroup(int resourceId, String text) {
        newGroup = new Group ( -1, text, resourceId, 0 );
        presenter.addGroup ( newGroup );
        group_id = newGroup.getId ( );


//        for (Fragment fragment : this.getSupportFragmentManager ( ).getFragments ( )) {
//            if (fragment instanceof NotesListFragment) {
//                ((NotesListFragment) fragment).showFillMenu (  );
//                break;
//            }
//        }
        return group_id;
    }

    public List<Note> searchNote(String newText) {
        return presenter.search ( newText );
    }

    public void removeAll() {
        presenter.clearDb ( );
        notes = presenter.refreshNotes ( 0 );
        index = 0;
        indexPrev = -1;

        FragmentManager fm = getSupportFragmentManager ( );
        fm.popBackStack ( );
        fm.beginTransaction ( )
                .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                .commit ( );
        if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showDetails ( );
//                    savePosition ( );
        }
        recreate ( );
    }

    public void showNotesListFragment(Note note) {
        this.group_id = note.getGroup_id ( );
        showNotesList ( note );
    }
}