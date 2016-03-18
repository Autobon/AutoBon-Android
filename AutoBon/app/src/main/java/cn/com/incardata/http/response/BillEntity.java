package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/7.
 * 账单实体类
 */
public class BillEntity extends BaseEntity{
    private Bill_Data data;

    public Bill_Data getData() {
        return data;
    }

    public void setData(Bill_Data data) {
        this.data = data;
    }
}
