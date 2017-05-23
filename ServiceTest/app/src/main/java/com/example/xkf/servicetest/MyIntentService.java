package com.example.xkf.servicetest;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //这个方法不是运行在猪线程中的
        Log.e("dalongmao", "Thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //onHandleIntent 运行完之后自动调用
        //非常方便的一个异步服务
        Log.e("dalongmao", "onDestroy");
    }
}
