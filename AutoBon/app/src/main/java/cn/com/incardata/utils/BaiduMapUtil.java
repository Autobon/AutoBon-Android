package cn.com.incardata.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

import cn.com.incardata.autobon.R;

/**
 * Created by zhangming on 2016/2/22.
 * 百度地图工具类封装
 */
public class BaiduMapUtil {
    protected static View pop;
    protected static BroadcastReceiver receiver;

    protected static Overlay[] markOverlay;  //标志物图层
    protected static Overlay[] popOverlay;  //信息框图层
    protected static LatLng[] latLngArray;  //位置信息记录
    protected static String[] windowInfo;  //窗体信息记录

    protected static boolean isZoomCenter = true;
    protected static final int markZIndex = 1;
    protected static final int popZIndex = 2;
    protected static final int length = 4;
    public static final int defaultLevel = 15;  //常量字段

    /**
     * 注册百度地图的广播接收者
     * @param context
     */
    public static void registerBaiduMapReceiver(Context context){
        receiver = new BroadcastReceiver() {
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
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  //注册网络错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR); //注册key校验结果
        context.getApplicationContext().registerReceiver(receiver, filter);
    }

    /**
     * 注销百度地图的广播接收者
     * @param context
     */
    public static void unRegisterBaiduMapReceiver(Context context){
        context.getApplicationContext().unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * 自动定位当前位置
     */
    public static void locate(Context context,BaiduMap baiduMap,int scanTime,LocationClient mLocationClient,BDLocationListener myListener) {
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
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


    public static void initData() {
        markOverlay = new Overlay[length];
        popOverlay = new Overlay[length];
        latLngArray = new LatLng[length];
        windowInfo = new String[length];
    }

    /**
     * 隐藏百度Logo图标
     */
    public static void hiddenBaiduLogo(MapView mMapView){
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
    }

    public static void drawOnePoint(String address,OnGetGeoCoderResultListener geoCoderListener) {
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);
        mSearch.geocode(new GeoCodeOption().city(address).address(address));
    }

    public static Overlay drawMarker(BaiduMap baiduMap,LatLng latLng, BitmapDescriptor descriptor, int zIndex) {
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
        bitmaps.add(descriptor);
        markerOptions.position(latLng).icons(bitmaps).draggable(false);
        Overlay overlay = baiduMap.addOverlay(markerOptions);
        overlay.setZIndex(zIndex);
        return overlay;  //返回添加的图层,便于移除
    }

    public static Overlay drawPopWindow(BaiduMap baiduMap,Context context,LatLng latLng,String address,int zIndex){
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
        bitmaps.add(BitmapDescriptorFactory.fromView(initPop(context,address,true)));
        markerOptions.position(latLng).icons(bitmaps).draggable(false);
        Overlay overlay = baiduMap.addOverlay(markerOptions);
        overlay.setZIndex(zIndex);
        return overlay;  //返回添加的图层,便于移除
    }

    public static View initPop(Context context,final String address,boolean isInit) {
        if(pop==null || isInit) {
            pop = View.inflate(context, R.layout.overlay_pop, null);
            TextView title = (TextView) pop.findViewById(R.id.title);
            title.setText(address);
        }
        return pop;
    }

    /**
     * 单点缩放至中心
     */
    public static void zoomByOneCenterPoint(BaiduMap baiduMap,LatLng centerPoint,float level){
        MapStatus mapStatus = new MapStatus.Builder().target(centerPoint).zoom(level).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 百度地图根据两点缩放
     * @param onePoint
     * @param twoPoint
     */
    public static void zoomByTwoPoint(BaiduMap baiduMap,LatLng onePoint, LatLng twoPoint) {
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
    public static double getDistance(LatLng start,LatLng end){
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;
        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;

        double R = 6371;  //地球半径
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;  //两点间距离 km，如果想要米的话，结果*1000就可以了

        return d*1000;
    }

    public static class MyListener implements BDLocationListener {
        private Context context;
        private BaiduMap baiduMap;
        private TextView tv_distance; //代表底部距离的TextView控件
        private String mAddress; //另一个点的位置
        private Button sign_in_btn; //签到界面Button

        public MyListener(Context context, BaiduMap baiduMap, TextView tv_distance, String mAddress, Button sign_in_btn){
            this.context = context;
            this.baiduMap = baiduMap;
            this.tv_distance = tv_distance;
            this.mAddress = mAddress;
            this.sign_in_btn = sign_in_btn;
        }

        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);
                if(markOverlay[0]!=null){
                    View pop = BaiduMapUtil.initPop(context,null,false);
                    TextView tv = (TextView) pop.findViewById(R.id.title);
                    tv.setText(result.getAddrStr());
                }else{
                    markOverlay[0] = BaiduMapUtil.drawMarker(this.baiduMap,latLng,BitmapDescriptorFactory.fromResource(R.mipmap.eat_icon),markZIndex);
                    popOverlay[0] = BaiduMapUtil.drawPopWindow(this.baiduMap,context,latLng,result.getAddrStr(),popZIndex);
                    latLngArray[0] = latLng;
                    windowInfo[0] = result.getAddrStr();
                    BaiduMapUtil.drawOnePoint(mAddress,new MyGeoCoderListener(context,this.baiduMap));
                }
                if(isZoomCenter){
                    BaiduMapUtil.zoomByOneCenterPoint(baiduMap,latLngArray[0],defaultLevel);
                    isZoomCenter = false;
                }
                if(markOverlay[1] == null){
                    tv_distance.setText("0m");
                }else{
                    double distance = BaiduMapUtil.getDistance(latLngArray[0],latLngArray[1]); //单位为m

                    if(sign_in_btn!=null){  //签到界面有提示框,并且改变Button样式
                        if(Math.abs(distance)<=10){  //到达(有误差)
                            tv_distance.setText(R.string.arrive_text);
                            sign_in_btn.setClickable(true);
                            sign_in_btn.setBackgroundColor(context.getResources().getColor(R.color.main_orange));
                            sign_in_btn.setTextColor(context.getResources().getColor(android.R.color.white));
                        }else{
                            Toast toast = Toast.makeText(context.getApplicationContext(),context.getString(R.string.not_arrive_text),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    }
                    if(distance>=1000){  //距离大于等于1公里
                        distance = DecimalUtil.DoubleDecimal1(distance/1000);  //保留一位小数
                        tv_distance.setText(String.valueOf(distance)+"km");
                    }else{  //距离小于1公里
                        distance = DecimalUtil.DoubleDecimal1(distance); //保留一位小数
                        tv_distance.setText(String.valueOf(distance)+"m");
                    }
                }
            }
        }
    }

    public static class MyGeoCoderListener implements OnGetGeoCoderResultListener {
        private Context context;
        private BaiduMap baiduMap;

        public MyGeoCoderListener(Context context,BaiduMap baiduMap){
            this.context = context;
            this.baiduMap = baiduMap;
        }

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

            markOverlay[1] = BaiduMapUtil.drawMarker(this.baiduMap,result.getLocation(),BitmapDescriptorFactory.fromResource(R.mipmap.eat_icon),markZIndex);
            popOverlay[1] = BaiduMapUtil.drawPopWindow(this.baiduMap,context,result.getLocation(),result.getAddress(),popZIndex);
            latLngArray[1] = result.getLocation();
            windowInfo[1] = result.getAddress();
            BaiduMapUtil.zoomByTwoPoint(baiduMap,latLngArray[0],latLngArray[1]);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        }
    }


}
