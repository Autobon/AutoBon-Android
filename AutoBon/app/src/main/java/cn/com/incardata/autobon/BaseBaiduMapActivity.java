package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.utils.BaiduMapUtil;

/**
 * Created by zhangming on 2016/2/24.
 */
public class BaseBaiduMapActivity extends Activity{
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;

    protected static final LatLng mLatLng = new LatLng(30.511869,114.405746);
    protected static final String mAddress = "门店";  //测试地址,可以更改
    protected static final int scanTime = 30000;  //设置30s定位一次

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaiduMapUtil.registerBaiduMapReceiver(this);  //注册百度地图广播接收者
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        //BaiduMapUtil.unRegisterBaiduMapReceiver(this);
        mLocationClient.stop();
        baiduMap.clear();
        baiduMap.setMyLocationEnabled(false); // 关闭定位图层
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        //mMapView.onDestroy();
        //mMapView = null;
        super.onDestroy();
    }
}
