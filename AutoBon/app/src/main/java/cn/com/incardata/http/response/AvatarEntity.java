package cn.com.incardata.http.response;

/**
 * 上传头像
 * Created by wanghao on 16/2/29.
 */
public class AvatarEntity extends BaseEntity{
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
