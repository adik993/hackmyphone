package com.adrian.hackmyphone.fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.adapter.MyAdapter;
import com.adrian.hackmyphone.items.Section;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactsFragment extends Fragment {

    private static final int REQ_READ_CONTACTS = 1;
    @Bind(R.id.list)
    RecyclerView mList;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    MyAdapter mAdapter;
    Subscription mSubscription;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new MyAdapter(new ArrayList<>());
        mList.setAdapter(mAdapter);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQ_READ_CONTACTS);
        } else populateList();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_READ_CONTACTS: {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    populateList();
                } else {
                    Snackbar.make(getView(), R.string.allow_read_contacts_to_work, Snackbar.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    private void populateList() {
        mSubscription = Observable.defer(() -> Observable.from(retrieveContacts()))
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Section::new)
                .toList()
                .subscribe(sections -> {
                    mAdapter.elems.addAll(sections);
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                });
    }

    private List<String> retrieveContacts() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        if(cursor==null) return new ArrayList<>(0);
        ArrayList<String> ret=new ArrayList<>(cursor.getCount());
        try {
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                ret.add(name);
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

}
