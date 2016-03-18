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

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.fragment.BaiduMapFragment;
import cn.com.incardata.getui.InvitationMsg;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.InvitationEntity;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;

/**
 * 被邀请确认
 */
public class InvitationActivity extends BaseActivity implements View.OnClickListener {

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
    private TextView distance;
    private ImageView indentImage;
    private TextView indentText;
    private TextView workTime;
    private TextView workNotes;
    private TextView mainTech;

    private OrderInfo_Data orderInfo;
    private InvitationMsg invitation;
    private int orderId;

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
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        invitation = getIntent().getParcelableExtra("INVITATION");

        mainTech = (TextView) findViewById(R.id.main_tech);
        mMapView = (MapView) findViewById(R.id.bdmapView);
        distance = (TextView) findViewById(R.id.distance);
        indentImage = (ImageView) findViewById(R.id.indent_image);
        indentText = (TextView) findViewById(R.id.indent_text);
        workTime = (TextView) findViewById(R.id.work_time);
        workNotes = (TextView) findViewById(R.id.work_notes);

        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别

        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺

        BaiduMapUtil.initData();
        setListener();

        if (orderInfo != null){
            orderId = orderInfo.getId();
            mainTech.setText(orderInfo.getMainTech().getName());
            setData(orderInfo.getPositionLon(), orderInfo.getPositionLat(), orderInfo.getPhoto(), orderInfo.getOrderTime(), orderInfo.getRemark(), orderInfo.getCreatorName());
        }else if (invitation != null){
            orderId = invitation.getOrder().getId();
            mainTech.setText(invitation.getPartner().getName());
            setData(invitation.getOrder().getPositionLon(),
                    invitation.getOrder().getPositionLat(),
                    invitation.getOrder().getPhoto(),
                    invitation.getOrder().getOrderTime(),
                    invitation.getOrder().getRemark(),
                    invitation.getOrder().getCreatorName());
        }else {
            T.show(this , R.string.loading_data_failure);
            return;
        }
    }

    private void setBaseData(){
        if (!TextUtils.isEmpty(photoUrl)){
            ImageLoaderCache.getInstance().loader(photoUrl, indentImage, false, R.mipmap.load_image_failed);
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

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.reject).setOnClickListener(this);
        findViewById(R.id.accept).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.reject:
                postInvitation(false);
                break;
            case R.id.accept:
                postInvitation(true);
                break;
        }
    }

   private void postInvitation(final boolean accepted){
       Http.getInstance().postTaskToken(NetURL.getInvitation(orderId), InvitationEntity.class, new OnResult() {
           @Override
           public void onResult(Object entity) {
                if (entity == null){
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
               if (entity instanceof InvitationEntity){
                   if (((InvitationEntity) entity).isResult()){
                       if (accepted){
                           T.show(getContext(), R.string.accepted);
                       }else {
                           T.show(getContext(), R.string.rejected);
                       }
                       finish();
                   }else {
                       T.show(getContext(), ((InvitationEntity) entity).getMessage());
                   }
               }
           }
       }, new BasicNameValuePair("accepted", String.valueOf(accepted)));
    }
}
