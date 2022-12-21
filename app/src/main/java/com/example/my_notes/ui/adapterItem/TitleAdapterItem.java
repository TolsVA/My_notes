package com.example.my_notes.ui.adapterItem;

import com.example.my_notes.ui.adapterItem.AdapterItem;

public class TitleAdapterItem implements AdapterItem {
    private String title;

    public TitleAdapterItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
