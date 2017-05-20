package com.networktest.xkf.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent = (TextView) findViewById(R.id.text);
    }

    public void send(View view) {
        sendRequestWithHttpUrlConnection();
    }

    private void sendRequestWithHttpUrlConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    //好吧，这里的坑爹网址
                    //注意一定是https
                    URL url = new URL("https://www.baidu.com");
                    connect = (HttpURLConnection) url.openConnection();
                    connect.setRequestMethod("GET");
                    InputStream in = connect.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        Log.e("dalongmao", line);
                        builder.append(line);
                    }
                    showContent(builder.toString());
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connect != null) {
                        connect.disconnect();
                    }
                }
            }
        }).start();

    }

    private void showContent(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContent.setText(content);
            }
        });
    }
}
