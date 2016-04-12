package com.adrian.hackmyphone.items;

/**
 * Created by adrian on 4/12/16.
 */
public class InfoItem implements Titled {
    private String mText;

    public InfoItem(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public String getTitle() {
        return getText();
    }
}
