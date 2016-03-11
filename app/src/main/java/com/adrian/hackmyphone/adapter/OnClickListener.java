package com.adrian.hackmyphone.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by student215 on 2016-03-07.
 */
public interface OnClickListener {
    void onItemClick(int i, RecyclerView.ViewHolder view);
    boolean onItemLongClick(int i, RecyclerView.ViewHolder view);
}
