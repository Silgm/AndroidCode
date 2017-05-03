package com.firstcodeofandroid.xkf.test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("x").setContent(R.id.tab1).setIndicator("1"));
        tabHost.addTab(tabHost.newTabSpec("y").setContent(R.id.tab2).setIndicator("2"));
        tabHost.addTab(tabHost.newTabSpec("z").setContent(R.id.tab3).setIndicator("3"));
    }
}
