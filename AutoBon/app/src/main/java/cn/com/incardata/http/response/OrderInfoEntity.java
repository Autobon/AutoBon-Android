package cn.com.incardata.http.response;

import android.os.Parcel;

/** 二期 订单详情实体类
 * Created by zhangming on 2016/3/10.
 * 订单详细信息
 */
public class OrderInfoEntity extends BaseEntityTwo{
   private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
