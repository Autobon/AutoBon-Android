package cn.com.incardata.http.response;

/**
 * 抢单
 * Created by wanghao on 16/3/9.
 */
public class TakeupEntity extends BaseEntity{
    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
