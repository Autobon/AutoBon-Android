package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/1.
 * 认证进度
 */
public class AuthorizationProgressEntity extends BaseEntity{
    private AuthorizationProgress_Data data;

    public AuthorizationProgress_Data getData() {
        return data;
    }

    public void setData(AuthorizationProgress_Data data) {
        this.data = data;
    }
}
