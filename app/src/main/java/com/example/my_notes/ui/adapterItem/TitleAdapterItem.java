package com.example.my_notes.ui.adapterItem;

public class TitleAdapterItem implements AdapterItem {

    private final String title;

    public TitleAdapterItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
