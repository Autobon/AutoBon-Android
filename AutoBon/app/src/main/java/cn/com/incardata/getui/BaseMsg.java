package cn.com.incardata.getui;

/**
 * 透传消息基类
 * Created by wanghao on 16/3/3.
 */
public class BaseMsg {
    private String action;
    private String title;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
