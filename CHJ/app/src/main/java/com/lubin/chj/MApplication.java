package com.lubin.chj;

import android.app.Application;

import com.lubin.chj.exception.CrashHandler;
import com.lubin.chj.utils.SharePreferenceUtil;

import java.lang.reflect.Method;

public class MApplication extends Application {
    public static MApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }

    public static MApplication getInstance() {
        return mInstance;
    }

    // 单例模式，才能及时返回数据
    SharePreferenceUtil mSpUtil;

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String sharedName = "chj_sharedInfo";
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return serial;
    }

}
