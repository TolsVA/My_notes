package com.example.my_notes.domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        data.put( KEY_ID, note.getId () );
        data.put( KEY_TITLE, note.getTitle () );
        data.put( KEY_TEXT, note.getText () );
        data.put( KEY_DATA, String.valueOf ( createdAt ) );
        data.put( KEY_GROUP_IG, note.getGroup_id () );

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

    @Override
    public void getAll(Callback<List<Note>> callback) {
        db.collection(NOTES)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        ArrayList<Note> result = new ArrayList<>();

                        for (DocumentSnapshot snapshot: documents) {
//                            String id = snapshot.getId();
                            long id = snapshot.getLong ( KEY_ID );
                            String title = snapshot.getString(KEY_TITLE);
                            String text = snapshot.getString(KEY_TEXT);
                            String date = snapshot.getString (KEY_DATA);
                            long group_id = snapshot.getLong ( KEY_GROUP_IG );

                            result.add(new Note(id, title, text, date, group_id));
                        }

                        callback.onSuccess(result);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }
}
