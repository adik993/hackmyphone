package com.adrian.hackmyphone.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.adrian.hackmyphone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by adrian on 4/12/16.
 */
public class ProgressItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    public ProgressItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
