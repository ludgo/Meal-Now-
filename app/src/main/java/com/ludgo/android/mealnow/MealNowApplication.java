package com.ludgo.android.mealnow;

import com.activeandroid.app.Application;
import com.ludgo.android.mealnow.ui.activity.MainActivity;

/**
 * An activity extending ActiveAndroid application to take advantage of their ORM system
 */
public class MealNowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
