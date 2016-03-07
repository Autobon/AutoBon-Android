package cn.com.incardata.getui;

/**
 * 透传消息类型
 * Created by wanghao on 16/3/3.
 */
public class ActionType {
    public final static String NAME = "action";

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
     * 订单开始工作中
     */
    public final static String IN_PROGRESS = "IN_PROGRESS";
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
     * 订单广播
     */
    public final static String ACTION_ORDER = "cn.com.incardata.ACTION_ORDER";

}