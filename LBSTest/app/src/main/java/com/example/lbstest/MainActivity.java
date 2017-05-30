package com.example.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        positionText = (TextView) findViewById(R.id.position_text_view);
        //这个地方用的是application整体的context
        mLocationClient = new LocationClient(getApplicationContext());
        //在接收到位置信息的时候就会调用这个接口啦
        mLocationClient.registerLocationListener(new MyLocationListener());
        //把权限信息存起来然后一起请求
        List<String> permissionList = new ArrayList<>();
        //申请运行时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionList.isEmpty()) {
            requestLocation();
        } else {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void requestLocation() {
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "不给权限生气啦！！！！！！！！！！", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    //有了权限的话就可以运行啦
                    requestLocation();
                } else {
                    Toast.makeText(this, "你可能用了假的Android手机,赶快换一个吧", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //在这个方法里面设置要显示的内容
            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度: ")
                    .append(bdLocation.getLatitude())
                    .append("\n");
            currentPosition.append("经线: ")
                    .append(bdLocation.getLongitude())
                    .append("\n");
            currentPosition.append("定位方式: ");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  positionText.setText(currentPosition.toString());
                              }
                          }
            );
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            
        }
    }
}
