package com.example.xkf.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Double, Integer> {
    private static final int TYPE_CANCELED = 3;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    public static final int TYPE_SUCCESSED = 0;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private DownloadListener listener;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = params[0];
            //通常来说把要下载的路径当作文件夹的名字
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            //这个方法是开辟一个特殊的文件夹来放置自己的东西
            //利用它获得文件夹的路径
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath();
            //组合起来创建出来文件
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadLength = file.length();
            }
            long contentLength =contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //这里addHeader是为了从指定的位置开始拉取数据
                    .url(downloadUrl)
                    .addHeader("RANGE", "bytes=" + downloadLength + "-")
                    .build();
            //response获得拉取的数据
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                //定位文件指针
                savedFile.seek(downloadLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        //写入文件
                        savedFile.write(b, 0, len);
                        //算出进度总和
                        double nowLength = (double) (total + downloadLength);
                        double rate = nowLength / (double) contentLength;
                        double progress = rate * 100;
                        //更新数据
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESSED;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && (file != null)) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        double progress = values[0];
        if (progress >= 0) {
            listener.onProgress(progress);
        }
    }

    //这个方法用于返回下载东西的字节数
    private long getContentLength(String downloadUrl) throws IOException {
        //使用OkHttp来拉取数据
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        //照常把数据拉出来到response
        if (response != null && response.isSuccessful()) {
            //它的body部分就是要下载的数据部分
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_SUCCESSED:
                listener.onSuccess();
                break;
            case TYPE_PAUSED:
                listener.inPaused();
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }
}
