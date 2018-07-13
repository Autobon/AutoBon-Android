package cn.com.incardata.http.response;

/**
 * 提现返回数据
 * <p>Created by wangyang on 2017/10/24.</p>
 */
public class TakeCashEntity extends BaseEntityTwo {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
