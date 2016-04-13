package com.adrian.hackmyphone.fragments;


import android.Manifest;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.items.Section;

import rx.functions.Func1;

public class ContactsFragment extends BaseContentProviderFragment<String, Section> {

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    protected Func1<Cursor, String> getMapCursor() {
        return cursor -> cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
    }

    @Override
    protected Func1<String, Section> getMapToListObj() {
        return Section::new;
    }

    @Override
    protected String getPermission() {
        return Manifest.permission.READ_CONTACTS;
    }

    @Override
    protected String getPermissionErrorMessage() {
        return getString(R.string.allow_read_contacts_to_work);
    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null) mSubscription.unsubscribe();
        super.onDestroy();
    }

}
