package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/** 二期 未完成订单列表实体类
 * Created by yang on 2016/11/9.
 */
public class ListUnfinishedOrderEntity extends BaseEntityTwo {

    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
    //    private ListUnFinishOrder message;
//
//    public ListUnFinishOrder getMessage() {
//        return message;
//    }
//
//    public void setMessage(ListUnFinishOrder message) {
//        this.message = message;
//    }
}
