package com.adrian.hackmyphone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.MyAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adrian on 2016-04-13.
 */
public abstract class BaseListFragment extends Fragment {

    @Bind(R.id.list)
    protected RecyclerView mList;
    protected MyAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mLayoutManager);
        mAdapter=new MyAdapter(new ArrayList<>());
        mList.setAdapter(mAdapter);
        return view;
    }

    public int getLayoutResId() {
        return R.layout.fragment_list;
    }
}
