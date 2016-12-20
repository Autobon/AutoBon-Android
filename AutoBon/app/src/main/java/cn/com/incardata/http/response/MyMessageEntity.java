package cn.com.incardata.http.response;

/** 二期 我的信息实体类
 * Created by yang on 2016/12/1.
 */
public class MyMessageEntity extends BaseEntityTwo {
    private Object message;


    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
