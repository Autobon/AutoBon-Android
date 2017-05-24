package cn.com.incardata.http.response;

/**
 * 未完成订单
 * Created by wanghao on 16/3/9.
 */
public class ListUnfinishedEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    //    private ListUnfinished_Data data;
//
//    public ListUnfinished_Data getData() {
//        return data;
//    }
//
//    public void setData(ListUnfinished_Data data) {
//        this.data = data;
//    }
}
