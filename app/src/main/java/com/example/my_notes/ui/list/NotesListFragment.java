package com.example.my_notes.ui.list;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.LinearLayoutCompat;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.dialog.MyDialogFragmentImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment{

//    private final SimpleDateFormat formatDate = new SimpleDateFormat("E dd.BB.yyyy 'и время' hh:mm:ss a zzz", Locale.getDefault());

    public static final String TAG = "NotesListFragment";

    public LinearLayoutCompat notesContainer;

    public List<Note> notes, deleteNotes;

    public static final String ARG_NOTES = "ARG_NOTES";

    public static final String ARG_NOTE = "ARG_NOTE";

    public static final String ARG_DELETE_NOTE = "ARG_DELETE_NOTE";

    public static final String DELETE_NOTE_KEY = "DELETE_NOTE_KEY";

    public static final String ARG_INDEX = "ARG_INDEX";

    public static final String RESULT_KEY = "NotesListFragment_RESULT";

    public static final String ADD_DELETE_KEY = "NotesListFragment_ADD_DELETE";

    public static final String CREATE_DELETE_KEY = "NotesListFragment_CREATE_DELETE";

    public int index;

    public Toolbar toolbar;

    public LinearLayout layoutToolbar;

    public FloatingActionButton fab;

    public DrawerLayout drawer;

    public BottomNavigationView bottomNavigationView;

    public NavigationView navigationView;

    public TextView deleteCounter;
    public TextView counter;
    public ImageView chooseAll;

    public NotesListFragment() {
    }

    public static NotesListFragment newInstance(List<Note> notes, int index, List<Note> deleteNotes) {
        NotesListFragment fragment = new NotesListFragment ( );
        Bundle args = new Bundle ( );
        args.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) notes );
        args.putInt ( ARG_INDEX, index );
        args.putParcelableArrayList ( ADD_DELETE_KEY, (ArrayList<? extends Parcelable>) deleteNotes );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            notes = getArguments ( ).getParcelableArrayList ( ARG_NOTES );
            index = getArguments ( ).getInt ( ARG_INDEX );
            deleteNotes = getArguments ( ).getParcelableArrayList ( ADD_DELETE_KEY );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.app_bar_fragment_note_list, container, false );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        navigationView = view.findViewById ( R.id.nav_view );//Шторка

        createNavView ( navigationView );

        drawer = view.findViewById ( R.id.drawer );
        toolbar = view.findViewById ( R.id.toolbar );
        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {
            supplyToolbar ( toolbar );
        }

        toolbarItemClick();

        layoutToolbar = view.findViewById ( R.id.layout_toolbar );
        deleteCounter = view.findViewById ( R.id.delete_counter );
        counter = view.findViewById ( R.id.counter );
        chooseAll = view.findViewById ( R.id.choose_all );

        bottomNavigationView = view.findViewById ( R.id.bottom_navigation );//popup

        fab = view.findViewById ( R.id.fab );
        ((MainActivity) requireActivity ( )).fabEventHandling ( fab );

        notesContainer = view.findViewById ( R.id.container_notes );

        showNotes ( notes );

        NestedScrollView scrollView = view.findViewById ( R.id.scroll_list );

        scrollView.requestChildFocus ( notesContainer, notesContainer.getChildAt ( index ) );

        if (notes.size ( ) != 0 && index >= 0) {
            notesContainer.getChildAt ( index ).setBackground ( getResources ( ).getDrawable ( R.drawable.layout_bg_2, requireContext ( ).getTheme ( ) ) );
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void toolbarItemClick() {
        toolbar.setOnMenuItemClickListener ( item -> {
            switch (item.getItemId ( )) {
                case R.id.search:
                    SearchView searchView = (SearchView) item.getActionView ( );
                    searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            notes = ((MainActivity)requireActivity ()).searchNote(newText);
                            showNotes ( notes );
                            return true;
                        }
                    } );
                    return true;
                case R.id.note_folders:
                    Toast.makeText ( requireContext (), item.getTitle (), Toast.LENGTH_SHORT ).show ( );

/*                presenter.clearDb ( );
            notes = presenter.refreshNotes ( );
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
            recreate ();*/
                    return true;
                default:
                    return false;
            }
        } );
    }

    private void createNavView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener ( new NavigationView.OnNavigationItemSelectedListener ( ) {
            @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ( )) {
                    case R.id.add_folder:
                        new MyDialogFragmentImageView ( ).show ( requireActivity ().getSupportFragmentManager ( ), MyDialogFragmentImageView.TAG );
//                        drawer.closeDrawer ( GravityCompat.START );
                        return true;
                    case R.id.delete_folder:
                        Toast.makeText ( requireContext (), "delete_folder", Toast.LENGTH_SHORT ).show ( );
                        return true;
                    case R.id.search_nav_list:
                        Toast.makeText ( requireContext (), "search", Toast.LENGTH_SHORT ).show ( );
                        return true;
                    default:
                        return false;
                }
            }
        } );
    }

    public void showNotes(List<Note> notes) {
        notesContainer.removeAllViews ( );

        toolbar.setTitle ( ((MainActivity) requireActivity ( )).getGroupName ( ));

        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {
            if (deleteNotes.size ( ) > 0) {
                createBottomNavigation ( );
                fab.setVisibility ( View.GONE );
                bottomNavigationView.setVisibility ( View.VISIBLE );
                toolbar.getMenu ( ).setGroupVisible ( R.id.search_note_folders, false );
                layoutToolbar.setVisibility ( View.VISIBLE );
                showLayoutToolbar ( );
            } else {
                fab.setVisibility ( View.VISIBLE );
                bottomNavigationView.setVisibility ( View.GONE );
                toolbar.getMenu ( ).setGroupVisible ( R.id.search_note_folders, true );
                layoutToolbar.setVisibility ( View.GONE );
                supplyToolbar ( toolbar );
            }
        }

        for (Note note : notes) {

            View itemView = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_note, notesContainer, false );

            TextView title = itemView.findViewById ( R.id.note_title );
            title.setText ( note.getTitle ( ) );

            TextView text = itemView.findViewById ( R.id.note_text );
            text.setText ( note.getText ( ) );

            TextView data = itemView.findViewById ( R.id.note_date );

            CheckBox checkBox = itemView.findViewById ( R.id.delete_index );

            for (int i = 0; i < deleteNotes.size ( ); i++) {
                if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
                    checkBox.setChecked ( true );
                }
            }

