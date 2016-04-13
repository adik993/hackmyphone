package com.adrian.hackmyphone.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.adrian.hackmyphone.R;
import com.adrian.hackmyphone.items.Section;
import com.adrian.hackmyphone.items.SmsMsg;

import rx.functions.Func1;

/**
 * Created by Adrian on 2016-04-13.
 */
public class SmsFragment extends BaseContentProviderFragment<SmsMsg, Section> {
    private static int LIMIT = 30;

    public static SmsFragment newInstance() {
        SmsFragment fragment = new SmsFragment();
        return fragment;
    }

    @Override
    protected Func1<Cursor, SmsMsg> getMapCursor() {
        return cursor -> {
            String address = cursor.getString(cursor.getColumnIndex("address"));//Telephony.Sms.ADDRESS
            String body = cursor.getString(cursor.getColumnIndex("body"));//Telephony.Sms.BODY
            return new SmsMsg(address, body);
        };
    }

    @Override
    protected Func1<SmsMsg, Section> getMapToListObj() {
        return smsMsg -> new Section(smsMsg.getAddress()+": "+smsMsg.getContent());
    }

    @Override
    protected String getPermission() {
        return Manifest.permission.READ_SMS;
    }

    @Override
    protected Cursor makeQuery(ContentResolver contentResolver) {
        return contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC"+" LIMIT "+LIMIT);
    }

    @Override
    protected String getPermissionErrorMessage() {
        return getString(R.string.allow_read_sms_to_work);
    }
}
