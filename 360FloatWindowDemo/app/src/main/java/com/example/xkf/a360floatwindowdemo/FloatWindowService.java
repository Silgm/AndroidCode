package com.example.xkf.a360floatwindowdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {
    private Timer timer;

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //如果定时器没有被开启,那么就创建出来然后每0.5秒运行一下
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(),0,500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask{

        @Override
        public void run() {

        }
    }
}
