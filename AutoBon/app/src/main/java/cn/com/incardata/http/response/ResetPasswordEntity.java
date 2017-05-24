package cn.com.incardata.http.response;

/**
 * 找回密码
 * Created by wanghao on 16/2/19.
 */
public class ResetPasswordEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
