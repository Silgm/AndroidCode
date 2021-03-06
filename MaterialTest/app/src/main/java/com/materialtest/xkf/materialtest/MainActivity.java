package com.materialtest.xkf.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Fruit[] fruits = {new Fruit("Apple", R.mipmap.apple), new Fruit("Banana", R.mipmap.banana),
            new Fruit("Orange", R.mipmap.orange), new Fruit("Watermelon", R.mipmap.watermelon),
            new Fruit("Pear", R.mipmap.pear), new Fruit("Grape", R.mipmap.grape),
            new Fruit("Pineapple", R.mipmap.pineapple), new Fruit("Strawberry", R.mipmap.strawberry),
            new Fruit("Cherry", R.mipmap.cherry), new Fruit("Mango", R.mipmap.mango)};

    private List<Fruit> mFruitList = new ArrayList<>();

    private FruitAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Material Design 的第一个组件-------上方那个标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (actionBar != null) {
            //显示系统的那个返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //把系统的那个返回按钮换成自己的图片
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        //Material Design 的第二个组件---------侧滑菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //他一般和NavigationView这个东西他一起使用
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_call:
                        Toast.makeText(MainActivity.this, "你点击了Call", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this, "你点击了Friends", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this, "你点击了Location", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_mail:
                        Toast.makeText(MainActivity.this, "你点击了Mail", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this, "你点击了Task", Toast.LENGTH_SHORT).show();
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //Material Design 第三个组件----------悬浮按钮
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.flb);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Material Design 第四个组件-----------一个可以提示并且点击交互的提示条
                Snackbar snackbar = Snackbar.make(view, "我想你可能需要我", Snackbar.LENGTH_SHORT);
                snackbar.setAction("点击这里", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "哈哈哈哈哈", Toast.LENGTH_SHORT).show();
                    }
                });
                snackbar.show();
            }
        });

        //Material Design 第五个组件---------强大无匹的CoordinatorLayout布局
        // 自动监听界面中组件的变化并且做出反应

        //Material Design 第六个组件---------CardView，这里用作RecycleView的item视图
        initFruits();
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mAdapter = new FruitAdapter(mFruitList);
        recycleView.setAdapter(mAdapter);
        recycleView.setLayoutManager(manager);

        //Material Design 第七个组件---------AppBarLayout
        //这个组件的效果非常不错，保护了ToolBar，又能合适的隐藏它

        //Material Design 第八个组件----------SwipeRefreshLayout
        //简单说就是下拉刷新组件
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //设置那个旋转的进度条的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //添加逻辑的代码
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

        //Material Design 第八个组件可折叠的标题栏


    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        mAdapter.notifyDataSetChanged();
                        //这是告知已经刷新完毕的标志
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initFruits() {
        for (int i = 0; i < 50; i++) {
            mFruitList.add(fruits[i % fruits.length]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //打开侧滑菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }
}
