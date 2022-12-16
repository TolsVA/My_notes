package com.example.my_notes.ui.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.my_notes.MainActivity;
import com.example.my_notes.R;
import com.example.my_notes.domain.Group;
import com.example.my_notes.domain.Note;
import com.example.my_notes.ui.dialog.MyDialogFragment;
import com.example.my_notes.ui.dialog.MyBottomDialogFragmentGroup;
import com.example.my_notes.ui.dialog.MyDialogFragmentImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class NoteDetailFragment extends Fragment {

    public static final String TAG = "NoteDetailFragment";

    public static final String ARG_NOTE = "ARG_NOTE";
    public static final String ARG_GROUP = "ARG_GROUP";
    public static final String ARG_NEW_GROUP = "ARG_NEW_GROUP";

    public static final String ARG_NEW_NOTE = "ARG_NEW_NOTE";

    public static final String RESULT_KEY_DETAIL_FRAGMENT = "NoteDetailFragment_RESULT_KEY_DETAIL_FRAGMENT";
    public static final String RESULT_KEY_GROUP_FRAGMENT = "NoteDetailFragment_RESULT_KEY_GROUP_FRAGMENT";
    private Note note;

    private EditText titleView, textView;

    private TextView dataView;

    private Button btnSave;

    public Group group;

    public Toolbar toolbar;

    public LinearLayout layoutToolbar;

    public BottomNavigationView bottomNavigationView;

    private long groupId;

    public ListView listView;
    public List<String> list;
    ArrayAdapter<String> adapter;


    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment ( );
        Bundle args = new Bundle ( );
        args.putParcelable ( ARG_NOTE, note );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments ( ) != null) {
            note = getArguments ( ).getParcelable ( ARG_NOTE );
        }
