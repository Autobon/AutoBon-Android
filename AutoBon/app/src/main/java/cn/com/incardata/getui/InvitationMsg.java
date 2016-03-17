package cn.com.incardata.getui;

import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.Order;

/**
 * 合作邀请
 * Created by wanghao on 16/3/17.
 */
public class InvitationMsg extends BaseMsg{
    private Order order;
    private MyInfo_Data owner;
    private MyInfo_Data partner;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MyInfo_Data getOwner() {
        return owner;
    }

    public void setOwner(MyInfo_Data owner) {
        this.owner = owner;
    }

    public MyInfo_Data getPartner() {
        return partner;
    }

    public void setPartner(MyInfo_Data partner) {
        this.partner = partner;
    }
}
