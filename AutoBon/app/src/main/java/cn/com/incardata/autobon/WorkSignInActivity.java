package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.ReportLocationEntity;
import cn.com.incardata.http.response.SignInEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/2/22.
 * 工作签到
 */
public class WorkSignInActivity extends BaseBaiduMapActivity implements View.OnClickListener{
    private static final int SIGN = 500000000;//允许签到距离m
    private TextView tv_day;
    private Context context;
    private Button sign_in_btn;
    private ImageView iv_my_info;
    private BDLocationListener myBDLocationListener;

    protected static View pop;
    protected static Overlay[] markOverlay;  //标志物图层
    protected static Overlay[] popOverlay;  //信息框图层
    protected static LatLng[] latLngArray;  //位置信息记录
    protected static String[] windowInfo;  //窗体信息记录

    private OrderInfo_Data orderInfo;
    private boolean isSign = false;//是否可以签到

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_map_address);
        initBaiduMapView();
        initView();
        initData();
        setListener();
        //data
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        try {
            mLatLng = new LatLng(Double.parseDouble(orderInfo.getPositionLat()), Double.parseDouble(orderInfo.getPositionLon()));
            mAddress = orderInfo.getCreatorName();
        }catch(NumberFormatException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("test","再次启动定位图层......");
        if(mLocationClient!=null){
            mLocationClient.start();
            baiduMap.setMyLocationEnabled(true);
        }
    }

    protected void initBaiduMapView(){
        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别
        mLocationClient = new LocationClient(getApplicationContext());

        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
    }

    private void initView(){
        context = this;
        tv_day = (TextView) findViewById(R.id.tv_day);
//        super.tv_distance = (TextView) findViewById(R.id.tv_distance);
        sign_in_btn = (Button) findViewById(R.id.sign_in_btn);
        iv_my_info = (ImageView) findViewById(R.id.iv_my_info);

        tv_day.setText(DateCompute.getWeekOfDate());
    }

    private void initData(){
        markOverlay = new Overlay[4];
        popOverlay = new Overlay[4];
        latLngArray = new LatLng[4];
        windowInfo = new String[4];
    }

    public void setListener(){
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                registerMyLocation();
                //BaiduMapUtil.closeLocationClient(baiduMap,mLocationClient);
                //myBDLocationListener = new BaiduMapUtil.MyListener(context,baiduMap,tv_distance,mLatLng,mAddress,sign_in_btn);
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置,定位扫描时间为5s,true代表是签到界面
                //BaiduMapUtil.locate(baiduMap,scanTime, mLocationClient,myBDLocationListener);
            }
        });
        iv_my_info.setOnClickListener(this);
        sign_in_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_my_info:
                Intent intent = new Intent(this,MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_btn:
                signIn();
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mLocationClient!=null){
            mLocationClient.stop();
            baiduMap.setMyLocationEnabled(false); //关闭定位图层
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myBDLocationListener!=null){
            mLocationClient.unRegisterLocationListener(myBDLocationListener);
            myBDLocationListener = null;
        }
        mLocationClient = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * 签到
     */
    public void signIn(){
        if (!isSign){
            T.show(this, R.string.arrival_to_sign);
            return;
        }

        List<BasicNameValuePair> bvList = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair bv_one = new BasicNameValuePair("positionLon",String.valueOf(latLngArray[0].longitude)); //经度
        BasicNameValuePair bv_two = new BasicNameValuePair("positionLat",String.valueOf(latLngArray[0].latitude)); //纬度
        BasicNameValuePair bv_three = new BasicNameValuePair("orderId",String.valueOf(orderInfo.getId()));  //订单id

        bvList.add(bv_one);
        bvList.add(bv_two);
        bvList.add(bv_three);

        Http.getInstance().postTaskToken(NetURL.SIGN_IN_URL, SignInEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.sign_in_failed));
                    return;
                }
                SignInEntity signInEntity = (SignInEntity) entity;
                if(signInEntity.isResult()){
                    if (orderInfo.getMainTech().getId() == MyApplication.getInstance().getUserId()){
                        orderInfo.getMainConstruct().setSigninTime(System.currentTimeMillis());
                    }else {
                        orderInfo.getSecondConstruct().setSigninTime(System.currentTimeMillis());
                    }
                    Intent intent = new Intent(getContext(), WorkBeforeActivity.class);
                    intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
                    startActivity(intent);
                    finish();
                }else{
                    T.show(context,signInEntity.getMessage());
                }
            }
        },(BasicNameValuePair[])bvList.toArray(new BasicNameValuePair[bvList.size()]));
    }


    public void registerMyLocation(){
        myBDLocationListener = new MyBDLocationListener();
        mLocationClient.registerLocationListener(myBDLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        baiduMap.setMyLocationEnabled(true);// 打开定位图层
        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true,
                BitmapDescriptorFactory.fromResource(R.drawable.here));
        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
    }

    private void drawAnotherPointByGeo(Context context,BaiduMap baiduMap,LatLng latLng,String mAddress){
        if(markOverlay[1]!=null){
            markOverlay[1].remove();
        }
        if(popOverlay[1]!=null){
            popOverlay[1].remove();
        }
        if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
            return;
        }
        markOverlay[1] =BaiduMapUtil.drawMarker(baiduMap,latLng,BitmapDescriptorFactory.fromResource(R.mipmap.shop),BaiduMapUtil.markZIndex);
        popOverlay[1] = BaiduMapUtil.drawPopWindow(baiduMap,context,latLng,mAddress,BaiduMapUtil.popZIndex);
        latLngArray[1] = latLng;
        windowInfo[1] = mAddress;
    }

    class MyBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);

                if(baiduMap != null){
                    baiduMap.setMyLocationData(new MyLocationData.Builder()
                            .accuracy(0)
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(result.getRadius())
                            .latitude(result.getLatitude())
                            .longitude(result.getLongitude())
                            .build());
//                    markOverlay[0] =BaiduMapUtil.drawMarker(baiduMap,latLng, BitmapDescriptorFactory.fromResource(R.mipmap.here),BaiduMapUtil.markZIndex);
                    /** 暂时隐藏pop **/
                    //popOverlay[0] = BaiduMapUtil.drawPopWindow(baiduMap,context,latLng,result.getAddrStr(),BaiduMapUtil.popZIndex);
                    latLngArray[0] = latLng;
                    windowInfo[0] = result.getAddrStr();
                    if (markOverlay[1] == null){
                        drawAnotherPointByGeo(context,baiduMap,mLatLng,mAddress);
                        BaiduMapUtil.zoomByTwoPoint(baiduMap,latLng,mLatLng);
                    }
                }
                if(markOverlay[1] == null){
//                    tv_distance.setText("0m");
                }else{
                    double distance = BaiduMapUtil.getDistance(latLngArray[0],mLatLng); //单位为m
                    if(sign_in_btn!=null){  //签到界面有提示框,并且改变Button样式
                        if(Math.abs(distance)<=SIGN){  //到达(有误差)
//                            tv_distance.setText(R.string.arrive_text);
                            sign_in_btn.setBackgroundResource(R.drawable.default_btn);
                            sign_in_btn.setTextColor(context.getResources().getColor(android.R.color.white));
                            int padding =  getResources().getDimensionPixelSize(R.dimen.dp10);
                            sign_in_btn.setPadding(0,padding,0,padding);
                            isSign = true;
                        }else{
                            //Toast toast = Toast.makeText(context.getApplicationContext(),context.getString(R.string.not_arrive_text),Toast.LENGTH_LONG);
                            //toast.setGravity(Gravity.CENTER,0,0);
                            //toast.show();
                        }
                    }
                    //改为提示，不需要显示距离
//                    if(distance>=1000){  //距离大于等于1公里
//                        distance = DecimalUtil.DoubleDecimal1(distance/1000);  //保留一位小数
//                        tv_distance.setText(String.valueOf(distance)+"km");
//                    }else{  //距离小于1公里
//                        distance = DecimalUtil.DoubleDecimal1(distance); //保留一位小数
//                        tv_distance.setText(String.valueOf(distance)+"m");
//                    }
                }
            }else {
                //定位失败
                T.show(getContext(), "定位失败，请检查您的网络、\nGPS或定位权限是否开启");
            }
        }
    }

    /**
     * 上传实时位置,每五分钟上传一次
     * @param mLatlng
     */
    private void reportLocation(final LatLng mLatlng){
        BasicNameValuePair bv_one = new BasicNameValuePair("rtpostionLon",String.valueOf(mLatlng.longitude)); //经度
        BasicNameValuePair bv_two = new BasicNameValuePair("rtpositionLat",String.valueOf(mLatlng.latitude)); //纬度
        Http.getInstance().postTaskToken(NetURL.REPORT_MY_ADDRESS, ReportLocationEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    return;
                }
                ReportLocationEntity reportLocationEntity = (ReportLocationEntity) entity;
                if(reportLocationEntity.isResult()){
                    Log.i("test","上传数据===>"+mLatlng.longitude+","+mLatlng.latitude);
                    return;
                }
            }
        },bv_one,bv_two);
    }
}
