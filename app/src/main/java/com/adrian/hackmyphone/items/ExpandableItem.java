package com.adrian.hackmyphone.items;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by adrian on 3/7/16.
 */
public class ExpandableItem extends LinearLayout {

    boolean mExpanded=false;
    private ExpandListener mListener;

    public ExpandableItem(Context context) {
        super(context);
    }

    public ExpandableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(getChildCount()>=2) throw new IllegalStateException("Cannot have more than 2 childs");
        else {
            if(!mExpanded && getChildCount()>0) {
                child.setVisibility(GONE);
            }
            super.addView(child, index, params);
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean animate, boolean expanded) {
        if(mExpanded==expanded) return;
        mExpanded = expanded;
        View childAt = getChildAt(1);
        if(animate && childAt!=null) {
            if(mExpanded) expand(childAt);
            else collapse(childAt);
        } else if (childAt!=null){
            if(mExpanded) {
                childAt.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
                childAt.setVisibility(VISIBLE);
            }
            else childAt.setVisibility(GONE);
        }
        requestLayout();
        invalidate();
    }

    public void toggleExpanded(boolean animate) {
        setExpanded(animate, !isExpanded());
    }

    public void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
                if(interpolatedTime==1 && mListener!=null) mListener.onExpanded();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                    if(mListener!=null) mListener.onCollapsed();
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void setListener(ExpandListener listener) {
        mListener = listener;
    }

    public interface ExpandListener {
        void onExpanded();
        void onCollapsed();
    }
}
