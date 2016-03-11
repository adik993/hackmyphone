package com.adrian.hackmyphone.databinders;

import com.adrian.hackmyphone.R;

/**
 * Created by adrian on 3/11/16.
 */
public class SimpleCardDataBinder extends SimpleTextDataBinder {

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_card_item;
    }
}
