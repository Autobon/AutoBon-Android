package cn.com.incardata.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by wanghao on 16/2/24.
 */
public class BaiduMapFragment extends BaseFragment{

    protected BroadcastReceiver receiver;
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;

    protected static final LatLng mLatLng = new LatLng(30.511869,114.405746);
    protected static final String mAddress = "门店";  //测试地址,可以更改
    protected static final int scanTime = 10000;  //设置10s定位一次


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //registerBaiduMapReceiver(this);  //注册百度地图广播接收者
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        //unRegisterBaiduMapReceiver(this);
        mLocationClient.stop();
        baiduMap.clear();
        baiduMap.setMyLocationEnabled(false); // 关闭定位图层
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        //mMapView.onDestroy();
        //mMapView = null;
        super.onDestroy();
    }

    /**
     * 注册百度地图的广播接收者
     * @param context
     */
    public void registerBaiduMapReceiver(Context context){
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  //注册网络错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR); //注册key校验结果
        context.getApplicationContext().registerReceiver(receiver, filter);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getAction();
                if(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(result)){
                    //网络错误
                    //T.show(context,context.getString(R.string.no_network_error));
                }else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(result)){
                    //key校验失败
                    //T.show(context,context.getString(R.string.error_key_tips));
                }
            }
        };
    }

    /**
     * 注销百度地图的广播接收者
     * @param context
     */
    public void unRegisterBaiduMapReceiver(Context context){
        context.getApplicationContext().unregisterReceiver(receiver);
        receiver = null;
    }
}
