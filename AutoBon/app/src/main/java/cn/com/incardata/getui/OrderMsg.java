package cn.com.incardata.getui;

import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo;

/**
 * 订单消息
 * Created by wanghao on 16/3/3.
 */
public class OrderMsg extends BaseMsg{
    private OrderInfo order;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
