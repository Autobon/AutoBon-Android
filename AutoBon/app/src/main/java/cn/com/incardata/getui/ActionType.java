package cn.com.incardata.getui;

/**
 * 透传消息类型
 * Created by wanghao on 16/3/3.
 */
public class ActionType {
    public final static String NAME = "action";
    public static final String EXTRA_DATA = "extra_data";

    /**
     * 推送新订单
     */
    public final static String NEW_ORDER = "NEW_ORDER";
    /**
     * 已有人抢单
     */
    public final static String TAKEN_UP = "TAKEN_UP";
    /**
     * 已发送合作邀请并等待结果
     */
    public final static String SEND_INVITATION = "SEND_INVITATION";
    /**
     * 合作邀请已接受
     */
    public final static String INVITATION_ACCEPTED = "INVITATION_ACCEPTED";
    /**
     * 合作邀请已拒绝
     */
    public final static String INVITATION_REJECTED = "INVITATION_REJECTED";
    /**
     * 邀请伙伴
     */
    public final static String INVITE_PARTNER = "INVITE_PARTNER";
    /**
     * 订单开始工作中
     */
    public final static String IN_PROGRESS = "IN_PROGRESS";
    /**
     * 签到
     */
    public final static String SIGNED_IN = "SIGNED_IN";
    /**
     * 订单已结束
     */
    public final static String FINISHED = "FINISHED";
    /**
     * 订单已评论
     */
    public final static String COMMENTED = "COMMENTED";
    /**
     * 订单已取消
     */
    public final static String CANCELED = "CANCELED";
    /**
     * 认证通过
     */
    public final static String VERIFICATION_SUCCEED = "VERIFICATION_SUCCEED";
    /**
     * 认证失败
     */
    public final static String VERIFICATION_FAILED = "VERIFICATION_FAILED";
    /**
     * 通知消息
     */
    public final static String NEW_MESSAGE = "NEW_MESSAGE";
    /**
     * 订单完成
     */
    public final static String ORDERFINISH = "COOPERATION";

    //--------------------------消息广播---------------------------------------
    /**
     * 订单广播
     */
    public final static String ACTION_ORDER = "cn.com.incardata.ACTION_ORDER";
    /**
     * 邀请广播
     */
    public final static String ACTION_INVITATION = "cn.com.incardata.ACTION_INVITATION";
    /**
     * 认证通过
     */
    public final static String ACTION_VERIFIED = "cn.com.incardata.ACTION_VERIFIED";
}
