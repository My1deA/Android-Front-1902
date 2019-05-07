package com.example.projectthree;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

public class MainApplication extends Application {
    private final static String TAG="MainApplication";

    private static MainApplication mApp;

    public HashMap<String,String> UserinfoMap=new HashMap<String,String>();

    // 利用单例模式获取当前应用的唯一实例
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在打开应用时对静态的应用实例赋值
        mApp = this;
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }


}
