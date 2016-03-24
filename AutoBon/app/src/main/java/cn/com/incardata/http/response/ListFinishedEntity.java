package cn.com.incardata.http.response;

/**
 * 已完成订单列表（与未完成订单的数据相同）
 * Created by wanghao on 16/3/21.
 */
public class ListFinishedEntity extends BaseEntity{
    private ListUnfinished_Data data;

    public ListUnfinished_Data getData() {
        return data;
    }

    public void setData(ListUnfinished_Data data) {
        this.data = data;
    }
}
