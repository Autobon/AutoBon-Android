package cn.com.incardata.autobon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.fragment.BaiduMapFragment;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;

/**
 * 被邀请确认
 */
public class InvitationActivity extends BaseActivity {

    private final static String TAG = "InvitationActivity";
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;

    private String positionLon;
    private String positionLat;
    private String workTimeStr;
    private String photoUrl;
    private String remark;
    private String shopName;

    private BDLocationListener myBDLocationListener;
    private View rootView;
    private TextView distance;
    private ImageView indentImage;
    private TextView indentText;
    private TextView workTime;
    private TextView workNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        mLocationClient = new LocationClient(getApplicationContext());
        initViews();
    }

    public void setData(Order order){
        if (order == null) return;
        positionLon = order.getPositionLon();
        positionLat = order.getPositionLat();
        photoUrl = order.getPhoto();
        workTimeStr = DateCompute.getDate(order.getOrderTime());
        remark = order.getRemark();
        shopName = order.getCreatorName();
        setBaseData();
    }

    public void setData(String positionLon, String positionLat, String photoUrl, long orderTime, String remark, String creatorName){
        this.positionLon = positionLon;
        this.positionLat = positionLat;
        this.photoUrl = photoUrl;
        this.workTimeStr = DateCompute.getDate(orderTime);
        this.remark = remark;
        this.shopName = creatorName;
        setBaseData();
    }

    @Override
    public void onStart() {
        super.onStart();
        initNetManager();  //网络状态切换监听
    }

    private void initViews() {
        mMapView = (MapView) rootView.findViewById(R.id.bdmapView);
        distance = (TextView) rootView.findViewById(R.id.distance);
        indentImage = (ImageView) rootView.findViewById(R.id.indent_image);
        indentText = (TextView) rootView.findViewById(R.id.indent_text);
        workTime = (TextView) rootView.findViewById(R.id.work_time);
        workNotes = (TextView) rootView.findViewById(R.id.work_notes);

        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别

        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺

        BaiduMapUtil.initData();
        setBaseData();
        setListener();
    }

    private void setBaseData(){
        if (!TextUtils.isEmpty(photoUrl)){
            ImageLoaderCache.getInstance().loader(photoUrl, indentImage, false);
            indentText.setVisibility(View.GONE);
        }
        if (workTime != null){
            workTime.setText(workTimeStr);
        }
        if (workNotes != null){
            workNotes.setText(remark);
        }
        try {
            BaiduMapUtil.drawAnotherPointByGeo(this, baiduMap, new LatLng(Double.parseDouble(positionLat), Double.parseDouble(positionLon)), shopName);
        }catch (NumberFormatException e){
            L.d(TAG, "NumberFormatException");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void setListener() {
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myBDLocationListener = new BaiduMapUtil.MyListener(getContext(), baiduMap,distance, null, "4S店", null);
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置
                BaiduMapUtil.locate(baiduMap, BaiduMapFragment.scanTime, new LocationClient(getContext()),myBDLocationListener);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mReceiver!=null){
           unregisterReceiver(mReceiver);  //注销网络监听的广播
        }
    }


    @Override
    public void onDestroy() {
        if(myBDLocationListener!=null){
            mLocationClient.unRegisterLocationListener(myBDLocationListener); //销毁定位广播
            myBDLocationListener = null;
        }
        BaiduMapUtil.closeLocationClient(baiduMap,mLocationClient);  //关闭定位
        mReceiver = null;
        super.onDestroy();
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
                    //BaiduMapUtil.locate(baiduMap,scanTime,mLocationClient,
                    //      new BaiduMapUtil.MyListener(context,baiduMap,distance,mLatLng,mAddress,null));
                    BaiduMapUtil.locate(baiduMap);
                }else{
                    T.show(context,context.getString(R.string.no_network_error));
                }
            }
        }
    };
}
