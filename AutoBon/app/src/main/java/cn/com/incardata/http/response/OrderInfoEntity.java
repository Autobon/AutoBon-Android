package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/10.
 * 订单详细信息
 */
public class OrderInfoEntity extends BaseEntity{
    private OrderInfo_Data data;

    public OrderInfo_Data getData() {
        return data;
    }

    public void setData(OrderInfo_Data data) {
        this.data = data;
    }
}
