package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/6.
 */
public class StartWorkEntity extends BaseEntity{
    private OrderInfo_Construction data;

    public OrderInfo_Construction getData() {
        return data;
    }

    public void setData(OrderInfo_Construction data) {
        this.data = data;
    }
}
