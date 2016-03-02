package cn.com.incardata.http.response;

/**
 * 提交认证
 * Created by wanghao on 16/2/26.
 */
public class CommitCertificateEntity extends BaseEntity{
    private Object data;

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
