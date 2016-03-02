package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/1.
 */
public class SignInEntity extends BaseEntity{
    private SignIn_Data data;

    public SignIn_Data getData() {
        return data;
    }

    public void setData(SignIn_Data data) {
        this.data = data;
    }
}
