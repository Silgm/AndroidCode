package com.example.xkf.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MyService.DownloadBinder downloadBinder;

    //如果想要activity和service通信的话
    //需要用匿名内部类来继承这个类
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //这里的service是那个Service的返回对象
            //要做一个强制类型的转换
            downloadBinder = (MyService.DownloadBinder) service;
            //这里处理具体的逻辑
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void bindService(View view) {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        unbindService(serviceConnection);
    }
}
