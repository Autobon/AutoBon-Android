package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;

import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.T;

/**
 * Created by Administrator on 2016/2/17.
 */
public class OrderReceiverActivity extends Activity{
    private Context context;
    private MyBaiduSDKReceiver receiver;
    private MapView mMapView;
    private BaiduMap baiduMap;
    public LocationClient mLocationClient;
    private MyListener myListener;
    private View pop;
    private TextView tv_distance;
    private boolean isZoomCenter = true;

    private Overlay[] markOverlay;  //标志物图层
    private Overlay[] popOverlay;  //信息框图层
    private LatLng[] latLngArray;  //位置信息记录
    private String[] windowInfo;  //窗体信息记录

    private static final int markZIndex = 1;
    private static final int popZIndex = 2;
    private static final int length = 4;  //常量字段
    private static final int defaultLevel = 15;

    private static final String mAddress = "武汉同济医院";  //测试地址,可以更改


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initManager();
        setContentView(R.layout.order_receiver_activity);
        initView();
        initData();
        setListener();
    }

    public void initManager(){
        receiver = new MyBaiduSDKReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  //注册网络错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR); //注册key校验结果
        registerReceiver(receiver, filter);
    }

    class MyBaiduSDKReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getAction();
            if(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(result)){
                //网络错误
                T.show(context,context.getString(R.string.no_network_error));
            }else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(result)){
                //key校验失败
                T.show(context,context.getString(R.string.error_key_tips));
            }
        }
    }

    class MyListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);
                if(markOverlay[0]!=null){
                    TextView tv = (TextView) pop.findViewById(R.id.title);
                    tv.setText(result.getAddrStr());
                }else{
                    markOverlay[0] = drawMarker(latLng,BitmapDescriptorFactory.fromResource(R.mipmap.eat_icon),markZIndex);
                    popOverlay[0] = drawPopWindow(latLng,result.getAddrStr(),popZIndex);
                    latLngArray[0] = latLng;
                    windowInfo[0] = result.getAddrStr();
                    drawOnePoint(mAddress);
                }
                if(isZoomCenter){
                    zoomByOneCenterPoint(latLngArray[0],defaultLevel);
                    isZoomCenter = false;
                }
                if(markOverlay[1] == null){
                    tv_distance.setText("0");
                }else{
                    double distance = getDistance(latLngArray[0],latLngArray[1])/1000; //单位为km
                    distance = DecimalUtil.DoubleDecimal1(distance);  //保留一位小数
                    tv_distance.setText(String.valueOf(distance));
                }
            }
        }
    }

    class MyGeoCoderListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                T.show(context,context.getString(R.string.no_result_tips));
                return;
            }
            if(markOverlay[1]!=null){
                markOverlay[1].remove();
            }
            if(popOverlay[1]!=null){
                popOverlay[1].remove();
            }

            markOverlay[1] = drawMarker(result.getLocation(),BitmapDescriptorFactory.fromResource(R.mipmap.eat_icon),markZIndex);
            popOverlay[1] = drawPopWindow(result.getLocation(),result.getAddress(),popZIndex);
            latLngArray[1] = result.getLocation();
            windowInfo[1] = result.getAddress();
            zoomByTwoPoint(latLngArray[0],latLngArray[1]);
            double distance = getDistance(latLngArray[0], latLngArray[1]);

            tv_distance.setText(String.valueOf((int)distance)+"米");
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        }
    }

    public void initView(){
        context = this;
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别

        hiddenBaiduLogo();  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
    }

    private void initData() {
        markOverlay = new Overlay[length];
        popOverlay = new Overlay[length];
        latLngArray = new LatLng[length];
        windowInfo = new String[length];
    }

    public void setListener(){
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                locate();  //地图加载完成后自动定位
            }
        });
    }

    /**
     * 自动定位当前位置
     */
    private void locate() {
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyListener();
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS

        mLocationClient.setLocOption(option);
        mLocationClient.start();
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true,
                BitmapDescriptorFactory.fromResource(R.mipmap.eat_icon));
        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
        baiduMap.setMyLocationEnabled(true);// 打开定位图层p
        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
    }

    /**
     * 隐藏百度Logo图标
     */
    public void hiddenBaiduLogo(){
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
    }

    protected void drawOnePoint(String address) {
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new MyGeoCoderListener());
        mSearch.geocode(new GeoCodeOption().city(address).address(address));
    }

    private Overlay drawMarker(LatLng latLng, BitmapDescriptor descriptor, int zIndex) {
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
        bitmaps.add(descriptor);
        markerOptions.position(latLng).icons(bitmaps).draggable(false);
        Overlay overlay = baiduMap.addOverlay(markerOptions);
        overlay.setZIndex(zIndex);
        return overlay;  //返回添加的图层,便于移除
    }

    private Overlay drawPopWindow(LatLng latLng,String address,int zIndex){
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
        bitmaps.add(BitmapDescriptorFactory.fromView(initPop(latLng,address)));
        markerOptions.position(latLng).icons(bitmaps).draggable(false);
        Overlay overlay = baiduMap.addOverlay(markerOptions);
        overlay.setZIndex(zIndex);
        return overlay;  //返回添加的图层,便于移除
    }

    private View initPop(LatLng latLng,final String address) {
        pop = View.inflate(getApplicationContext(),R.layout.overlay_pop,null);
        TextView title = (TextView) pop.findViewById(R.id.title);
        title.setText(address);
        return pop;
    }

    /**
     * 单点缩放至中心
     */
    private void zoomByOneCenterPoint(LatLng centerPoint,float level){
        MapStatus mapStatus = new MapStatus.Builder().target(centerPoint).zoom(level).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 百度地图根据两点缩放
     * @param onePoint
     * @param twoPoint
     */
    private void zoomByTwoPoint(LatLng onePoint, LatLng twoPoint) {
        LatLngBounds bounds = new LatLngBounds.Builder().include(onePoint).include(twoPoint).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        baiduMap.setMapStatus(mapStatusUpdate);

        float level = baiduMap.getMapStatus().zoom;
        MapStatus mapStatus = new MapStatus.Builder().zoom(level).build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(update);
    }

    /**
     * 获取两点间距离
     * @param start
     * @param end
     * @return
     */
    public double getDistance(LatLng start,LatLng end){
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;
        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;

        double R = 6371;  //地球半径
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;  //两点间距离 km，如果想要米的话，结果*1000就可以了

        return d*1000;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
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
