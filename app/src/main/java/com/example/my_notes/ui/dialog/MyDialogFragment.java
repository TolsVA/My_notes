package com.example.my_notes.ui.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

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
                (view, year, monthOfYear, dayOfMonth) -> {
                    String textDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                    Activity activity = requireActivity ( );
                    if (activity instanceof DialogClickListener) {
                        ((DialogClickListener) activity).applySettings (  textDateParam, id  );
                    }
                }, mYear, mMonth, mDay);
    }
}

