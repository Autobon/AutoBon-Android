package cn.com.incardata.autobon;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.OrderWaitAdapter;
import cn.com.incardata.adapter.OrderWaitNewAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CollectionShopEntity;
import cn.com.incardata.http.response.CollectionShop_Data;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.ListNew_Data;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;
import cn.com.incardata.view.SelectPopupWindow;


/**
 * 未被抢的单
 */
public class WaitOrderActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener {
    private PullToRefreshView refresh;
    private ListView mListView;
    //    private OrderWaitAdapter mAdapter;
    private OrderWaitNewAdapter mAdapter;
    private List<OrderInfo> orderList;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    private TextView today;
    private TextView[] tv_screen = new TextView[5];

    private List<CollectionShop_Data> collectionShopList;

    public static boolean isGetCollectionShop = false;

    private int selectTypes;
    private List<String> workItemList;
    private List<String> sequenceList;

    private LocationClient mLocationClient;                         //定位
    private LocationClientOption mLocationOption;                   //定位参数

    private MyLocationListener locationListener;                    //定位回调监听

    private boolean isFrist = true;                                 //

    Map<String, String> params = new HashMap<>();

    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "20");
        return params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order);

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGetCollectionShop) {
            isGetCollectionShop = false;
            getCollectionShopList();
        }
    }

    private void initView() {
        collectionShopList = new ArrayList<>();
        workItemList = new ArrayList<>();
        workItemList.add("隔热膜");
        workItemList.add("隐形车衣");
        workItemList.add("车身改色");
        workItemList.add("美容清洁");
        sequenceList = new ArrayList<>();
        sequenceList.add("倒序");
        sequenceList.add("正序");
        today = (TextView) findViewById(R.id.today);
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.wait_order_list);

        tv_screen[0] = (TextView) findViewById(R.id.tv_all);
        tv_screen[1] = (TextView) findViewById(R.id.tv_work_item);
        tv_screen[2] = (TextView) findViewById(R.id.tv_distance);
        tv_screen[3] = (TextView) findViewById(R.id.tv_work_time);
        tv_screen[4] = (TextView) findViewById(R.id.tv_sequence);

        orderList = new ArrayList<>();
        mAdapter = new OrderWaitNewAdapter(this, orderList, collectionShopList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WaitOrderInfoActivity.class);
                intent.putExtra(AutoCon.ORDER_INFO, orderList.get(position));
                startActivityForResult(intent, 0x10);
            }
        });

        refresh.setOnHeaderRefreshListener(this);
        refresh.setOnFooterRefreshListener(this);

        today.setText(DateCompute.getWeekOfDate());
        getCollectionShopList();
        params.put("order",String.valueOf(1));
        loadData(getParams());

        startLocation();

        mAdapter.setOnClickOrderListener(new OrderWaitNewAdapter.OnClickOrderListener() {
            @Override
            public void onClickOrder(int position) {
                immediateOrder(orderList.get(position).getId());
            }
        });


        findViewById(R.id.back).setOnClickListener(this);
        for (TextView textView : tv_screen) {
            textView.setOnClickListener(this);
        }
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        locationListener = new MyLocationListener();
        //初始化定位
        mLocationClient = new LocationClient(this);
        //设置定位回调监听
        mLocationClient.registerLocationListener(locationListener);
        //初始化定位参数
        mLocationOption = new LocationClientOption();
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        // LocationMode.Hight_Accuracy：高精度；
        // LocationMode. Battery_Saving：低功耗；
        // LocationMode. Device_Sensors：仅使用设备；

        mLocationOption.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        // gcj02：国测局坐标；
        // bd09ll：百度经纬度坐标；
        // bd09：百度墨卡托坐标；
        // 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        mLocationOption.setScanSpan(0);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        // 如果设置为0，则代表单次定位，即仅定位一次，默认为0
        // 如果设置非0，需设置1000ms以上才有效
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setOpenGps(true);
        mLocationOption.setNeedDeviceDirect(true);
        //可选，设置是否使用gps，默认false
        // 使用高精度和仅用设备两种定位模式的，参数必须设置为true
        mLocationOption.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mLocationOption.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        // 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        mLocationOption.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationOption.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的位置信息，此处必须为true
        //可选，7.2版本新增能力
        // 如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        //设置定位参数
        mLocationClient.setLocOption(mLocationOption);
        mLocationClient.start();
    }


    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocationClient.stop();
            if (bdLocation != null) {
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                if (isFrist){
                    mAdapter.setTechLocation(latLng);
                    mAdapter.notifyDataSetChanged();
                    isFrist = false;
                    return;
                }
                mAdapter.setTechLocation(latLng);
                params.put("orderType",String.valueOf(2));
                params.put("latitude",String.valueOf(bdLocation.getLatitude()));
                params.put("longitude",String.valueOf(bdLocation.getLongitude()));
                page = 1;
                isRefresh = true;
                loadData(getParams());
//                double distance = BaiduMapUtil.getDistance(latLng, mLatLng); //单位为m
                //改为提示，不需要显示距离
//                    if(distance>=1000){  //距离大于等于1公里
//                        distance = DecimalUtil.DoubleDecimal1(distance/1000);  //保留一位小数
//                        tv_distance.setText(String.valueOf(distance)+"km");
//                    }else{  //距离小于1公里
//                        distance = DecimalUtil.DoubleDecimal1(distance); //保留一位小数
//                        tv_distance.setText(String.valueOf(distance)+"m");
//                    }
            } else {
                //定位失败
                T.show(getContext(), "定位失败，请检查您的网络、\nGPS或定位权限是否开启");
            }
        }

