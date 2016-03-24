package cn.com.incardata.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.igexin.sdk.PushManager;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.PushIDEntity;
import cn.com.incardata.utils.L;

public class AService extends Service {
    private boolean isRun;

    public AService() {
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public boolean isRun() {
        return isRun;
    }

    public class LocalBinder extends Binder {
        public AService getService() {
            return AService.this;
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
        Intent intent = new Intent(this, AService.class);
        startService(intent);
    }
}
