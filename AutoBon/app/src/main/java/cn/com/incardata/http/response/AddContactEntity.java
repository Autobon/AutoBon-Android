package cn.com.incardata.http.response;

/** 添加技师实体类
 * Created by zhangming on 2016/2/28.
 */
public class AddContactEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
