package com.adrian.hackmyphone.fragments;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Adrian on 2016-04-13.
 */
public abstract class BaseContentProviderFragment<D, L> extends BaseListProgressFragment {

    private static final int REQ_PERMISSION = 1;
    Subscription mSubscription;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(ActivityCompat.checkSelfPermission(getActivity(), getPermission())!= PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(new String[]{getPermission()}, REQ_PERMISSION);
        } else callPopulateList();
        return view;
    }

    protected abstract Func1<Cursor, D> getMapCursor();
    protected abstract Func1<D, L> getMapToListObj();
    protected abstract String getPermission();
    protected abstract String getPermissionErrorMessage();

    protected void callPopulateList() {
        populateList(getMapToListObj());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION: {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    callPopulateList();
                } else {
                    Snackbar.make(getView(), getPermissionErrorMessage(), Snackbar.LENGTH_SHORT).show();
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

    private void populateList(Func1<D, L> mapFunc) {
        mSubscription = Observable.defer(() -> Observable.from(retrieve(getMapCursor())))
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mapFunc)
                .toList()
                .subscribe(sections -> {
                    mAdapter.elems.addAll(sections);
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                });
    }

    private List<D> retrieve(Func1<Cursor, D> func) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        if(cursor==null) return new ArrayList<>(0);
        ArrayList<D> ret=new ArrayList<>(cursor.getCount());
        try {
            while(cursor.moveToNext()) {
                D obj = func.call(cursor);
                ret.add(obj);
            }
        } finally {
            cursor.close();
        }
        return ret;
    }
}
