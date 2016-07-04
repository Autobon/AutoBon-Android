package cn.com.incardata.getui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 透传消息基类
 * Created by wanghao on 16/3/3.
 */
public class BaseMsg implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.title);
    }

    public BaseMsg() {
    }

    protected BaseMsg(Parcel in) {
        this.action = in.readString();
        this.title = in.readString();
    }

}
