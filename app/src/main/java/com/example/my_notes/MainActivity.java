package com.example.my_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.InMemoryRepository;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapter.MyAdapter;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;
import com.example.my_notes.ui.detail.MyDialogFragment;
import com.example.my_notes.ui.detail.NoteDetailFragment;
import com.example.my_notes.ui.list.NotesListFragment;
import com.example.my_notes.ui.list.NotesListPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.ClickDatePickerDialog {

    public SharedPreferences pref;

    public static final String ARG_INDEX = "ARG_INDEX";

    public List<Note> notes;

    public Note note, newNote;

    public Group group;

    public List<Group> groups;

    public int index = 0;

    public int indexPrev = -1;

    public ViewPager2 pager;

    private FloatingActionButton fab;

    private CoordinatorLayout coordinatorLayout;

    private View itemView;

    public List<Note> deleteNotes;

    private NotesListPresenter presenter;

    private Toolbar toolbar;

    private DrawerLayout drawer;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (index != -1) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ARG_INDEX, index);
            editor.apply();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy ( );
        presenter.closeDb ( );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        deleteNotes = new ArrayList<> ( );

        pref = getSharedPreferences("TABLE", MODE_PRIVATE);

        index = pref.getInt(ARG_INDEX, 0);

        presenter = new NotesListPresenter ( new InMemoryRepository(this) );

        notes = presenter.refreshNotes ();

        drawer = findViewById(R.id.drawer);

        supplyToolbar();

        coordinatorLayout = findViewById(R.id.fragment_container);

        pager = findViewById(R.id.fragment_container_detail);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener( view -> {
            fab.setVisibility(View.GONE);
            view.setClickable(false);
            view.setVisibility(View.GONE);

            String folderName = getString ( R.string.Uncategorized );
            if (presenter.checkGroupFor ( folderName ) == 0) {
                group = new Group ( -1, folderName, R.drawable.ic_baseline_folder_copy_24 );
                group = presenter.addGroup ( group );
            } else {
                group = presenter.searchByGroupName( folderName );
            }

            newNote = new Note(-1, "", "", String.valueOf (new Date ()), group.getIndex ());
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                pager.setAdapter(changeContent( Collections.singletonList(newNote)));
            } else {
                Fragment fmList = getSupportFragmentManager()
                        .findFragmentByTag(NotesListFragment.TAG);

                FragmentManager fmDetail = getSupportFragmentManager();

                assert fmList != null;
                fmDetail.beginTransaction()
                        .remove(fmList)
                        .addToBackStack("")
                        .add(R.id.fragment_container, NoteDetailFragment.newInstance(newNote), NoteDetailFragment.TAG)
                        .commit();
            }
            selectPosition();
        } );

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
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(true);
                        pager.setCurrentItem ( index, false );
                    } else {
                        fab.setVisibility(View.GONE);

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

                    fab.setVisibility ( View.VISIBLE );
                    fab.setClickable(true);

                    if (note.getIndex ( ) != -1) {
                        presenter.upgradeNote ( note );
                        notes = presenter.refreshNotes ();
                    } else {
                        presenter.addNote ( note );
                        notes = presenter.refreshNotes ();
                        indexPrev = index;
                        index = 0;
                        note = notes.get ( index );
                    }

                    if (getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (notes.size () > 1) {
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
                    fm.popBackStack ();
                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                            .commit ( );

//                    // Перерисовать фрагмент способ 2 найти фрагмент по TAG
//                    Fragment fmList = getSupportFragmentManager ( )
//                            .findFragmentByTag ( NotesListFragment.TAG );
//                    fm.popBackStack ();
//                    assert fmList != null;
//                    ((NotesListFragment)fmList).showNotes(notes);
//
//                    //Перерисовать фрагмент способ 3 найти фрагмент пройдя по стеку фрагментов this.getSupportFragmentManager ().getFragments ()
//                    for (Fragment fragment : this.getSupportFragmentManager ().getFragments ()) {
//                        if (fragment instanceof NotesListFragment) {
//                            ((NotesListFragment) fragment).showNotes ( notes );
//                            break;
//                        }
//                    }
                } );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NoteDetailFragment.RESULT_KEY_GROUP_FRAGMENT, this, (requestKey, result) -> {
                    groups = presenter.refreshGroup ();

                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, GroupFragment.newInstance ( groups ), GroupFragment.TAG )
                            .commit ( );
                } );

        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.CREATE_DELETE_KEY, this, (requestKey, result) -> {
                    deleteNotes = result.getParcelableArrayList ( NotesListFragment.ARG_NOTES );
                    fab.setVisibility ( View.GONE );

                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                            .commit ( );
                } );
        getSupportFragmentManager ( )
                .setFragmentResultListener ( NotesListFragment.ADD_DELETE_KEY, this, (requestKey, result) -> {
                    deleteNotes = result.getParcelableArrayList ( NotesListFragment.ARG_NOTES );

                    fm.beginTransaction ( )
                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
                            .commit ( );
                } );
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void selectPosition() {

        NestedScrollView scrollView = findViewById ( R.id.scroll_list );
        LinearLayoutCompat notesContainer = findViewById ( R.id.container_notes );

        if (notes.size () > 0) {
            scrollView.requestChildFocus ( notesContainer, notesContainer.getChildAt ( index ) );
            notesContainer.getChildAt ( index ).setBackground ( getDrawable ( R.drawable.layout_bg_2 ) );
        }

        if (indexPrev >= 0 && index != indexPrev) {
            notesContainer.getChildAt ( indexPrev )
                    .setBackground ( getDrawable ( R.drawable.layout_bg ) );
        }
    }

    private FragmentStateAdapter changeContent(List<Note> _notes) {
        return new MyAdapter(MainActivity.this, _notes);
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
        if (coordinatorLayout != null) {

            coordinatorLayout.removeView ( itemView );

            fab.setVisibility ( View.VISIBLE );

            Fragment fmList = getSupportFragmentManager ( )
                    .findFragmentByTag ( NotesListFragment.TAG );

            assert fmList != null;
            getSupportFragmentManager ( )
                    .beginTransaction ( )
                    .show ( fmList )
                    .commit ( );

//            savePosition();
        }
        super.onBackPressed ( );
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
            case R.id.search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        int index2 = -1;
                        notes = presenter.search ( newText );
                        FragmentManager fm = getSupportFragmentManager();
                        fm.popBackStack();
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, NotesListFragment.newInstance(notes, index2, deleteNotes), NotesListFragment.TAG)
                                .commit();
                        return false;
                    }
                });
                return true;
            case R.id.clear_history:

                presenter.clearDb ( );
                notes = presenter.refreshNotes ();
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
//                recreate ();
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

    public void supplyToolbar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.nav_app_bar_navigate_up_description,
                R.string.nav_app_bar_navigate_up_description
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}