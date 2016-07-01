package cn.com.incardata.getui;

import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo_Cooperator;

/**
 * 订单消息
 * Created by wanghao on 16/3/3.
 */
public class OrderMsg extends BaseMsg{
    private Order order;
    private OrderInfo_Cooperator cooperator;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
