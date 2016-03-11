package com.adrian.hackmyphone.items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by adrian on 3/11/16.
 */
public class IntentItem implements Titled {
    private String title;
    private Intent mIntent;

    public IntentItem(String title, Intent intent) {
        this.title = title;
        mIntent = intent;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public static class Builder {
        private Context mContext;
        private Class<? extends Activity> mActivityClass;
        private String mTitle;

        public Builder() {
        }

        public Builder context(Context context) {
            mContext = context;
            return this;
        }

        public Builder activityClass(Class<? extends Activity> activityClass) {
            mActivityClass = activityClass;
            return this;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public IntentItem build() {
            return new IntentItem(mTitle, new Intent(mContext, mActivityClass));
        }
    }
}
