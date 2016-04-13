package cn.com.incardata.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.igexin.sdk.PushManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.PushIDEntity;
import cn.com.incardata.http.response.ReportLocationEntity;
import cn.com.incardata.utils.L;

public class AutobonService extends Service {
    private boolean isRun;

    public AutobonService() {
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public boolean isRun() {
        return isRun;
    }

    public class LocalBinder extends Binder {
        public AutobonService getService() {
            return AutobonService.this;
        }
    }
    private IBinder binder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PushManager.getInstance().initialize(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_REDELIVER_INTENT;
        setRun(true);
        uploadClientId();
        return super.onStartCommand(intent, flags, startId);
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10){
                uploadClientId();
            }
        }
    };

    /**
     * 上传cid到后台
     */
    private void uploadClientId() {
        Http.getInstance().postTaskToken(NetURL.PUSH_ID, PushIDEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){
                    L.d("Getui", "cid上传失败");
                    mHandler.sendEmptyMessageDelayed(10, 5000);
                    return;
                }
                if (entity instanceof PushIDEntity && !((PushIDEntity) entity).isResult()){
                    L.d("Getui", "cid上传失败");
                    mHandler.sendEmptyMessageDelayed(0, 5000);
                }
            }
        }, new BasicNameValuePair("pushId", PushManager.getInstance().getClientid(this)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRun(false);
//        Intent intent = new Intent(this, AutobonService.class);
//        startService(intent);
    }
//---------------------------------------------启动百度定位位置上传（5分钟一次）--------------------------------
    public LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    /**
     * 启动位置同步
     */
    public void startBDLocal(){
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(getClientOption());
        mLocationClient.start();
    }

    public LocationClientOption getClientOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(300000);  //设置扫描定位时间5min
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        return option;
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null){
                L.d("BDLocation", bdLocation.getAddrStr() == null ? "" : bdLocation.getAddrStr());
                uploadMyLocal(bdLocation);
            }
        }

        private void uploadMyLocal(BDLocation bdLocation){
            List<NameValuePair> mParams = new ArrayList<NameValuePair>();
            mParams.add(new BasicNameValuePair("lng", String.valueOf(bdLocation.getLongitude())));
            mParams.add(new BasicNameValuePair("lat", String.valueOf(bdLocation.getLatitude())));
            mParams.add(new BasicNameValuePair("province", bdLocation.getProvince()));
            mParams.add(new BasicNameValuePair("city", bdLocation.getCity()));
            mParams.add(new BasicNameValuePair("district", bdLocation.getDistrict()));

            Http.getInstance().postTaskToken(NetURL.REPORT_MY_ADDRESS, ReportLocationEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null)return;
                    L.d("BDLocation-uploadLocal", "isResult=" + ((ReportLocationEntity)entity).isResult());
                }
            }, (BasicNameValuePair[]) mParams.toArray(new BasicNameValuePair[mParams.size()]));
        }
    }
}