//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            mLocationClient.stop();
//            if (bdLocation != null) {
//                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
//                        bdLocation.getLocType() == BDLocation.TypeNetWorkLocation ||
//                        bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
//                    Log.e("address", bdLocation.getAddress().address);
//                    latitude = bdLocation.getLatitude();
//                    longitude = bdLocation.getLongitude();
//                    if (siteOrder == 0) {
//                        uploadSite(null);
//                    } else {
//                        if (data.getDispatchType() == 0) {//重载
//                            if (data.getTaskDetails().get(siteOrder).getDeliverDetails() != null && data.getTaskDetails().get(siteOrder).getDeliverDetails().size() > 0){
//                                showPopupWindow(siteOrder, data.getTaskDetails().get(siteOrder).getDeliverDetails());
//                            }else {
//                                uploadSite(null);
//                            }
//                        } else {//空载
//                            uploadSite(null);
//                        }
//
//                    }
//                } else {
//                    String errText = "定位失败," + bdLocation.getLocType() + ": " + bdLocation.getLocTypeDescription();
//                    Log.e("AmapErr", errText);
//                    T.show(context,errText);
//                }
//            }
//        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_all:
                updateSelectTypeStyle(0);
                if (params.get("workType") != null || params.containsKey("workType")) {
                    params.remove("workType");
                }
                page = 1;
                isRefresh = true;
                loadData(getParams());
                break;
            case R.id.tv_work_item:
                updateSelectTypeStyle(1);
                showPopupWindow(1);
                break;
            case R.id.tv_distance:
                updateSelectTypeStyle(2);
                startLocation();
                break;
            case R.id.tv_work_time:
                updateSelectTypeStyle(3);
                if (params.get("latitude") != null || params.containsKey("latitude")) {
                    params.remove("latitude");
                }
                if (params.get("longitude") != null || params.containsKey("longitude")) {
                    params.remove("longitude");
                }
                params.put("orderType",String.valueOf(1));
                page = 1;
                isRefresh = true;
                loadData(getParams());
                break;
            case R.id.tv_sequence:
                updateSelectTypeStyle(4);
                showPopupWindow(2);
                break;
        }
    }

    SelectPopupWindow pop;

    /**
     * 弹框
     *
     * @param selectType
     */
    public void showPopupWindow(int selectType) {
        if (pop == null) {
            pop = new SelectPopupWindow(this);
            pop.init();
            pop.setListener(listener);
        }
        selectTypes = selectType;
        switch (selectType) {
            case 1:
                pop.setData(workItemList);
                break;
            case 2:
                pop.setData(sequenceList);
                break;
        }
        pop.showAtLocation(findViewById(R.id.ll_select), Gravity.BOTTOM, 0, 0);
    }

    public SelectPopupWindow.OnCheckedListener listener = new SelectPopupWindow.OnCheckedListener() {
        @Override
        public void onChecked(int index) {
            switch (selectTypes) {
                case 1:
                    if (index == -1){
                        return;
                    }else {
                        switch (index){
                            case 0:
                                params.put("workType",String.valueOf(1));
                                break;
                            case 1:
                                params.put("workType",String.valueOf(2));
                                break;
                            case 2:
                                params.put("workType",String.valueOf(3));
                                break;
                            case 3:
                                params.put("workType",String.valueOf(4));
                                break;
                        }
                        page = 1;
                        isRefresh = true;
                        loadData(getParams());
                    }
                    break;
                case 2:
                    if (index == -1){
                        return;
                    }else {
                        switch (index){
                            case 0:
                                params.put("order",String.valueOf(1));
                                break;
                            case 1:
                                params.put("order",String.valueOf(2));
                                break;
                        }
                        page = 1;
                        isRefresh = true;
                        loadData(getParams());
                    }
                    break;
            }
        }
    };

    /**
     * 抢单
     */
    private void immediateOrder(int orderId) {
        showDialog(getString(R.string.order_receiving_process));
        Http.getInstance().postTaskToken(NetURL.TAKEUPV2, TakeupEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.immediate_order_failed);
                    return;
                }
                if (entity instanceof TakeupEntity) {
                    TakeupEntity takeup = (TakeupEntity) entity;
                    if (takeup.isStatus()) {
                        OrderInfo orderInfo = JSON.parseObject(takeup.getMessage().toString(), OrderInfo.class);
                        MyApplication.isRefresh = true;
                        page = 1;
                        isRefresh = true;
                        loadData(getParams());
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, orderInfo.getId());
                        bundle.putString("OrderNum", orderInfo.getOrderNum());
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        return;
                    } else {
                        T.show(getContext(), takeup.getMessage().toString());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)));
    }

    /**
     * 获取收藏技师列表
     */
    private void getCollectionShopList() {
        Http.getInstance().getTaskToken(NetURL.YETCOLLECTIONSHOP, "page=1&pageSize=200", CollectionShopEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof CollectionShopEntity) {
                    if (collectionShopList != null && collectionShopList.size() > 0) {
                        collectionShopList.clear();
                    }
                    CollectionShopEntity collectionShopEntity = (CollectionShopEntity) entity;
                    collectionShopList.addAll(collectionShopEntity.getList());
                    mAdapter.notifyDataSetInvalidated();
                }
            }
        });
    }

    /**
     * 获取可抢订单列表
     *
     * @param param
     */
    private void loadData(Map<String, String> param) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        Http.getInstance().getTaskToken(NetURL.LIST_NEWV2, ListNewEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof ListNewEntity) {
                    ListNewEntity listNew = (ListNewEntity) entity;
                    Log.e("waitOrder",listNew.getMessage().toString());
                    if (listNew.isStatus()) {
                        ListNew_Data listNew_data = JSON.parseObject(listNew.getMessage().toString(), ListNew_Data.class);
                        totalPages = listNew_data.getTotalPages();
                        if (isRefresh) {
                            orderList.clear();
                        }
                        if (listNew_data.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_order));
                        }
                        orderList.addAll(listNew_data.getList());
                        mAdapter.notifyDataSetInvalidated();
                    } else {
                        T.show(getContext(), listNew.getMessage().toString());
                        return;
                    }
                    isRefresh = false;
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (page >= totalPages) {
            T.show(getContext(), R.string.has_load_all_label);
            refresh.loadedCompleted();
            return;
        }
        ++page;
        isRefresh = false;
        loadData(getParams());
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        loadData(getParams());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x10 & resultCode == 0x11) {
            page = 1;
            isRefresh = true;
            loadData(getParams());
        }
    }


    /**
     * 筛选条件点击后的样式修改
     *
     * @param chedkId 被点击项下标
     */
    public void updateSelectTypeStyle(int chedkId) {

        for (int i = 0; i < tv_screen.length; i++) {
            if (i == chedkId) {
                tv_screen[i].setTextSize(15);
                tv_screen[i].setTextColor(getResources().getColor(R.color.main_orange));
                tv_screen[i].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                tv_screen[i].setTextSize(13);
                tv_screen[i].setTextColor(getResources().getColor(R.color.darkgray));
                tv_screen[i].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

}
