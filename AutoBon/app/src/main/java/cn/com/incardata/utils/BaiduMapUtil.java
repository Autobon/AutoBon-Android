package cn.com.incardata.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;

import java.io.File;
import java.util.ArrayList;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.NetWorkHelper;

/**
 * Created by zhangming on 2016/2/22.
 * 百度地图工具类封装
 */
public class BaiduMapUtil {
    protected static View pop;
    protected static Overlay[] markOverlay;  //标志物图层
    protected static Overlay[] popOverlay;  //信息框图层
    protected static LatLng[] latLngArray;  //位置信息记录
    protected static String[] windowInfo;  //窗体信息记录

    public static final int markZIndex = 1;
    public static final int popZIndex = 2;
    protected static final int length = 4;
    public static final int defaultLevel = 15;  //常量字段

    public static BaiduMap baiduMaps = null;

    public static LatLng baidulatlng = null;
    private static LatLng endLatlng = null;
    private static Context contexts = null;
    private static String shopNames = null;
    private static Marker shopMarker = null;


    public static LatLng getBaidulatlng() {
        return baidulatlng;
    }

    public static void setBaidulatlng(LatLng baidulatlng) {
        BaiduMapUtil.baidulatlng = baidulatlng;
    }

    /**
     * 自动定位当前位置
     */
    public static void locate(BaiduMap baiduMap,int scanTime,LocationClient mLocationClient,BDLocationListener myListener) {
        baiduMaps = baiduMap;
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();
        baiduMap.setMyLocationEnabled(true);// 打开定位图层
        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
        locate(baiduMap);
    }

