package cn.com.incardata.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incardata.autobon.R;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.utils.L;

/**
 * 个推
 * Created by wanghao on 16/3/2.
 */
public class GeTuiPushReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    if (TextUtils.isEmpty(data)){
                        Log.d("Getui", "receiver payload : [data = null");
                        return;
                    }
                    Log.d("Getui", "receiver payload : " + data);
                    processMessage(context, data);

                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                L.d("Getui", cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;
        }
    }

    private void processMessage(Context context, String msg){
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String action = jsonObject.getString(ActionType.NAME);

            if (ActionType.NEW_ORDER.equals(action)){
                Intent intent = new Intent(ActionType.ACTION_ORDER);
                intent.putExtra(ActionType.NEW_ORDER, msg);
                context.sendBroadcast(intent);
//            }else if (ActionType.VERIFICATION_SUCCEED.equals(action)){
                showNotification(context, "认证通知", "认证通过");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Getui", "透传的josn格式错误");
        }
    }

    private void showNotification(Context context, String title, String message) {
        Intent intent = new Intent();
        intent.putExtra("notification", 0);
        intent.putExtra("message", title);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setTicker("您有一条新的消息！")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis());

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.getNotification();
        } else {
            n = builder.build();
        }
        n.number++;
        mNotificationManager.notify(0, n);
    }
}
