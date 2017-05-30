package com.example.xkf.a360floatwindowdemo;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
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
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (isHome()) {

            }
        }
    }

    private boolean isHome() {
        //获取activity manager
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = manager.getRunningTasks(1);

    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        //intent设置的过滤条件是启动的那个主要的activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //这里添加了随着系统启动而启动的activity
        //有了这两个action的话,就可以拉取到所有属于桌面应用的activity
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolve = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolve) {
            names.add(ri.resolvePackageName);
        }
        return names;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
