package cn.com.incardata.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.igexin.sdk.PushManager;

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

    /**
     * 上传cid到后台
     */
    private void uploadClientId() {
//        Http.getInstance().postTaskToken(NetURL.PUSH_ID, PushIDEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                if (entity == null){
//                    L.d("Getui", "cid上传失败");
//                    uploadClientId();
//                    return;
//                }
//                if (entity instanceof PushIDEntity && !((PushIDEntity) entity).isResult()){
//                    L.d("Getui", "cid上传失败");
//                    uploadClientId();
//                }
//            }
//        }, new BasicNameValuePair("pushId", PushManager.getInstance().getClientid(this)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRun(false);
        Intent intent = new Intent(this, AutobonService.class);
        startService(intent);
    }

//    private void showNotification(int typeid,String title,String message){
//		Intent intent = new Intent(this, MainActivity.class);//TEST
//		intent.putExtra("notification", typeid);
//		intent.putExtra("message", title);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, typeid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		Notification.Builder builder = new Notification.Builder(this)
//		.setContentTitle(title)
//		.setContentText(message)
//		.setContentIntent(contentIntent)
//		.setSmallIcon(R.drawable.logo)
//		.setAutoCancel(true)
//		.setTicker("您有一条新的消息！")
//		.setWhen(System.currentTimeMillis());
//
//		Notification n = null;
//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//			n = builder.getNotification();
//		} else {
//			n  = builder.build();
//		}
//		n.number++;
//		mNotificationManager.notify(typeid, n);
}
