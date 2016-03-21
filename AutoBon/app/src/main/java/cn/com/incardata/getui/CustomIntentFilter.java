package cn.com.incardata.getui;

import android.content.IntentFilter;

/**
 * Created by wanghao on 16/3/3.
 */
public class CustomIntentFilter {

    /**
     * 订单
     * @return
     */
    public static IntentFilter getOrderIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionType.ACTION_ORDER);
        return intentFilter;
    }

    /**
     * 订单＋邀请
     * @return
     */
    public static IntentFilter getInvitationIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionType.ACTION_INVITATION);
        intentFilter.addAction(ActionType.ACTION_ORDER);
        return intentFilter;
    }
}
