package com.example.xkf.servicebestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(double progress) {
            //把这个manager拉出来
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //更新通知
            manager.notify(1, getNotification("下载中", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, getNotification("下载成功", -1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, getNotification("下载失败", -1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void inPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "已暂停", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "下载已取消", Toast.LENGTH_SHORT).show();
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    //删文件
                    file.delete();
                }
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //撤销掉前台的通知栏
                manager.cancel(1);
                stopForeground(true);
            }
        }
    };

    private Notification getNotification(String tittle, double progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(tittle);
        builder.setContentIntent(pi);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (progress >= 0) {
            //进度大于0的时候需要显示
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
        }
        return builder.build();
    }

    public DownloadService() {
    }

    //这里选择绑定MainActivity
    //在绑定的时候开启异步线程
    private DownloadBind binder = new DownloadBind();
    private DownloadTask downloadTask;
    private String downloadUrl;

    class DownloadBind extends Binder {
        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                //开启异步线程来不停的下载数据并且更新进度条
                //同时在前台推送一个显示进度条的通知
                startForeground(1, getNotification("下载中", 0));
                Toast.makeText(DownloadService.this, "开始下载", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
