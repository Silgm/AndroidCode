package com.example.xkf.servicebestpractice;


public interface DownloadListener {
    void onProgress(double progress);

    void onSuccess();

    void onFailed();

    void inPaused();

    void onCanceled();
}
