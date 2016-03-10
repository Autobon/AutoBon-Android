package cn.com.incardata.fragment;


import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;

/**
 * Created by wanghao on 16/3/8.
 */
public class OrderDialogFragment extends DialogFragment {
    private final static String TAG = "IndentMapFragment";
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;

    private String positionLon;
    private String positionLat;
    private String workTimeStr;
    private String photoUrl;
    private String remark;
    private String shopName;

    private OnFragmentInteractionListener mListener;
    private BDLocationListener myBDLocationListener;
    private View rootView;
    private TextView distance;
    private ImageView indentImage;
    private TextView indentText;
    private TextView workTime;
    private TextView workNotes;

    public OrderDialogFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        if (getArguments() != null) {
            positionLon = getArguments().getString("PositionLon", "0.0");
            positionLat =  getArguments().getString("PositionLat", "0.0");
            photoUrl =  getArguments().getString("Photo", "http:");
            workTimeStr =  getArguments().getString("OrderTime", "2016-03-02 02:14");
            remark =  getArguments().getString("Remark", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_indent_map, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        initViews();
        return rootView;
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
        initNetManager(getActivity());  //网络状态切换监听
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
            BaiduMapUtil.drawAnotherPointByGeo(getActivity(), baiduMap, new LatLng(Double.parseDouble(positionLat), Double.parseDouble(positionLon)), shopName);
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
                myBDLocationListener = new BaiduMapUtil.MyListener(getActivity(),baiduMap,distance, null, "4S店", null);
                //tv_distance为下方显示距离的TextView控件,mAddress为另一个点的位置
                BaiduMapUtil.locate(baiduMap, BaiduMapFragment.scanTime, new LocationClient(getActivity()),myBDLocationListener);
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
}