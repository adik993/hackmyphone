package com.adrian.hackmyphone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrian.hackmyphone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adrian on 2016-04-13.
 */
public class BaseListProgressFragment extends BaseListFragment {

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_list_progress;
    }
}
