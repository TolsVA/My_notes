package com.example.my_notes.domain;

import java.util.List;

public interface NotesRepository {

    List<Note> getAllNotes();

    List<Group> getAllGroup();

    void upgradeNote(Note note);

    Note addNote(Note note);

    void clearDb();

    void deleteIndex(long index);

    void closeDb();

    List<Note> searchDb(String text);

    Group addGroup(Group group);

    int checkGroupForDbGroup(String text);

    Group searchByGroupName(String folderName);

}
