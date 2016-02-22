package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by zhangming on 2016/2/22.
 * 工作签到
 */
public class WorkSignInActivity extends Activity{
    private TextView tv_day,tv_week_day,tv_distance;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private Context context;
    private static final String mAddress = "武汉光谷广场";  //测试地址,可以更改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaiduMapUtil.registerBaiduMapReceiver(this);  //注册百度地图广播接收者
        setContentView(R.layout.work_map_address);
        initBaiduMapView();
        initView();
        setListener();
    }

    public void initBaiduMapView(){
        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别
        mLocationClient = new LocationClient(this);

        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
    }

    private void initView(){
        context = this;
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_week_day = (TextView) findViewById(R.id.tv_week_day);
        tv_distance = (TextView) findViewById(R.id.tv_distance);

        tv_day.setText(DateCompute.getCurrentYearMonthDay());
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        dateFm.format(date);
        tv_week_day.setText(DateCompute.getWeekOfDate(date));  //传入参数值为null代表获取当前系统时间为星期几
    }

    public void setListener(){
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置,定位扫描时间为5s
                BaiduMapUtil.locate(context,baiduMap,5000,mLocationClient,
                        new BaiduMapUtil.MyListener(context,baiduMap,tv_distance,mAddress));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaiduMapUtil.unRegisterBaiduMapReceiver(this);
        mLocationClient.stop();
        baiduMap.clear();
        baiduMap.setMyLocationEnabled(false); // 关闭定位图层
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
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

}
