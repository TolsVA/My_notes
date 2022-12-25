package com.example.my_notes.ui.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.InMemoryRepository;
import com.example.my_notes.domain.Note;
import com.example.my_notes.domain.NoteListView;
import com.example.my_notes.ui.adapter.MyAdapter;
import com.example.my_notes.ui.adapter.ZoomOutPageTransformer;
import com.example.my_notes.ui.adapterItem.AdapterItem;
import com.example.my_notes.ui.adapterItem.GroupItem;
import com.example.my_notes.ui.adapterItem.NoteItem;
import com.example.my_notes.ui.adapterItem.ItemsAdapter;
import com.example.my_notes.ui.detail.NoteDetailFragment;
import com.example.my_notes.ui.dialog.DialogClickListener;
import com.example.my_notes.ui.dialog.MyBottomDialogFragmentGroup;
import com.example.my_notes.ui.dialog.MyDialogFragmentChoose;
import com.example.my_notes.ui.dialog.MyDialogFragmentImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment implements NoteListView {

//    private final SimpleDateFormat formatDate = new SimpleDateFormat("E dd.BB.yyyy 'и время' hh:mm:ss a zzz", Locale.getDefault());

    public static final String TAG = "NotesListFragment";
    public static final String SHOW_ALL_NOTES = "SHOW_ALL_NOTES";

    public SharedPreferences pref;

    public static final String ARG_INDEX = "NotesListFragment_ARG_INDEX";
    public static final String ARG_GROUP_ID = "NotesListFragment_ARG_GROUP_ID";

    public Note note, newNote;

    public Group group;

    public Group newGroup;

    public long group_id;

    public RecyclerView notesList;

    private ItemsAdapter adapter;

    public List<Note> notes, deleteNotes;

    public List<Group> groups;

    public static final String ARG_NOTES = "ARG_NOTES";

    public static final String ARG_NOTE = "ARG_NOTE";

    public static final String ARG_DELETE_NOTE = "ARG_DELETE_NOTE";

    public static final String DELETE_NOTE_KEY = "DELETE_NOTE_KEY";

    public static final String RESULT_KEY = "NotesListFragment_RESULT";

    public static final String ADD_DELETE_KEY = "NotesListFragment_ADD_DELETE";

    public static final String CREATE_DELETE_KEY = "NotesListFragment_CREATE_DELETE";

    public int index;

    public int indexPrev = -1;

    public Toolbar toolbar;

    public LinearLayout layoutToolbar;

    public FloatingActionButton fab;

    public DrawerLayout drawer;

    public BottomNavigationView bottomNavigationView;

    public NavigationView navigationView;

    public TextView deleteCounter;
    public TextView counter;
    public ImageView chooseAll;
    public CheckBox check;

    private NotesListPresenter presenter;

    private ProgressBar progress;

    public ViewPager2 pager;

    private View itemView;
//    public ViewPager2 pager2;

    private CoordinatorLayout coordinatorLayout;

    private int previousClickedItemPosition = -1;

    private ArrayList<AdapterItem> adapterItems;

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
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu ( menu, v, menuInfo );
        Activity activity = requireActivity ();
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_folder:
            case R.id.delete_folder:
                Toast.makeText ( requireContext (), item.getTitle (), Toast.LENGTH_SHORT ).show ( );
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            notes = getArguments ( ).getParcelableArrayList ( ARG_NOTES );
            index = getArguments ( ).getInt ( ARG_INDEX );
            deleteNotes = getArguments ( ).getParcelableArrayList ( ADD_DELETE_KEY );
        }

        adapter = new ItemsAdapter ();

        setHasOptionsMenu ( true );

        setOnClickAdapter(adapter);

        getParentFragmentManager ()
                .setFragmentResultListener ( NoteDetailFragment.RESULT_KEY_DETAIL_FRAGMENT, this, (requestKey, result) -> {
                    note = result.getParcelable ( NoteDetailFragment.ARG_NEW_NOTE );
                    int iconGroup = result.getInt ( NoteDetailFragment.ARG_NEW_ICON );
                    String nameGroup = result.getString ( NoteDetailFragment.ARG_NEW_NAME_GROUP );

                    removeNoteDetailFragment();

                    if (iconGroup != 0 && nameGroup != null) {
                        newGroup = new Group ( -1, nameGroup, iconGroup, 0 );
                        group = presenter.addGroup ( newGroup );
                        note.setGroup_id ( group.getId () );
                    }

                    if (group_id != note.getGroup_id ()) {
                        group_id = note.getGroup_id ();
                        presenter.refreshNotes ( group_id );
                    }

                    if (note.getId () <= 0) {
                        presenter.addNote ( note );
                    } else {
                        presenter.upgradeNote ( note );
                    }

                    toolbar.setTitle ( getGroupName () );
                } );
    }

    private void removeNoteDetailFragment() {
        Fragment fmList = getParentFragmentManager ()
                .findFragmentByTag ( NoteDetailFragment.TAG );

        FragmentManager fm = getParentFragmentManager ();
        fm.popBackStack ();
        assert fmList != null;
        fm.beginTransaction ( )
                .remove ( fmList )
                .commit ( );
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
        drawer = view.findViewById ( R.id.drawer );
        toolbar = view.findViewById ( R.id.toolbar );
        fab = view.findViewById ( R.id.fab );
        progress = view.findViewById (R.id.progress_bar);
        pager = view.findViewById (R.id.fragment_container_detail);

        coordinatorLayout = view.findViewById ( R.id.fragment_container );
        layoutToolbar = view.findViewById ( R.id.layout_toolbar );
        deleteCounter = view.findViewById ( R.id.delete_counter );
        counter = view.findViewById ( R.id.counter );
        chooseAll = view.findViewById ( R.id.choose_all );

        bottomNavigationView = view.findViewById ( R.id.bottom_navigation );

        notesList = view.findViewById ( R.id.notes_list );
        notesList.setLayoutManager ( new LinearLayoutManager ( requireContext (), LinearLayoutManager.VERTICAL, false ) );
//        notesList.setLayoutManager ( new GridLayoutManager ( requireContext (), 2 ) );
        notesList.setAdapter ( adapter );

        DefaultItemAnimator animator = new DefaultItemAnimator ();
        animator.setAddDuration ( 500 );
        animator.setRemoveDuration ( 500 );
        notesList.setItemAnimator ( animator );

        adapterItems = new ArrayList<> ();

        if (savedInstanceState == null) {
            presenter = new NotesListPresenter ( requireContext ( ), new InMemoryRepository ( requireContext ( ) ), this );

            Activity activity = requireActivity ( );
            ((MainActivity) activity).getPresenter ( presenter );
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
                case R.id.delete_everything:
                    Activity activity = requireActivity ();
                    if (activity instanceof DialogClickListener) {
                        ((DialogClickListener) activity).getGroups ( );
                    }
                    MyDialogFragmentChoose fragment = MyDialogFragmentChoose.newInstance ( ((MainActivity)getActivity ()).getGroups () );
                    fragment.show ( getParentFragmentManager (), MyDialogFragmentChoose.TAG );
                    return true;
                case R.id.load_pap:
                    showGroup(((MainActivity)getActivity ()).getGroups ());
                    return true;
                case R.id.load_notes:
                    ((MainActivity)requireActivity ()).setGroupId ( 0 );
//                    showNotes ( ((MainActivity)requireActivity ()).getNotes () );
                    return true;
                default:
                    return false;
            }
        } );
    }

    @SuppressLint("NonConstantResourceId")
    private void createNavView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener ( item -> {
            switch (item.getItemId ( )) {
                case R.id.add_folder:
                    new MyDialogFragmentImageView ( ).show ( requireActivity ().getSupportFragmentManager ( ), MyDialogFragmentImageView.TAG );
                    return true;
                case R.id.delete_folder:
                    Toast.makeText ( requireContext (), "delete_folder", Toast.LENGTH_SHORT ).show ( );
                    return true;
                case R.id.search_nav_list:
                    Toast.makeText ( requireContext (), "search", Toast.LENGTH_SHORT ).show ( );
                    return true;
                case R.id.all_notes:

                    Bundle data = new Bundle ( );
                    data.putParcelable ( ARG_NOTE, null );

                    getParentFragmentManager ( )
                            .setFragmentResult ( SHOW_ALL_NOTES, data );

                    return true;
                default:
                    return false;
            }
        } );
    }

    private ArrayList<AdapterItem> castToType(List<Note> notes) {
        adapterItems.clear ();
        for (Note note: notes) {
            adapterItems.add ( new NoteItem ( note ) );
        }
        return adapterItems;
    }

    private ArrayList<AdapterItem> castToTypeGroup(List<Group> groups) {
        adapterItems.clear ();
        for (Group group: groups) {
            adapterItems.add ( new GroupItem ( group ) );
        }
        return adapterItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showGroup(List<Group> groups) {
        this.groups = groups;
        adapterItems = castToTypeGroup (groups);
        adapter.setItems ( adapterItems );
        adapter.notifyDataSetChanged ();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showNotes(List<Note> notes) {
        this.notes = notes;
        adapterItems = castToType(notes);

        adapter.setItems ( adapterItems );
        adapter.notifyDataSetChanged ();


        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {
            supplyToolbar ( toolbar );
            toolbar.getMenu ().getItem (  1  ).getIcon ().setTint ( getResources ().getColor ( R.color.purple_700, requireContext ().getTheme ()  ) );
            createNavView ( navigationView );
            toolbarItemClick(); // Проверить

            ((MainActivity) requireActivity ( )).fabEventHandling ( fab, group_id );

            toolbar.setTitle ( getGroupName () );

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
        } else {
            bottomNavigationView.setVisibility ( View.GONE );
        }
    }

    @Override
    public void upgradeNote(Note note) {
        int position = adapter.upgradeItem(new NoteItem (note));
        adapter.notifyItemChanged(position);
//        notesList.scrollToPosition ( position );
    }

    @Override
    public void deleteNotes(List<Note> deleteNotes) {
        for (int i = 0; i < deleteNotes.size (); i++) {
            int position = adapter.removeItem(new NoteItem (deleteNotes.get ( i )));
            adapter.notifyItemRemoved ( position );
//            notesList.scrollToPosition ( position );
        }
    }

    @Override
    public void addNote(Note note) {
        int position = 0;
        adapter.addItems ( position, new NoteItem ( note ) );
        adapter.notifyItemInserted ( position );
        notesList.scrollToPosition ( position );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setOnClickAdapter(ItemsAdapter adapter) {
        adapter.setOnClickItem ( (view, note, position) -> {
            if(previousClickedItemPosition != position) {
                previousClickedItemPosition = position;
                adapter.notifyDataSetChanged ();

                Fragment fmList = getParentFragmentManager ( )
                        .findFragmentByTag ( NotesListFragment.TAG );

                FragmentManager fmDetail = getParentFragmentManager ( );

                assert fmList != null;
                fmDetail.beginTransaction ( )
                        .hide ( fmList )
                        .addToBackStack ( "" )
                        .add ( R.id.fragment_container, NoteDetailFragment.newInstance ( note.getNote () ), NoteDetailFragment.TAG )
                        .commit ( );
            }
        } );

        adapter.setOnLongClickItem ( (view, note, position, checkBox) -> {
            if(previousClickedItemPosition != position) {
                previousClickedItemPosition = position;
                adapter.notifyDataSetChanged ( );
            }
            isCheckedNotes(note, checkBox);
            showNotes ( notes );
        } );

        adapter.setOnClickItemGroup ( (view, groupItem, position) -> Toast.makeText ( NotesListFragment.this.requireContext ( ), groupItem.getGroup ( ).getName ( ), Toast.LENGTH_SHORT ).show ( ) );
    }

    private void isCheckedNotes(NoteItem note, CheckBox checkBox) {
        if (checkBox.isChecked ( )) {
            checkBox.setVisibility ( View.GONE );
            checkBox.setChecked ( false );
            for (int i = 0; i < deleteNotes.size ( ); i++) {
                if (note.getNote ( ).getId ( ) == deleteNotes.get ( i ).getId ( )) {
                    deleteNotes.remove ( note.getNote ( ) );
                }
            }
        } else {
            deleteNotes.add ( note.getNote ( ) );
            checkBox.setVisibility ( View.VISIBLE );
            checkBox.setChecked ( true );
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void createBottomNavigation() {
        MenuItem menuItem = bottomNavigationView.getMenu ( ).getItem ( 0 );
        menuItem.setCheckable ( false );

        bottomNavigationView.setOnItemSelectedListener ( item -> {
            item.setCheckable ( false );
            switch (item.getItemId ( )) {
                case R.id.save:

                case R.id.move:
                    Toast.makeText ( requireContext ( ), "Здесь что-то будет", Toast.LENGTH_SHORT ).show ( );
                    return true;
                case R.id.delete_selected_notes:
                    presenter.deleteIndex ( deleteNotes );
                    deleteNotes.clear ( );
                    fab.setVisibility ( View.VISIBLE );
                    bottomNavigationView.setVisibility ( View.GONE );
                    layoutToolbar.setVisibility ( View.GONE );


//                    notes = presenter.refreshNotes ( group_id );
//                    index = 0;
//                    FragmentManager fm = getSupportFragmentManager ( );
//                    fm.beginTransaction ( )
//                            .replace ( R.id.fragment_container, NotesListFragment.newInstance ( notes, index, deleteNotes ), NotesListFragment.TAG )
//                            .commit ( );
                    return true;
            }
            return false;
        } );
    }

    @SuppressLint("SetTextI18n")
    private void showLayoutToolbar() {

        deleteCounter.setOnClickListener ( view -> {
            deleteNotes.clear ( );
            showNotes ( notes );
        } );

        if (deleteNotes.size ( ) > 0) {
            counter.setText ( "Выбрано: " + deleteNotes.size ( ) );
        }

        chooseAll.setOnClickListener ( view -> {
            if (deleteNotes.size ( ) == notes.size ( )) {
                deleteNotes.clear ( );
            } else {
                deleteNotes.clear ( );
                deleteNotes.addAll ( notes );
            }
            showNotes ( notes );
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
        groups = presenter.refreshGroup ( );
        for (int i = 0; i < groups.size (); i++){
            final int position = i;
            menu.add ( R.id.group, i + 1, Menu.NONE, groups.get ( i ).getName () )
                    .setCheckable ( true )
                    .setIcon ( groups.get ( position ).getIcon ()  )
                    .setActionView ( R.layout.counter_notes );
            TextView textNameGroup = menu.getItem ( i + 1 ).getActionView ().findViewById ( R.id.value_counter_notes );
            textNameGroup.setText (  String.valueOf ( groups.get ( position ).getCount () ) );
            menu.getItem ( i + 1 ).setOnMenuItemClickListener ( menuItem -> {
                group_id = groups.get ( position ).getId ();
//                toolbar.setTitle ( getGroupName () );
                presenter.refreshNotes ( group_id );
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } );
        }
    }

    public void showFillMenu() {
        Menu menu = navigationView.getMenu ();
        menu.removeGroup ( R.id.group );
        fillMenu ( menu );
    }


    @Override
    public void showProgress() {
        progress.setVisibility ( View.VISIBLE );
    }

    @Override
    public void hideProgress() {
        progress.setVisibility ( View.GONE );
    }

    public String getGroupName() {
        groups = presenter.refreshGroup ( );
        if (groups.size () == 0) {
            group_id = -1;
        }
        String groupName = null;
        if (group_id > 0) {
            for (Group group : groups) {
                if (group.getId ( ) == group_id) {
                    groupName = group.getName ( );
                }
            }
        } else if (group_id == 0) {
            groupName = "Все заметки";

        } else {
            groupName = getResources ( ).getString ( R.string.you_have_no_notes );
        }
        return groupName;
    }

}