package cn.com.incardata.http.response;

/**
 * 上传身份证照片
 * Created by wanghao on 16/3/1.
 */
public class IdPhotoEntity extends BaseEntityTwo{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
