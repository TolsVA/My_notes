package com.example.my_notes.domain;

import java.util.List;

public interface NotesRepository {

    void getAllNotes(long group_id, Callback<List<Note>> callback);

    List<Group> getAllGroup();

    void upgradeNote(Note note);

    void addNote(Note note);

    void clearDb();

    void deleteIndex( long index );

    void deleteIndexGroup(long index);

    void closeDb();

    List<Note> searchDb(String text);

    Group addGroup(Group group);

    int checkGroupForDbGroup(String text);

    List<Group> searchByGroupName(String folderName);

    void deleteIndexNoteGroupId(long position);
}
