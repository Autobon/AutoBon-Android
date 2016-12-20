package cn.com.incardata.http.response;

/**
 * 注册
 * Created by wanghao on 16/2/18.
 */
public class RegisterEntity extends BaseEntityTwo{
   private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
