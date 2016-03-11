package com.adrian.hackmyphone.items;

/**
 * Created by adrian on 3/11/16.
 */
public class Section implements Titled {
    private String mTitle;

    public Section(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
