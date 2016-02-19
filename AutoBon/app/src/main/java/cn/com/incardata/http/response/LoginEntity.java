package cn.com.incardata.http.response;

/**
 * 登录
 * Created by wanghao on 16/2/17.
 */
public class LoginEntity extends BaseEntity{
    private Login_Data data;

    public Login_Data getData() {
        return data;
    }

    public void setData(Login_Data data) {
        this.data = data;
    }

}
