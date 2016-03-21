package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/21.
 */
public class BillOrderEntity extends BaseEntity{
    private BillOrder_Data data;

    public BillOrder_Data getData() {
        return data;
    }

    public void setData(BillOrder_Data data) {
        this.data = data;
    }
}
