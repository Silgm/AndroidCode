package com.example.xkf.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class MyService extends Service {
    public MyService() {

    }

    private DownloadBinder mBinder = new DownloadBinder();
    //自己创建一个绑定类
    //继承Binder
    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.e("dalongmao", "startDownload");
        }

        public int getProgress() {
            Log.e("dalongmao", "getProgress");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回这个类的实例
        //会在serviceconnect中用到
        return mBinder;
    }

    //service运行先是create
    @Override
    public void onCreate() {
        Log.e("dalongmao", "onCreate");
        super.onCreate();
    }

    //再是onStartCommand
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("dalongmao", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    //销毁运行这个
    @Override
    public void onDestroy() {
        Log.e("dalongmao", "onDestroy");
        super.onDestroy();
    }
}
