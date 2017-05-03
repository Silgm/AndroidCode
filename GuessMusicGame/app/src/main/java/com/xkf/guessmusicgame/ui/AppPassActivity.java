package com.xkf.guessmusicgame.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xkf.guessmusicgame.R;
import com.xkf.guessmusicgame.data.Const;
import com.xkf.guessmusicgame.util.Util;

public class AppPassActivity extends Activity {
    private static Context mcontext;
    //启动一个活动
    public static void startActivity(Context context) {
        mcontext=context;
        Intent intent = new Intent(context, AppPassActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass_layout);
    }

    public void reStart(View view) {
        Toast.makeText(this, "骗你的。。。。回到第一关了", Toast.LENGTH_SHORT).show();
        Util.saveData(mcontext,-1, Const.TOTAL_COINS);
        Intent intent = new Intent(this,mcontext.getClass());
        startActivity(intent);
        this.finish();
    }
}
