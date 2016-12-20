package cn.com.incardata.http.response;

/**可抢订单列表（没有人抢的单）
 * Created by wanghao on 16/4/20.
 */
public class ListNewEntity extends BaseEntityTwo {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    //    private ListNew_Data data;
//
//    public ListNew_Data getData() {
//        return data;
//    }
//
//    public void setData(ListNew_Data data) {
//        this.data = data;
//    }
}
