package com.adrian.hackmyphone.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by adrian on 3/10/16.
 */
public class ItemVisibilityScrollListener extends RecyclerView.OnScrollListener {

    private final String TAG=getClass().getName();

    LinearLayoutManager mLayoutManager;
    int mLastTopPos=-1;
    int mLastBotPos=-1;
    ItemVisibilityChange mListener;

    public ItemVisibilityScrollListener(LinearLayoutManager layoutManager, ItemVisibilityChange listener) {
        mLayoutManager = layoutManager;
        mListener = listener;
        mLastTopPos = mLayoutManager.findFirstVisibleItemPosition();
        mLastBotPos = mLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int topItemPos = mLayoutManager.findFirstVisibleItemPosition();
        int bottomItemPos = mLayoutManager.findLastVisibleItemPosition();
        if(mLastTopPos!=-1 && mLastTopPos!=topItemPos) {
            if(topItemPos> mLastTopPos) notifyHide(mLastTopPos, topItemPos-1);
            else notifyShow(mLastTopPos-1, topItemPos);
        }
        if(mLastBotPos!=-1 && mLastBotPos!=bottomItemPos) {
            if(bottomItemPos>mLastBotPos) notifyShow(mLastBotPos+1, bottomItemPos);
            else notifyHide(mLastBotPos, bottomItemPos+1);
        }
        mLastTopPos=topItemPos;
        mLastBotPos=bottomItemPos;
    }

    public void notifyHide(int pos) {
        if(mListener!=null) mListener.onItemHidden(pos);
    }

    public void notifyShow(int pos) {
        if(mListener!=null) mListener.onItemShown(pos);
    }

    public void notifyHide(int a, int b) {
        int start=Math.min(a, b);
        int end=Math.max(a, b);
        for(int i=start; i<=end; i++) notifyHide(i);
    }

    public void notifyShow(int a, int b) {
        int start=Math.min(a, b);
        int end=Math.max(a, b);
        for(int i=start; i<=end; i++) notifyShow(i);
    }

    public interface ItemVisibilityChange {
        void onItemHidden(int position);
        void onItemShown(int position);
    }
}
