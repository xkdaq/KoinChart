package com.xuke.koinchart;

import android.app.Application;

import com.xuke.koinchart.util.OkHttpUtil;

/**
 * Created by kekex on 2018/9/5.
 *
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtil.initOkHttp();
    }
}
