package com.adrian.hackmyphone.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.items.ExpandableItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by student215 on 2016-03-07.
 */
public class ExpandableViewHolder extends RecyclerView.ViewHolder {

    @Bind(android.R.id.text1)
    public TextView text;
    @Bind(R.id.expandable)
    public ExpandableItem expandable;
    @Bind(R.id.values)
    public TextView values;

    public ExpandableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
