package com.xkf.guessmusicgame.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MyPlayer {
    public static final int SOUND_ENTER = 0;

    public static final int SOUND_CANCEL = 1;

    public static final int SOUND_COIN = 2;

    //三个音效的名字
    private static String[] soundName = {"enter.mp3", "cancel.mp3", "coin.mp3"};

    //一个播放器数组，每一个对应一个按钮点击的音效
    private static MediaPlayer[] mButtonMediaPlayer = new MediaPlayer[soundName.length];

    //专门播放歌曲
    private static MediaPlayer mMusicMediaPlayer;

    //播放按钮的音效
    public static void playTone(Context context, int index) {
        if (mButtonMediaPlayer[index] == null) {
            mButtonMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor = context.getAssets().openFd("" + soundName[index]);
                mButtonMediaPlayer[index].setDataSource
                        (fileDescriptor.getFileDescriptor(),
                                fileDescriptor.getStartOffset(),
                                fileDescriptor.getLength());
                mButtonMediaPlayer[index].prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mButtonMediaPlayer[index].start();
    }

    //播放歌曲
    public static void playSong(Context context, String fileName) {
        if (mMusicMediaPlayer == null) {
            mMusicMediaPlayer = new MediaPlayer();
        }
        mMusicMediaPlayer.reset();
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mMusicMediaPlayer.setDataSource
                    (fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
            mMusicMediaPlayer.prepare();
            mMusicMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //停止歌曲
    public static void stopTheSong() {
        if (mMusicMediaPlayer != null) {
            mMusicMediaPlayer.stop();
        }
    }
}
