package cn.com.incardata.http.response;

/**
 * 上传身份证照片
 * Created by wanghao on 16/3/1.
 */
public class IdPhotoEntity extends BaseEntity{
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
