package com.adrian.hackmyphone.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by adrian on 3/11/16.
 */
public class SimpleTextViewHolder extends RecyclerView.ViewHolder {
    @Bind(android.R.id.text1)
    public TextView text;


    public SimpleTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
