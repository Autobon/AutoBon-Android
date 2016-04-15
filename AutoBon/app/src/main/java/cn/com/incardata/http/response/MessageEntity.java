package cn.com.incardata.http.response;

/**
 * 通知消息
 * Created by wanghao on 16/4/14.
 */
public class MessageEntity extends BaseEntity{
    private Message_Data data;

    public Message_Data getData() {
        return data;
    }

    public void setData(Message_Data data) {
        this.data = data;
    }
}
