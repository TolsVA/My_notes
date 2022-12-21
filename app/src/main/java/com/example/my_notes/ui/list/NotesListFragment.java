package com.example.my_notes.ui.list;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.adapterItem.AdapterItem;
import com.example.my_notes.ui.adapterItem.GroupItem;
import com.example.my_notes.ui.adapterItem.NoteItem;
import com.example.my_notes.ui.adapterItem.ItemsAdapter;
import com.example.my_notes.ui.adapterItem.OnClickItem;
import com.example.my_notes.ui.adapterItem.OnLongClickItem;
import com.example.my_notes.ui.dialog.DialogClickListener;
import com.example.my_notes.ui.dialog.MyDialogFragmentChoose;
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
    public static final String SHOW_ALL_NOTES = "SHOW_ALL_NOTES";

    public RecyclerView notesList;

    private ItemsAdapter adapter;

    public List<Note> notes, deleteNotes;

    public List<Group> groups;

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

    public ViewPager2 pager2;

    private int previousClickedItemPosition = -1;

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
        MenuInflater inflater = getActivity ().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add_folder:
                Toast.makeText ( requireContext (), item.getTitle (), Toast.LENGTH_SHORT ).show ( );
//                editNote(info.id);
                return true;
            case R.id.delete_folder:
//                deleteNote(info.id);
                int i = 0;
                Toast.makeText ( requireContext (), item.getTitle (), Toast.LENGTH_SHORT ).show ( );
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            notes = getArguments ( ).getParcelableArrayList ( ARG_NOTES );
            index = getArguments ( ).getInt ( ARG_INDEX );
            deleteNotes = getArguments ( ).getParcelableArrayList ( ADD_DELETE_KEY );
        }
        adapter = new ItemsAdapter ();

        adapter.setOnClickItem ( new OnClickItem ( ) {
            @Override
            public void onClickItem(View view, NoteItem note, int position) {
                if(previousClickedItemPosition != position) {
                    previousClickedItemPosition = position;
                    adapter.notifyDataSetChanged ();

                    Bundle data = new Bundle ( );
                    data.putParcelable ( ARG_NOTE, note.getNote () );

                    getParentFragmentManager ( )
                            .setFragmentResult ( RESULT_KEY, data );
                }
            }
        } );

        adapter.setOnLongClickItem ( new OnLongClickItem ( ) {
            @Override
            public void onLongClickItem(View view, NoteItem note, int position, CheckBox checkBox) {
                if (checkBox.isChecked ( )) {
                    checkBox.setVisibility ( View.GONE );
                    checkBox.setChecked ( false );
                    for (int i = 0; i < deleteNotes.size ( ); i++) {
                            if (note.getNote ().getIndex () == deleteNotes.get ( i ).getIndex ( )) {
                                deleteNotes.remove ( note );
                            }
                        }

                } else {
                    deleteNotes.add ( note.getNote () );
                    checkBox.setVisibility ( View.VISIBLE );
                    checkBox.setChecked ( true );

                }

                showNotes ( notes );
            }
        } );

        setHasOptionsMenu ( true );
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

        layoutToolbar = view.findViewById ( R.id.layout_toolbar );
        deleteCounter = view.findViewById ( R.id.delete_counter );
        counter = view.findViewById ( R.id.counter );
        chooseAll = view.findViewById ( R.id.choose_all );

        bottomNavigationView = view.findViewById ( R.id.bottom_navigation );//popup


        notesList = view.findViewById ( R.id.notes_list );
        notesList.setLayoutManager ( new LinearLayoutManager ( requireContext (), LinearLayoutManager.VERTICAL, false ) );
