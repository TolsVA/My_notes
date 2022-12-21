package com.example.my_notes.domain;

import java.util.List;

public interface NotesRepository {

    List<Note> getAllNotes(long group_id);

    List<Group> getAllGroup();

    void upgradeNote(Note note);

    void addNote(Note note);

    void clearDb();

    void deleteIndex(long index);

    void deleteIndexGroup(long index);

    void closeDb();

    List<Note> searchDb(String text);

    void addGroup(Group group);

    int checkGroupForDbGroup(String text);

    List<Group> searchByGroupName(String folderName);

    void deleteIndexNoteGroupId(long position);
}
