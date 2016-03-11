package com.adrian.hackmyphone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by adrian on 3/10/16.
 */
public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {
    VH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent);
    void onBindViewHolder(T elem, VH holder, int position);
}
