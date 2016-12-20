package cn.com.incardata.http.response;

/**放弃订单
 * Created by wanghao on 16/6/29.
 */
public class DropOrderEntity extends BaseEntityTwo {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