//        setHasOptionsMenu ( true );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate ( R.layout.note_detail_toolbar, container, false );
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        toolbar = view.findViewById ( R.id.toolbar_note_detail );
        bottomNavigationView = view.findViewById ( R.id.bottom_navigation_detail );

        if (getResources ( ).getConfiguration ( ).orientation != Configuration.ORIENTATION_LANDSCAPE) {

            bottomNavigationView.setVisibility ( View.VISIBLE );
            createBottomNavigation ( );

            toolbar.setVisibility ( View.VISIBLE );
            toolbar.setTitle ( ((MainActivity) requireActivity ( )).getGroupName ( ) );
            toolbarItemClick ( );
        } else {
            bottomNavigationView.setVisibility ( View.GONE );
            toolbar.setVisibility ( View.GONE );
        }

        titleView = view.findViewById ( R.id.title_detail );

        textView = view.findViewById ( R.id.text_detail );

        dataView = view.findViewById ( R.id.data_detail );

        displayDetails ( note );

    }

    private void displayDetails(Note note) {

        titleView.setText ( note.getTitle ( ) );
        textView.setText ( note.getText ( ) );
        dataView.setText ( note.getData ( ) );

//        SimpleDateFormat formatDate = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            formatDate = new SimpleDateFormat("E '('dd MMMM yyyy')' '-->' hh:mm", Locale.getDefault());
//        }
//
//        assert formatDate != null;
//        StringBuilder sb = new StringBuilder(formatDate.format( Date.parse(note.getData())).toLowerCase());
//        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
//
//        dataView.setText(sb.toString());
        dataView.setText ( note.getData ( ) );

        dataView.setOnClickListener ( view -> {
            MyDialogFragment fragment = MyDialogFragment.newInstance ( dataView.getId ( ) );
            fragment.show ( getChildFragmentManager ( ), MyDialogFragment.TAG );
        } );


//        btnSave.setOnClickListener( view -> {
//            note.setTitle(titleView.getText().toString());
//            note.setText(textView.getText().toString());
//            note.setData (dataView.getText().toString());
//
//            Activity activity = requireActivity();
//            PopupMenu popupMenu = new PopupMenu(activity, view);
//            activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//
//            popupMenu.setOnMenuItemClickListener ( new PopupMenu.OnMenuItemClickListener ( ) {
//                @SuppressLint("NonConstantResourceId")
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId ( )) {
//                        case R.id.save_to_default_older:
//                            Bundle data = new Bundle();
//                            data.putParcelable(ARG_NEW_NOTE, note);
//                            getParentFragmentManager()
//                                    .setFragmentResult(RESULT_KEY_DETAIL_FRAGMENT, data);
//                            return true;
//                        case R.id.save_to_new_folder:
//                            Toast.makeText ( activity, item.getTitle (), Toast.LENGTH_SHORT ).show ( );
//                            return true;
//                        case R.id.save_to_folder_of_choice:
////                            Bundle data1 = new Bundle();
////                            data1.putParcelable(ARG_NEW_GROUP, note);
////                            getParentFragmentManager()
////                                    .setFragmentResult(RESULT_KEY_GROUP_FRAGMENT, data1);
//                            List<Group> groups = ((MainActivity) getActivity ( )).getGroups();
//                            MyDialogFragmentGroup fragment = MyDialogFragmentGroup.newInstance ( groups );
//                            fragment.show(getChildFragmentManager (), MyDialogFragmentGroup.TAG);
//                            return true;
//                        default:
//                            return false;
//                    }
//                }
//            } );
//            popupMenu.show();
//        } );
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity ().getMenuInflater().inflate(R.menu.menu_context_detail, menu);
////        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        menu.setHeaderTitle("У вас нет не одной папки для хранения заметок");
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        //find out which menu item was pressed
//        switch (item.getItemId()) {
//            case R.id.save_to_default_older:
//                if (toolbar.getTitle ())
////                countries.remove(info.position);
////                adapter.notifyDataSetChanged();
//                return true;
//            case R.id.create_new_folder:
////                String listItemName = countries.get(info.position);
////                openBrowser("<a class="vglnk" href="https://en.wikipedia.org/wiki/" rel="nofollow"><span>https</span><span>://</span><span>en</span><span>.</span><span>wikipedia</span><span>.</span><span>org</span><span>/</span><span>wiki</span><span>/</span></a>" + listItemName);
//
//                return true;
//            default:
//                return false;
//        }
//    }

    @SuppressLint("NonConstantResourceId")
    private void toolbarItemClick() {
        toolbar.setOnMenuItemClickListener ( item -> {
            switch (item.getItemId ( )) {
                case R.id.delete_note:
                    Toast.makeText ( requireContext ( ), "Удалить заметку", Toast.LENGTH_SHORT ).show ( );
                    return true;
                default:
                    return false;
            }
        } );
    }

    private void createBottomNavigation() {
        MenuItem menuItem = bottomNavigationView.getMenu ( ).getItem ( 0 );
        menuItem.setCheckable ( false );

        bottomNavigationView.setOnItemSelectedListener ( new NavigationBarView.OnItemSelectedListener ( ) {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                note.setTitle ( titleView.getText ( ).toString ( ) );
                note.setText ( textView.getText ( ).toString ( ) );
                note.setData ( dataView.getText ( ).toString ( ) );
                item.setCheckable ( false );
                switch (item.getItemId ( )) {
                    case R.id.save:
                        String s = getResources ( ).getString ( R.string.you_have_no_notes );
                        if (toolbar.getTitle ( ).equals ( s )) {

                            Activity activity = requireActivity ( );
                            PopupMenu popupMenu = new PopupMenu ( activity, textView );
                            activity.getMenuInflater ( ).inflate ( R.menu.popup_menu_detail, popupMenu.getMenu ( ) );

                            popupMenu.setOnMenuItemClickListener ( new PopupMenu.OnMenuItemClickListener ( ) {
                                @SuppressLint("NonConstantResourceId")
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId ( )) {
                                        case R.id.save_to_default_older:
                                            int iconResourceId = R.drawable.ic_baseline_folder_24;
                                            String name = getString ( R.string.uncategorized );

                                            groupId = ((MainActivity) requireActivity ( )).createNewGroup ( iconResourceId, name );

//                                            toolbar.setTitle ( name );
//                                            groupId = ((MainActivity) requireActivity ( )).getGroupId ( );
                                            note.setGroup_id ( groupId );

//                                            Bundle data = new Bundle ( );
//                                            data.putParcelable ( ARG_NEW_NOTE, note );
//                                            getParentFragmentManager ( )
//                                                    .setFragmentResult ( RESULT_KEY_DETAIL_FRAGMENT, data );
                                            ((MainActivity) requireActivity ( )).showNotesListFragment ( note );
                                            return true;
                                        case R.id.create_new_folder:
                                            MyDialogFragmentImageView fragment = MyDialogFragmentImageView.newInstance ( note );
                                            fragment.show ( getParentFragmentManager (), MyDialogFragmentImageView.TAG );
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            } );
                            popupMenu.show ( );
                        } else {
                            groupId = ((MainActivity) requireActivity ( )).getGroupId ( );
                            note.setGroup_id ( groupId );

//                            Bundle data = new Bundle ( );
//                            data.putParcelable ( ARG_NEW_NOTE, note );
//                            getParentFragmentManager ( )
//                                    .setFragmentResult ( RESULT_KEY_DETAIL_FRAGMENT, data );
                            ((MainActivity) requireActivity ( )).showNotesListFragment ( note );
                        }
                        return true;
                    case R.id.save_as:
                        List<Group> groups = ((MainActivity) getActivity ( )).getGroups ( );
                        MyBottomDialogFragmentGroup fragment = MyBottomDialogFragmentGroup.newInstance ( groups, note );
                        fragment.show ( getParentFragmentManager (), MyBottomDialogFragmentGroup.TAG );

                        return true;
                    case R.id.save_new_folder:
                        MyDialogFragmentImageView f = MyDialogFragmentImageView.newInstance ( note );
                        f.show ( getParentFragmentManager (), MyDialogFragmentImageView.TAG );
                        return true;
                }
                return false;
            }
        } );
    }
}