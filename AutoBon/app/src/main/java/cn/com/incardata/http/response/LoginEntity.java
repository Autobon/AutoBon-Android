package cn.com.incardata.http.response;

/**
 * 登录
 * Created by wanghao on 16/2/17.
 */
public class LoginEntity extends BaseEntityTwo{
   private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
