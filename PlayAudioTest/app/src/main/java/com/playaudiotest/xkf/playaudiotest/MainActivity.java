package com.playaudiotest.xkf.playaudiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        initMediaPlayer();
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    private void initMediaPlayer() {
        try {
            //把文件放到assets里面的话就是这样
            //先getAssets，获得一个Manager
            AssetManager manager = getAssets();
            //然后openFd那个文件
            AssetFileDescriptor descriptor = manager.openFd("music.mp3");
            //第一步设置资源
            //这里注意，少了一个参数都别想播放
            //很坑爹
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor()
                    , descriptor.getStartOffset(), descriptor.getLength());
            //第二步准备
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "没找到", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        //第三部操作
        //reset是让他停下，不是stop
        switch (view.getId()) {
            case R.id.play:
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                break;
            case R.id.pause:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case R.id.stop:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.reset();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            //第四步 stop
            mMediaPlayer.stop();
            //第五步 释放资源
            mMediaPlayer.release();
        }
    }
}
