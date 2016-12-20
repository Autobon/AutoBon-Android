package cn.com.incardata.http.response;

/**
 * 更改密码
 * Created by wanghao on 16/2/19.
 */
public class ChangePasswordEntity extends BaseEntityTwo{

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    //    private Object data;
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
}
