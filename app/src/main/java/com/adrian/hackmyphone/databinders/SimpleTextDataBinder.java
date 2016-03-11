package com.adrian.hackmyphone.databinders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.adapter.DataBinder;
import com.adrian.hackmyphone.items.Titled;
import com.adrian.hackmyphone.viewholders.SimpleTextViewHolder;

/**
 * Created by adrian on 3/11/16.
 */
public class SimpleTextDataBinder implements DataBinder<Titled, SimpleTextViewHolder> {
    @Override
    public SimpleTextViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new SimpleTextViewHolder(view);
    }

    protected int getLayoutResId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindViewHolder(Titled elem, SimpleTextViewHolder holder, int position) {
        holder.text.setText(elem.getTitle());
    }
}
