package com.adrian.hackmyphone.databinders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.DataBinder;
import com.adrian.hackmyphone.items.ProgressItem;
import com.adrian.hackmyphone.viewholders.ProgressItemViewHolder;

/**
 * Created by adrian on 4/12/16.
 */
public class ProgressItemDataBinder implements DataBinder<ProgressItem, ProgressItemViewHolder> {
    @Override
    public ProgressItemViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.progress_item, parent, false);
        return new ProgressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgressItem elem, ProgressItemViewHolder holder, int position) {
        
    }
}
