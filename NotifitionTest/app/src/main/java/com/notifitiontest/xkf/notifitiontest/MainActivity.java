package com.notifitiontest.xkf.notifitiontest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("My First Notification");
        builder.setContentText("I Will Study Android Every Day");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        Intent intent = new Intent(this, ClickContentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Intent intent2 = new Intent(this, ClickContentActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity
                (this, 5678, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action
                (R.mipmap.ic_launcher_round, "I Got It!", pendingIntent2);
        builder.addAction(action);
        NotificationManager manager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(123, builder.build());
    }
}