//        notesList.setLayoutManager ( new GridLayoutManager ( requireContext (), 2 ) );
//        notesList.setLayoutManager ( new StaggeredGridLayoutManager ( 8,0 ) );

        notesList.setAdapter ( adapter );

        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {
            supplyToolbar ( toolbar );
            toolbar.getMenu ().getItem (  1  ).getIcon ().setTint ( getResources ().getColor ( R.color.purple_700, requireContext ().getTheme ()  ) );
            createNavView ( navigationView );
            toolbarItemClick(); // Проверить

//            pager2 = view.findViewById ( R.id.pager_group );
//            groups = ((MainActivity) requireActivity ( )).getGroups ();
//            pager2.setAdapter ( new MyAdapterGroup ( requireActivity (), groups ) );
//            pager2.setPageTransformer ( new ZoomOutPageTransformer ( ) );
//
//            long groupId = ((MainActivity) requireActivity ( )).getGroupId();
//            pager2.setCurrentItem ( (int) groupId, false );

//            savePosition ( );

            ((MainActivity) requireActivity ( )).fabEventHandling ( fab, toolbar.getTitle () );
        }

        showNotes ( notes );

//        NestedScrollView scrollView = view.findViewById ( R.id.scroll_list );
//
//        scrollView.requestChildFocus ( notesContainer, notesContainer.getChildAt ( index ) );

//        if (notes.size ( ) != 0 && index >= 0) {
//            notesContainer.getChildAt ( index ).setBackground ( getResources ( ).getDrawable ( R.drawable.layout_bg_2, requireContext ( ).getTheme ( ) ) );
//        }
    }

