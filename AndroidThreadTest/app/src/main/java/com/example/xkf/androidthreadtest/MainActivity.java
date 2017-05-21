package com.example.xkf.androidthreadtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mText;
    public static final int UPDATA_TEXT = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_TEXT:
                    mText.setText("Nice to meet you");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.show_text);
    }

    public void change(View view) {
        switch (view.getId()) {
            case R.id.change_text:
                Message message = new Message();
                message.what = UPDATA_TEXT;
                handler.sendMessage(message);
        }
    }
}
