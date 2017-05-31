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
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private Boolean isFirstLocate = true;

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //必须在这调用
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.baidumap_view);
        baiduMap = mapView.getMap();
        //设置允许锁定当前位置
        baiduMap.setMyLocationEnabled(true);
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

    private void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            isFirstLocate = false;
            //封装位置
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            //把位置包装成一个更新对象
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            //更新
            baiduMap.animateMapStatus(update);
            //显示的等级为16
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
        }
        //建立一个当前位置对象
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(bdLocation.getLatitude());
        builder.longitude(bdLocation.getLongitude());
        //创建当前位置数据对象
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //允许使用GPS定位
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //获取详细的地址
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
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
            currentPosition.append("国家: ")
                    .append(bdLocation.getCountry())
                    .append("\n");
            currentPosition.append("省: ")
                    .append(bdLocation.getProvince())
                    .append("\n");
            currentPosition.append("市: ")
                    .append(bdLocation.getCity())
                    .append("\n");
            currentPosition.append("区: ")
                    .append(bdLocation.getDistrict())
                    .append("\n");
            currentPosition.append("街道: ")
                    .append(bdLocation.getStreet())
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
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                    bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