//    private void savePosition() {
//        this.pager2.registerOnPageChangeCallback ( new ViewPager2.OnPageChangeCallback ( ) {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled ( position, positionOffset, positionOffsetPixels );
//            }
//
//            @SuppressLint("UseCompatLoadingForDrawables")
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected ( position );
//                groups = ((MainActivity) requireActivity ( )).getGroups ();
//                long group_id = groups.get ( position ).getId ();
//                ((MainActivity) requireActivity ( )).setGroupId(group_id);
//                notes = ((MainActivity) requireActivity ( )).getNotes();
//                showNotes ( notes );
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged ( state );
//            }
//        } );
//    }

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
                    MyDialogFragmentChoose fragment = MyDialogFragmentChoose.newInstance ( ((MainActivity)requireActivity ()).getGroups () );
                    fragment.show ( getParentFragmentManager (), MyDialogFragmentChoose.TAG );
                    return true;
                case R.id.load_pap:
                    showGroup(((MainActivity)requireActivity ()).getGroups ());
                    return true;
                case R.id.load_notes:
                    ((MainActivity)requireActivity ()).setGroupId ( 0 );
                    showNotes ( ((MainActivity)requireActivity ()).getNotes () );
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
                    case R.id.all_notes:
                        getParentFragmentManager ( )
                                .setFragmentResult ( SHOW_ALL_NOTES, null );
                        return true;
                    default:
                        return false;
                }
            }
        } );
    }

    private ArrayList<AdapterItem> castToType(List<Note> notes) {
        ArrayList<AdapterItem> adapterItems = new ArrayList<> ();
        for (Note note: notes) {
            adapterItems.add ( new NoteItem ( note ) );
        }
        return adapterItems;
    }

    private ArrayList<AdapterItem> castToTypeGroup(List<Group> groups) {
        ArrayList<AdapterItem> adapterItems = new ArrayList<> ();
        for (Group group: groups) {
//            adapterItems.add ( new GroupAdapterItem ( group, group.getName (), group.getIcon () ) );
            adapterItems.add ( new GroupItem ( group ) );
        }
        return adapterItems;
    }

    private void showGroup(List<Group> groups) {
        this.groups = groups;
        ArrayList<AdapterItem> adapterItems = castToTypeGroup (groups);
        adapter.setItems ( adapterItems );
        adapter.notifyDataSetChanged ();
    }

    public void showNotes(List<Note> notes) {
//        notesContainer.removeAllViews ( );
        ArrayList<AdapterItem> adapterItems = castToType(notes);
        this.notes = notes;

        adapter.setItems ( adapterItems );
        adapter.notifyDataSetChanged ();

        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {

            String groupName = ((MainActivity) getActivity ( )).getGroupName ();
            setTitleToolbar(groupName);

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


//        for (Note note : notes) {
//            View itemView = LayoutInflater.from ( requireContext ( ) ).inflate ( R.layout.item_note, notesContainer, false );
//
////            registerForContextMenu ( itemView );//context_menu
//
//            TextView title = itemView.findViewById ( R.id.note_title );
//            title.setText ( note.getTitle ( ) );
//
//            TextView text = itemView.findViewById ( R.id.note_text );
//            text.setText ( note.getText ( ) );
//
//            TextView data = itemView.findViewById ( R.id.note_date );
//
//            CheckBox checkBox = itemView.findViewById ( R.id.delete_index );
//
//            for (int i = 0; i < deleteNotes.size ( ); i++) {
//                if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
//                    checkBox.setChecked ( true );
//                }
//            }
//
///*            SimpleDateFormat formatDate = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                formatDate = new SimpleDateFormat("день недели:  EEEE\n дата________:   dd MMMM yyyy" +
////                        "\nВремя______:   hh:mm", Locale.getDefault());
//                formatDate = new SimpleDateFormat ( "EEEE  dd MMMM yyyy   hh:mm", Locale.getDefault ( ) );
//            }
//
//            assert formatDate != null;
//            StringBuilder sb = new StringBuilder(formatDate.format( Date.parse ( note.getData() )).toLowerCase());
//            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
//            String data1 = sb.toString();
//
//            data.setText(data1);*/
//
//            data.setText ( note.getData ( ) );
//
//            notesContainer.addView ( itemView );
//
//            if (deleteNotes.size ( ) > 0) {
//                checkBox.setVisibility ( View.VISIBLE );
//                itemView.setOnClickListener ( view -> {
//                    if (checkBox.isChecked ( )) {
//                        checkBox.setChecked ( false );
//                        for (int i = 0; i < deleteNotes.size ( ); i++) {
//                            if (note.getIndex ( ) == deleteNotes.get ( i ).getIndex ( )) {
//                                deleteNotes.remove ( note );
//                            }
//                        }
//                    } else {
//                        checkBox.setChecked ( true );
//                        deleteNotes.add ( note );
//                    }
//
//                    checkBox.setOnClickListener ( new View.OnClickListener ( ) {
//                        @Override
//                        public void onClick(View view) {
//                            Bundle data1 = new Bundle ( );
//                            data1.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );
//
//                            getParentFragmentManager ( )
//                                    .setFragmentResult ( CREATE_DELETE_KEY, data1 );
//                        }
//                    } );
//
//                    Bundle data1 = new Bundle ( );
//                    data1.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );
//
//                    getParentFragmentManager ( )
//                            .setFragmentResult ( CREATE_DELETE_KEY, data1 );
//                } );
//            } else {
//                itemView.setOnClickListener ( view -> {
//                    Bundle data12 = new Bundle ( );
//                    data12.putParcelable ( ARG_NOTE, note );
//
//                    getParentFragmentManager ( )
//                            .setFragmentResult ( RESULT_KEY, data12 );
//                } );
//
//                itemView.setOnLongClickListener ( view -> {
//                    checkBox.setVisibility ( View.VISIBLE );
//                    checkBox.setChecked ( true );
//                    deleteNotes.add ( note );
//
//                    Bundle data13 = new Bundle ( );
//                    data13.putParcelableArrayList ( ARG_NOTES, (ArrayList<? extends Parcelable>) deleteNotes );
//
//                    getParentFragmentManager ( )
//                            .setFragmentResult ( CREATE_DELETE_KEY, data13 );
//                    return false;
//                } );
//            }
//        }
    }

    public void setTitleToolbar(String groupName) {
        toolbar.setTitle ( groupName );
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