package com.example.admin.tiff;

import android.app.Application;

/**
 * Created by 衡峰 on 2017/10/23.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**MuPDF*/
        System.loadLibrary("mupdf");
    }
}
