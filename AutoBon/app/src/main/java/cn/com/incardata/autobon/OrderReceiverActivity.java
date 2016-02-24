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
 * 接单开始工作
 * Created by Administrator on 2016/2/17.
 */
public class OrderReceiverActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private TextView tv_distance,tv_add_contact;

    private LinearLayout ll_add_contact,ll_tab_bottom;
    private TextView tv_username,tv_begin_work;
    private View bt_line_view;
    private ImageView iv_back;

    protected BaiduMap baiduMap;
    protected MapView mMapView;

    private static final String mAddress = "武汉光谷广场";  //测试地址,可以更改
    private static final int ADD_CONTACT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaiduMapUtil.registerBaiduMapReceiver(this);  //注册百度地图广播接收者
        setContentView(R.layout.order_receiver_activity);
        initView();
        BaiduMapUtil.initData();
        setListener();
    }

    public void initView(){
        context = this;
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_add_contact = (TextView) findViewById(R.id.tv_add_contact);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_begin_work = (TextView)findViewById(R.id.tv_begin_work);
        ll_add_contact = (LinearLayout) findViewById(R.id.ll_add_contact);
        ll_tab_bottom = (LinearLayout) findViewById(R.id.ll_tab_bottom);
        bt_line_view = findViewById(R.id.bt_line_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);


        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别

        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
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
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置
                BaiduMapUtil.locate(context,baiduMap,new LocationClient(context),
                        new BaiduMapUtil.MyListener(context,baiduMap,tv_distance,mAddress));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add_contact:
                Intent intent = new Intent(this,AddContactActivity.class);
                startActivityForResult(intent,ADD_CONTACT_CODE);
                break;
            case R.id.iv_back:
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
        BaiduMapUtil.unRegisterBaiduMapReceiver(this);
        baiduMap.clear();
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
