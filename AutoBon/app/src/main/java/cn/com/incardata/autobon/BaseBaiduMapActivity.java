package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/2/24.
 * 有关百度地图页面的基类
 */
public class BaseBaiduMapActivity extends Activity{
    protected BroadcastReceiver receiver;
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;
    protected TextView tv_distance;

    protected static LatLng mLatLng = new LatLng(30.511869,114.405746);
    protected static String mAddress = "门店";  //测试地址,可以更改
    protected static int scanTime = 10*1000;  //设置10s定位一次


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNetManager();
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
    protected void onStop() {
        super.onStop();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        BaiduMapUtil.closeLocationClient(baiduMap,mLocationClient);  //关闭定位
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        //mMapView.onDestroy();
        super.onDestroy();
    }

    private void initManager() {
        receiver = new MyBaiduSDKReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  //注册网络错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR); //注册key校验结果
        registerReceiver(receiver, filter);
    }

    private void initNetManager(){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("test", "网络状态已经改变");
                if(NetWorkHelper.isNetworkAvailable(context)){
                   // BaiduMapUtil.locate(baiduMap,scanTime,mLocationClient,
                     //       new BaiduMapUtil.MyListener(context,baiduMap,tv_distance,mLatLng, mAddress,null));
                    BaiduMapUtil.locate(baiduMap);
                }else{
                    T.show(context,context.getString(R.string.no_network_error));
                }
            }
        }
    };

    class MyBaiduSDKReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getAction();
            if(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(result)){
                //网络错误
                Log.i("test","网络异常");
            }else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(result)){
                //key校验失败
                Log.i("test","key校验失败");
            }
        }
    }
}
