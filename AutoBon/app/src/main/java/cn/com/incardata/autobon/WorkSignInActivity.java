package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
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
public class WorkSignInActivity extends BaseBaiduMapActivity{
    private TextView tv_day,tv_week_day;
    private Context context;
    private Button sign_in_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_map_address);
        initBaiduMapView();
        initView();
        setListener();
    }

    protected void initBaiduMapView(){
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
        super.tv_distance = (TextView) findViewById(R.id.tv_distance);
        sign_in_btn = (Button) findViewById(R.id.sign_in_btn);

        tv_day.setText(DateCompute.getCurrentYearMonthDay());
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        dateFm.format(date);
        tv_week_day.setText(DateCompute.getWeekOfDate(date)); //获取当前日期是星期几
    }

    public void setListener(){
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置,定位扫描时间为5s,true代表是签到界面
                BaiduMapUtil.locate(baiduMap,scanTime, mLocationClient,
                        new BaiduMapUtil.MyListener(context,baiduMap,tv_distance,mLatLng,mAddress,sign_in_btn));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
