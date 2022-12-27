package com.example.my_notes.domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestormNotesRepository implements FirestormRepository {

    public static final FirestormRepository INSTANCE = new FirestormNotesRepository ();

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATA = "data";
    private static final String KEY_GROUP_IG = "group_id";

    private static final String NOTES = "notes";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void addNote(Note note, Callback<Note> callback) {
        Map<String, Object> data = new HashMap<> ();

        Date createdAt = new Date();

        note.setId ( 1 ); // Заглушка

        data.put( KEY_ID, note.getId () );
        data.put( KEY_TITLE, note.getTitle () );
        data.put( KEY_TEXT, note.getText () );
        data.put( KEY_DATA, String.valueOf ( createdAt ) );
        data.put( KEY_GROUP_IG, String.valueOf ( note.getGroup_id () ) );

        db.collection(NOTES)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference> () {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        String id = documentReference.getId();

//                        note.setId ( Long.parseLong (id) );

                        callback.onSuccess ( note );
                    }
                })
                .addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        callback.onError(e);
                    }
                });
    }
}
