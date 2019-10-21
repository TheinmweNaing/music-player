package com.team.musicplayer;

import android.app.Application;

public class MusicPlayerApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getApplication() {
        return application;
    }
}
