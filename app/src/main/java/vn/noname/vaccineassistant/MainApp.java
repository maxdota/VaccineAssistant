package vn.noname.vaccineassistant;

import android.app.Application;

public class MainApp extends Application {
    public static MainApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
}
