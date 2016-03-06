package cn.com.incardata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

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
                    processMessage(context, data);
                    Log.d("Getui", "receiver payload : " + data);

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
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Getui", "透传的josn格式错误");
        }
    }
}
