package cn.com.incardata.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.io.File;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.EnlargementActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfo_Cooperator;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;


/**
 * <p>地图与订单图片<p/>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndentMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndentMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndentMapFragment extends BaiduMapFragment{
    private final static String TAG = "IndentMapFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String positionLon;
    private String positionLat;
    private String workTimeStr;
    private String agreeEndTime;
    private String createOrderTime;
    private String photoUrl;
    private String remark;
    private String shopName;//地图标记

    private String orderNumber_str;//订单类型
    private String orderType_str;//订单类型
    private String orderOwner_str;//下单人
    private String contact_phone_str;//联系电话
    private String shopsLocation_str;//商户位置
    private String shopsAlias_str;//商户名称

    private OnFragmentInteractionListener mListener;
    private BDLocationListener myBDLocationListener;
    private BaiduMap.OnMarkerClickListener markerClickListener;
    private View rootView;
    private TextView order_number;
    private TextView distance;
//    private ImageView indentImage;
    private TextView indentText;
    private TextView workTime;
    private TextView agree_end_time;
    private TextView create_time;
    private TextView orderType;
    private TextView orderOwner;
    private TextView contact_phone;
    private ImageView img_phone;
    private TextView shopsLocation;
    private TextView shopsAlias;
    private TextView workNotes;
    private GridView order_grid;
    private RelativeLayout rl1;
    private View v1;

    private boolean isClick = true;

    public IndentMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IndentMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndentMapFragment newInstance(boolean isClick) {
        IndentMapFragment fragment = new IndentMapFragment();
        Bundle args = new Bundle();
        args.putBoolean("isClick", isClick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.isClick = getArguments().getBoolean("isClick");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_indent_map, container, false);
            initViews();
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public void setData(Order order, OrderInfo_Cooperator cooperator){
        if (order != null) {
            positionLon = order.getPositionLon();
            positionLat = order.getPositionLat();
            photoUrl = order.getPhoto();
            workTimeStr = DateCompute.getDate(order.getOrderTime());

            remark = order.getRemark();
            shopName = order.getCreatorName();

            orderNumber_str = order.getOrderNum();
        }

        if (cooperator != null) {
            orderType_str = MyApplication.getInstance().getSkill(order.getOrderType());
            orderOwner_str = cooperator.getCorporationName();
            contact_phone_str = cooperator.getContactPhone();
            shopsLocation_str = cooperator.getAddress();
            shopsAlias_str = cooperator.getFullname();
        }

//        setBaseData();

//        Bundle bundle = new Bundle();
//        bundle.putString("PositionLon", positionLon);
//        bundle.putString("PositionLat", positionLat);
//        bundle.putString("Photo", photoUrl);
//        bundle.putString("OrderTime", workTimeStr);
//        bundle.putString("Remark", remark);
//        this.setArguments(bundle);
    }

    public void setData(OrderInfo orderInfo){
        if (orderInfo == null) return;
        this.positionLon = orderInfo.getLongitude();
        this.positionLat = orderInfo.getLatitude();
        this.photoUrl = orderInfo.getPhoto();
        this.workTimeStr = DateCompute.getDate(orderInfo.getAgreedStartTime());
        this.agreeEndTime = DateCompute.getDate(orderInfo.getAgreedEndTime());
        this.createOrderTime = DateCompute.getDate(orderInfo.getCreateTime());
        this.remark = orderInfo.getRemark();
        this.shopName = orderInfo.getCoopName();
        this.orderNumber_str = orderInfo.getOrderNum();

        orderType_str = "";
        String[] types = (orderInfo.getType()).split(",");
        for (int j = 0; j < types.length; j++ ){
            orderType_str = orderType_str + getProject(types[j]) + ",";
        }
        orderType_str = orderType_str.substring(0,orderType_str.length() - 1);
        if (orderInfo.getCreatorName() == null){
            orderOwner_str = "";
        }else {
            orderOwner_str = orderInfo.getCreatorName();
        }
        if (orderInfo.getContactPhone() == null){
            contact_phone_str = "";
        }else {
            contact_phone_str = orderInfo.getContactPhone();
        }

        shopsLocation_str = orderInfo.getAddress();
        shopsAlias_str = orderInfo.getCoopName();
        setBaseData();
    }

    public String getProject(String type){
        if ("1".equals(type)){
            return "隔热膜";
        }else if ("2".equals(type)){
            return "隐形车衣";
        }else if ("3".equals(type)){
            return "车身改色";
        }else if ("4".equals(type)){
            return "美容清洁";
        }else
            return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        initNetManager(getActivity());  //网络状态切换监听
    }

    private void initViews() {
        v1 = rootView.findViewById(R.id.v1);
        rl1 = (RelativeLayout) rootView.findViewById(R.id.rl1);
        mMapView = (MapView) rootView.findViewById(R.id.bdmapView);
        order_number = (TextView) rootView.findViewById(R.id.order_number);
        distance = (TextView) rootView.findViewById(R.id.distance);
//        indentImage = (ImageView) rootView.findViewById(R.id.indent_image);
        indentText = (TextView) rootView.findViewById(R.id.indent_text);
        workTime = (TextView) rootView.findViewById(R.id.work_time);
        agree_end_time = (TextView) rootView.findViewById(R.id.agree_end_time);
        create_time = (TextView) rootView.findViewById(R.id.create_time);
        orderType = (TextView) rootView.findViewById(R.id.order_type);
        orderOwner = (TextView) rootView.findViewById(R.id.create_order_people);
        contact_phone = (TextView) rootView.findViewById(R.id.contact_phone);
        img_phone = (ImageView) rootView.findViewById(R.id.img_phone);
        shopsLocation = (TextView) rootView.findViewById(R.id.shops_location);
        workNotes = (TextView) rootView.findViewById(R.id.work_notes);
        shopsAlias = (TextView) rootView.findViewById(R.id.shops_name);
        order_grid = (GridView) rootView.findViewById(R.id.order_grid);
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别

//        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
////                return false;
//                startNavi(new LatLng(Double.parseDouble(positionLat), Double.parseDouble(positionLon)),shopsLocation_str);
////                openBaiduMap(Double.parseDouble(positionLat), Double.parseDouble(positionLon), shopName);
//                return false;
//            }
//        });



        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺

        BaiduMapUtil.initData();
        setBaseData();
        setListener();
    }

    /**
     * 打开百度地图导航功能
     * @param lon
     * @param lat
     * @param describle
     */
    private void openBaiduMap(double lon, double lat, String describle) {
        LatLng latLng = BaiduMapUtil.getBaidulatlng();
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(latLng.latitude);
            loc.append(",");
            loc.append(latLng.longitude);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            if (isInstallPackage("com.baidu.BaiduMap")) {
                startActivity(intent); //启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
//                LatLng ptMine = new LatLng(latLng.latitude, latLng.longitude);
//                LatLng ptPosition = new LatLng(lat, lon);
//
//                NaviParaOption para = new NaviParaOption()
//                        .startPoint(ptMine)
//                        .endPoint(ptPosition);
//                BaiduMapNavigation.openWebBaiduMapNavi(para, getContext());
                T.show(getContext(),"手机未安装百度地图");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开启百度导航
    public void startNavi(LatLng endLatLng,String address) {
        LatLng latLng = BaiduMapUtil.getBaidulatlng();
        //百度地图,从起点是LatLng ll_location = new LatLng("你的纬度latitude","你的经度longitude");
        //终点是LatLng ll = new LatLng("你的纬度latitude","你的经度longitude");
        NaviParaOption para = new NaviParaOption();
        para.startPoint(latLng);
        para.startName("我的位置");
        para.endPoint(endLatLng);
        para.endName(address);
        if (isInstallPackage("com.baidu.BaiduMap")) {
            try {
                BaiduMapNavigation.openBaiduMapNavi(para, getContext());
            } catch (BaiduMapAppNotSupportNaviException e) {
                e.printStackTrace();
                T.show(getContext(),"您尚未安装百度地图或地图版本过低");
            }
        } else {
//                LatLng ptMine = new LatLng(latLng.latitude, latLng.longitude);
//                LatLng ptPosition = new LatLng(lat, lon);
//
//                NaviParaOption para = new NaviParaOption()
//                        .startPoint(ptMine)
//                        .endPoint(ptPosition);
//                BaiduMapNavigation.openWebBaiduMapNavi(para, getContext());
            T.show(getContext(),"手机未安装百度地图");
        }

    }

    // 判断手机是否有app
    private boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void setBaseData(){
        if (workTime == null) return;
//        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + photoUrl, indentImage, R.mipmap.load_image_failed);
//        indentText.setVisibility(View.GONE);

        if (workTime != null){
            workTime.setText(workTimeStr);
        }
        if (workNotes != null){
            workNotes.setText(remark);
        }
        try {
            BaiduMapUtil.drawAnotherPointByGeo(getActivity(), baiduMap, new LatLng(Double.parseDouble(positionLat), Double.parseDouble(positionLon)), shopName);
        }catch (NumberFormatException e){
            L.d(TAG, "NumberFormatException");
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        order_number.setText(orderNumber_str);
        orderType.setText(orderType_str);
        orderOwner.setText(orderOwner_str);
        contact_phone.setText(contact_phone_str);
        shopsLocation.setText(shopsLocation_str);
        shopsAlias.setText(shopsAlias_str);
        agree_end_time.setText(agreeEndTime);
        create_time.setText(createOrderTime);
        if (TextUtils.isEmpty(photoUrl)){
            rl1.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
        }else {
            rl1.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            Myadapter myadapter;
            final String[] urlOrder;
            if (photoUrl.contains(",")) {
                urlOrder = photoUrl.split(",");
            } else {
                urlOrder = new String[]{photoUrl};
            }
            myadapter = new Myadapter(getContext(), urlOrder);
            order_grid.setAdapter(myadapter);

            order_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    openImage(position, urlOrder);
                }
            });
        }
    }


    private void setListener() {
//        rootView.findViewById(R.id.indent_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickImage(v);
//            }
//        });



        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                myBDLocationListener = new BaiduMapUtil.MyListener(getActivity(),baiduMap,distance, null, "4S店", null);
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置
                BaiduMapUtil.locate(baiduMap,scanTime, new LocationClient(getActivity().getApplicationContext()),myBDLocationListener);

            }
        });

        if (isClick){
            markerClickListener = new BaiduMapUtil.MarkerCilckListener();
            baiduMap.setOnMarkerClickListener(markerClickListener);
        }


        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(contact_phone_str)){
                    Uri uri = Uri.parse("tel:" + contact_phone_str);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }

            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**查看订单放大图
     * @param v
     */
    public void onClickImage(View v){
        Bundle bundle = new Bundle();
        bundle.putStringArray("IMAGE_URL", new String[]{photoUrl});
        startActivity(EnlargementActivity.class, bundle);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onStop() {
        super.onStop();
        if(mReceiver!=null){
            getActivity().unregisterReceiver(mReceiver);  //注销网络监听的广播
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initNetManager(Context context){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiver, mFilter);
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

    /** 查看图片
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls){
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }

    class Myadapter extends BaseAdapter {
        private Context context;
        private String[] urlItem;

        public Myadapter(Context context, String[] urlItem) {
            this.context = context;
            this.urlItem = urlItem;
        }

        @Override
        public int getCount() {
            return urlItem.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_image, viewGroup, false);
                imageView = (ImageView) view.findViewById(R.id.imgGridItem);
//                imageView.setLayoutParams(new GridView.LayoutParams(display.getWidth() / 3,display.getWidth() / 3));
//                GridView.LayoutParams params = new GridView.LayoutParams(display.getWidth() / 3, display.getWidth() / 3);
//                view.setLayoutParams(params);
                view.setTag(imageView);
            } else {
                imageView = (ImageView) view.getTag();
            }
//            imageView.setImageResource(imgUrl[position]);
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + urlItem[position], imageView, false);


            return view;
        }
    }
}
