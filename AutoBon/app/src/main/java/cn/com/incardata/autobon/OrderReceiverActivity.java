package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.StringUtil;

/**
 * 接单开始工作(弃用)
 * Created by Administrator on 2016/2/17.
 */
public class OrderReceiverActivity extends BaseBaiduMapActivity implements View.OnClickListener{
    private Context context;
    private TextView tv_add_contact;

    private LinearLayout ll_add_contact,ll_tab_bottom;
    private TextView tv_username,tv_begin_work;
    private View bt_line_view;
    private ImageView iv_back;

    private static final int ADD_CONTACT_CODE = 1;  //添加联系人的请求码requestCode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_receiver_activity);
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

    public void initView(){
        context = this;
        super.tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_add_contact = (TextView) findViewById(R.id.tv_add_contact);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_begin_work = (TextView)findViewById(R.id.tv_begin_work);
        ll_add_contact = (LinearLayout) findViewById(R.id.ll_add_contact);
        ll_tab_bottom = (LinearLayout) findViewById(R.id.ll_tab_bottom);
        bt_line_view = findViewById(R.id.bt_line_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    public void setListener(){
        tv_add_contact.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置,null代表不是签到界面
                BaiduMapUtil.locate(baiduMap, scanTime, mLocationClient,
                        new BaiduMapUtil.MyListener(context, baiduMap,tv_distance,mLatLng, mAddress, null));
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_add_contact:
                intent = new Intent(this,AddContactActivity.class);
                startActivityForResult(intent,ADD_CONTACT_CODE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_begin_work:
                intent = new Intent(this,WorkSignInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CONTACT_CODE){  //添加联系人返回,更新界面
            switch (resultCode){
                case RESULT_OK:
                    String username = data.getExtras().getString("username");
                    if(StringUtil.isNotEmpty(username)){
                        tv_username.setText(username);
                        bt_line_view.setVisibility(View.VISIBLE);
                        ll_add_contact.setVisibility(View.VISIBLE);
                        ll_tab_bottom.setVisibility(View.GONE);
                        tv_begin_work.setVisibility(View.VISIBLE);
                        tv_begin_work.setOnClickListener(this);
                    }
                    break;
                default:
                    break;
            }
        }
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
