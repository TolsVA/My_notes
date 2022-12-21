package com.example.my_notes.ui.adapterItem;

import com.example.my_notes.domain.Group;

public class GroupItem implements AdapterItem {

    private final Group group;

    public GroupItem(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }
}