/*            SimpleDateFormat formatDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                formatDate = new SimpleDateFormat("день недели:  EEEE\n дата________:   dd MMMM yyyy" +
//                        "\nВремя______:   hh:mm", Locale.getDefault());
                formatDate = new SimpleDateFormat ( "EEEE  dd MMMM yyyy   hh:mm", Locale.getDefault ( ) );
            }

            assert formatDate != null;
            StringBuilder sb = new StringBuilder(formatDate.format( Date.parse ( note.getData() )).toLowerCase());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            String data1 = sb.toString();

            data.setText(data1);*/

            data.setText ( note.getData ( ) );

            notesContainer.addView ( itemView );

            if (deleteNotes.size ( ) > 0) {
                checkBox.setVisibility ( View.VISIBLE );
                itemView.setOnClickListener ( view -> {
                    if (checkBox.isChecked ( )) {
                        checkBox.setChecked ( false );
                        for (int i = 0; i < deleteNotes.size ( ); i++) {
                            if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
                                deleteNotes.remove ( note );
                            }
                        }
                    } else {
                        checkBox.setChecked ( true );
                        deleteNotes.add ( note );
                    }

                    checkBox.setOnClickListener ( new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            Bundle data1 = new Bundle ( );
                            data1.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );

                            getParentFragmentManager ( )
                                    .setFragmentResult ( CREATE_DELETE_KEY, data1 );
                        }
                    } );

                    Bundle data1 = new Bundle ( );
                    data1.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );

                    getParentFragmentManager ( )
                            .setFragmentResult ( CREATE_DELETE_KEY, data1 );
                } );
            } else {
                itemView.setOnClickListener ( view -> {
                    Bundle data12 = new Bundle ( );
                    data12.putParcelable ( ARG_NOTE, note );

                    getParentFragmentManager ( )
                            .setFragmentResult ( RESULT_KEY, data12 );
                } );

                itemView.setOnLongClickListener ( view -> {
                    checkBox.setVisibility ( View.VISIBLE );
                    checkBox.setChecked ( true );
                    deleteNotes.add ( note );

                    Bundle data13 = new Bundle ( );
                    data13.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );

                    getParentFragmentManager ( )
                            .setFragmentResult ( CREATE_DELETE_KEY, data13 );
                    return false;
                } );
            }
        }
    }

    private void createBottomNavigation() {
        MenuItem menuItem = bottomNavigationView.getMenu ( ).getItem ( 0 );
        menuItem.setCheckable ( false );

        bottomNavigationView.setOnItemSelectedListener ( new NavigationBarView.OnItemSelectedListener ( ) {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable ( false );
                switch (item.getItemId ( )) {
                    case R.id.save:

                    case R.id.move:
                        Toast.makeText ( requireContext ( ), "Здесь что-то будет", Toast.LENGTH_SHORT ).show ( );
                        return true;
                    case R.id.delete_selected_notes:
                        Bundle data = new Bundle ( );
                        data.putParcelableArrayList ( ARG_DELETE_NOTE, (ArrayList<? extends Parcelable>) deleteNotes );

                        getParentFragmentManager ( )
                                .setFragmentResult ( DELETE_NOTE_KEY, data );
                        return true;
                }
                return false;
            }
        } );
    }

    private void showLayoutToolbar() {
        deleteCounter.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                deleteNotes.clear ( );
                showNotes ( notes );
            }
        } );

        if (deleteNotes.size ( ) > 0) {
            counter.setText ( "Выбрано: " + deleteNotes.size ( ) );
        }

        chooseAll.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (deleteNotes.size ( ) == notes.size ( )) {
                    deleteNotes.clear ( );
                } else {
                    deleteNotes.clear ( );
                    deleteNotes.addAll ( notes );
                }
                showNotes ( notes );
            }
        } );
    }

    public void supplyToolbar(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                requireActivity ( ),
                drawer,
                toolbar,
                R.string.nav_app_bar_navigate_up_description,
                R.string.nav_app_bar_navigate_up_description
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened ( drawerView );
                Menu menu = navigationView.getMenu ();
                menu.removeGroup ( R.id.group );
                fillMenu ( menu );
            }
        };
        drawer.addDrawerListener ( toggle );
        toggle.syncState ( );
    }

    public void fillMenu( Menu menu ) {
        List<Group> groups = ((MainActivity) requireActivity ( )).getGroups ();
        for (int i = 0; i < groups.size (); i++){
            final int position = i;
            menu.add ( R.id.group, i + 1, Menu.NONE, groups.get ( i ).getName () )
                    .setCheckable ( true )
                    .setIcon ( groups.get ( position ).getIcon ()  )
                    .setActionView ( R.layout.counter_notes );
            TextView textNameGroup = menu.getItem ( i + 1 ).getActionView ().findViewById ( R.id.value_counter_notes );
            textNameGroup.setText (  String.valueOf ( groups.get ( position ).getCount () ) );
            menu.getItem ( i + 1 ).setOnMenuItemClickListener ( new MenuItem.OnMenuItemClickListener ( ) {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    long group_id = groups.get ( position ).getId ();
                    ((MainActivity) requireActivity ( )).setGroupId(group_id);
                    notes = ((MainActivity) requireActivity ( )).getNotes();
                    showNotes ( notes );
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            } );
        }
    }

    public void showFillMenu() {
        Menu menu = navigationView.getMenu ();
        menu.removeGroup ( R.id.group );
        fillMenu ( menu );
    }
}