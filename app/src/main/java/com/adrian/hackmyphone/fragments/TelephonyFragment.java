package com.adrian.hackmyphone.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrian.hackmyphone.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TelephonyFragment extends Fragment {

    @Bind(R.id.text)
    TextView mTextView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private Subscription mSubscription;


    public static Fragment newInstance() {
        return new TelephonyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_telephony, container, false);
        ButterKnife.bind(this, view);
        TelephonyManager telephonyManager= (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        mSubscription = Observable.defer(() -> Observable.from(telephonyManager.getClass().getDeclaredMethods()))
                .onErrorResumeNext(throwable -> Observable.empty())
                .filter(method -> method.getParameterTypes().length == 0)
                .filter(method1 -> !method1.getReturnType().equals(Void.class))
                .map(method2 -> invokeToString(method2, telephonyManager))
                .reduce(String::concat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mTextView.setText(s);
                    mProgressBar.setVisibility(View.GONE);
                });
        return view;
    }

    @Override
    public void onDetach() {
        if(mSubscription!=null) mSubscription.unsubscribe();
        super.onDetach();
    }

    private String invokeToString(Method method, Object o) {
        StringBuilder b=new StringBuilder();
        try {
            Object invoke = method.invoke(o);
            b.append(method.getName()).append(": ");
            b.append(invoke).append("\n");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return b.toString();
    }
}