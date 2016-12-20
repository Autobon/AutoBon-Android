package cn.com.incardata.http.response;

/**
 * 提交认证
 * Created by wanghao on 16/2/26.
 */
public class CommitCertificateEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
