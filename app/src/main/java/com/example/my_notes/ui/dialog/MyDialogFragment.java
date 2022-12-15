package com.example.my_notes.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.my_notes.domain.Note;

import java.util.Calendar;
import java.util.List;

public class MyDialogFragment extends DialogFragment {

    public static final String TAG = "MyDialogFragment";

    public static final String ARG_TEXT_VIEW = "ARG_TEXT_VIEW";

    public static int id;


    public static MyDialogFragment newInstance(int id) {
        MyDialogFragment fragment = new MyDialogFragment ( );
        Bundle args = new Bundle ( );
        args.putInt ( ARG_TEXT_VIEW, id );
        fragment.setArguments ( args );
        return fragment;
    }

    public interface ClickDatePickerDialog {
        void applySettings(String text, int id);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments () != null) {
            id = getArguments ( ).getInt ( ARG_TEXT_VIEW ) ;
        }
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireActivity (),
                new DatePickerDialog.OnDateSetListener ( ) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String textDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        ((ClickDatePickerDialog) MyDialogFragment.this.requireActivity ( )).applySettings ( textDateParam, id );
                    }
                }, mYear, mMonth, mDay);
    }
}

