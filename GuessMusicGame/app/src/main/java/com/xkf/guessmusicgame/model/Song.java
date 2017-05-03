package com.xkf.guessmusicgame.model;

public class Song {
    private String mSongName;
    private String mSongFileNmae;
    private int mNmaeLength;

    public char[] getNameChar() {
        return mSongName.toCharArray();
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
        mNmaeLength = songName.length();
    }

    public String getSongFileNmae() {
        return mSongFileNmae;
    }

    public void setSongFileNmae(String songFileNmae) {
        mSongFileNmae = songFileNmae;
    }

    public int getNmaeLength() {
        return mNmaeLength;
    }


}