    public static void locate(BaiduMap baiduMap){
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true,
                BitmapDescriptorFactory.fromResource(R.drawable.here));
        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
    }

    public static void closeLocationClient(BaiduMap baiduMap,LocationClient mLocationClient){
        if(mLocationClient!=null){
            mLocationClient.stop();
        }
        baiduMap.setMyLocationEnabled(false); // 关闭定位图层
    }

    public static void initData() {
        if (markOverlay == null) {
            markOverlay = new Overlay[length];
            popOverlay = new Overlay[length];
            latLngArray = new LatLng[length];
            windowInfo = new String[length];
        }
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

    public static void drawAnotherPointByGeo(final Context context, BaiduMap baiduMap, final LatLng latLng, final String shopName){
        baiduMaps = baiduMap;
        endLatlng = latLng;
        contexts = context;
        shopNames = shopName;
        if(markOverlay[1]!=null){
            markOverlay[1].remove();
        }
        if(popOverlay[1]!=null){
            popOverlay[1].remove();
        }
        if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
            return;
        }
        markOverlay[1] = drawMarker(baiduMap,latLng,BitmapDescriptorFactory.fromResource(R.mipmap.shop),markZIndex);
        popOverlay[1] = drawPopWindow(baiduMap,context,latLng,shopName,popZIndex);
        zoomByTwoPoint(baiduMap, latLngArray[0], latLng);

        latLngArray[1] = latLng;
        windowInfo[1] = shopName;


//        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
////                return false;
//                if (marker == markOverlay[1]){
//                    startNavi(context,latLng,shopName);
//                }
////                openBaiduMap(Double.parseDouble(positionLat), Double.parseDouble(positionLon), shopName);
//                return false;
//            }
//        });

    }



    //开启百度导航
    public static void startNavi(Context context,String address) {
//        LatLng latLng = BaiduMapUtil.getBaidulatlng();
        //百度地图,从起点是LatLng ll_location = new LatLng("你的纬度latitude","你的经度longitude");
        //终点是LatLng ll = new LatLng("你的纬度latitude","你的经度longitude");
//        NaviParaOption para = new NaviParaOption();
//        para.startPoint(latLngArray[0]);
//        para.startName("我的位置");
//        para.endPoint(latLngArray[1]);
//        para.endName(address);
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/navi?location=" + latLngArray[1].latitude + "," + latLngArray[1].longitude));
        if (isInstallPackage("com.baidu.BaiduMap")) {
            context.startActivity(intent);
//            try {
////                BaiduMapNavigation.openBaiduMapNavi(para, context);
//            } catch (BaiduMapAppNotSupportNaviException e) {
//                e.printStackTrace();
//                T.show(context,"您尚未安装百度地图或地图版本过低");
//            }
        } else {
//                LatLng ptMine = new LatLng(latLng.latitude, latLng.longitude);
//                LatLng ptPosition = new LatLng(lat, lon);
//
//                NaviParaOption para = new NaviParaOption()
//                        .startPoint(ptMine)
//                        .endPoint(ptPosition);
//                BaiduMapNavigation.openWebBaiduMapNavi(para, getContext());
            T.show(context,"手机未安装百度地图");
        }

    }

    // 判断手机是否有app
    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }




    public static Overlay drawMarker(BaiduMap baiduMap,LatLng latLng, BitmapDescriptor descriptor, int zIndex) {
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
        bitmaps.add(descriptor);
        markerOptions.position(latLng).icons(bitmaps).draggable(false);
        if (endLatlng != null && latLng == endLatlng){
            shopMarker = (Marker) baiduMap.addOverlay(markerOptions);
        }
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
        if (start == null || end == null) return 0;
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
        public LatLng latLng;  //另一个点的经纬度
        private Button sign_in_btn; //签到界面Button
        private boolean isFirst;

        public MyListener(Context context, BaiduMap baiduMap, TextView tv_distance, LatLng latLng,String mOhterTitle, Button sign_in_btn){
            initData();
            this.context = context;
            this.baiduMap = baiduMap;
            this.tv_distance = tv_distance;
            this.latLng = latLng;
            this.mAddress = mOhterTitle;
            this.sign_in_btn = sign_in_btn;
            this.isFirst = true;
        }

        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                latLng = new LatLng(latitude, longitude);
                latLngArray[0] = latLng;
                baidulatlng = latLng;
                if(isFirst){
                    if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
                        return;
                    }
//                    baiduMap.setMyLocationData(new MyLocationData.Builder()
//                            .accuracy(0)
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
////                            .direction(result.getRadius())
//                            .latitude(result.getLatitude())
//                            .longitude(result.getLongitude())
//                            .build());
                    markOverlay[0] = drawMarker(this.baiduMap,latLng,BitmapDescriptorFactory.fromResource(R.mipmap.here1),markZIndex);

                    /** 暂时隐藏pop **/
                    //popOverlay[0] = drawPopWindow(this.baiduMap,context,latLng,result.getAddrStr(),popZIndex);
//                    latLngArray[0] = latLng;
//                    windowInfo[0] = result.getAddrStr();
                    //drawOnePoint(mAddress,new MyGeoCoderListener(context,this.baiduMap));
                    //drawAnotherPointByGeo(context,this.baiduMap,this.latLng,this.mAddress);
                    zoomByOneCenterPoint(baiduMap, this.latLng, BaiduMapUtil.defaultLevel);
                    if (latLngArray[1] != null){
                        zoomByTwoPoint(baiduMap,latLngArray[0], latLngArray[1]);
                    }
                    isFirst = false;
                }else{
                    View pop = BaiduMapUtil.initPop(context,null,false);
                    TextView tv = (TextView) pop.findViewById(R.id.title);
                    tv.setText(result.getAddrStr());
                }
                if(markOverlay[1] == null){
                    tv_distance.setText("0m");
                }else{
                    double distance = BaiduMapUtil.getDistance(this.latLng, latLngArray[1]); //单位为m

                    if(sign_in_btn!=null){  //签到界面有提示框,并且改变Button样式
                        if(Math.abs(distance)<=100){  //到达(有误差)
                            tv_distance.setText(R.string.arrive_text);
                            sign_in_btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.click_btn));  //兼容api14
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


    public static class MarkerCilckListener implements BaiduMap.OnMarkerClickListener{

        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng ll = marker.getPosition();        //点击图钉的经纬度
            LatLng ls = shopMarker.getPosition();    //商户的经纬度
            if (markOverlay[1] != null && markOverlay[0] != null && ll == ls){
                final AlertDialog.Builder builder = new AlertDialog.Builder(contexts);
                builder.setTitle("温馨提示")//设置对话框标题
                        .setMessage("是否打开百度地图导航")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                startNavi(contexts,shopNames);
                            }

                        }).setNegativeButton("取消", null);//在按键响应事件中显示此对话框
                builder.show();
            }
            return false;
        }
    }

    /**
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
     if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
     return;
     }
     markOverlay[1] = BaiduMapUtil.drawMarker(this.baiduMap,result.getLocation(),BitmapDescriptorFactory.fromResource(R.mipmap.shop),markZIndex);
     popOverlay[1] = BaiduMapUtil.drawPopWindow(this.baiduMap,context,result.getLocation(),result.getAddress(),popZIndex);
     latLngArray[1] = result.getLocation();
     windowInfo[1] = result.getAddress();
     BaiduMapUtil.zoomByTwoPoint(baiduMap,latLngArray[0],latLngArray[1]);
     }

     @Override
     public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

     }
     }**/

}
