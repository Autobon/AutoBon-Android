package cn.com.incardata.http.response;

/**
 * 更新个推ID
 * Created by wanghao on 16/3/2.
 */
public class PushIDEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
